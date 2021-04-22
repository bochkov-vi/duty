package com.bochkov.duty.planning.domain;

import com.bochkov.duty.jpa.entity.*;
import com.bochkov.duty.planning.service.DutyTypeInterval;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.common.collect.TreeBasedTable;
import dnl.utils.text.table.GuavaTableModel;
import dnl.utils.text.table.TextTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@PlanningSolution
@Getter
@Setter
@Accessors(chain = true)
public class ShiftRostering implements Serializable {


    List<ShiftType> shiftTypes;


    List<EmployeeShiftTypeLimit> employeeShiftTypeLimits;

      @ProblemFactCollectionProperty
    List<Day> days;

      @ProblemFactCollectionProperty
    List<Shift> shifts;


    @ValueRangeProvider(id = "employees")
       @ProblemFactCollectionProperty
    List<Employee> employees;

    @PlanningEntityCollectionProperty
      List<ShiftAssignment> shiftAssignments;


    @PlanningScore
     private HardMediumSoftScore score;

    public static void printData(ShiftRostering plan, PrintStream printStream, int indent) {
        List<Employee> employeeList = plan.getEmployees();
        int startIndex = 2;
        Table<Integer, String, String> table = plan.getShiftAssignments().stream().collect(Tables.toTable(da -> employeeList.indexOf(da.getEmployee()) + startIndex
                , da -> da.getDay().getId().format(DateTimeFormatter.ofPattern("dd"))
                , da -> da.getShiftType().getUiOptions().getPlainText(),
                TreeBasedTable::create));
        for (Employee employee : employeeList) {
            int index = employeeList.indexOf(employee);
            table.put(index + startIndex, "----", employee.toString());
        }
        table.put(0, "----", "");
        for (Day day : plan.getDays()) {
            table.put(0, day.getId().format(DateTimeFormatter.ofPattern("dd")), day.getId().format(DateTimeFormatter.ofPattern("EE")));
        }

        table.put(1, "----", "");
        for (Day day : plan.getDays()) {
            table.put(1, day.getId().format(DateTimeFormatter.ofPattern("dd")),
                    Optional.ofNullable(day).map(Day::isWeekend).filter(v -> v).map(v -> "*").orElse(""));
        }
        GuavaTableModel model = new GuavaTableModel(table);
        TextTable textTable = new TextTable(model);
        textTable.printTable(printStream, indent);
    }

    public String asTextTable() {
        return asTextTable(0);
    }

    public String asTextTable(int indent) {
        String result = null;
        try (OutputStream out = new ByteArrayOutputStream()) {
            try (PrintStream ps = new PrintStream(out)) {
                printData(this, ps, indent);
            }
            result = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @ProblemFactCollectionProperty
    List<DutyTypeInterval> getDutyTypeIntervalList() {
        Map<ShiftType, Long> map = employees.stream().flatMap(person -> person.getShiftTypes().stream().map(dt -> Pair.of(person, dt)))
                .collect(Collectors.groupingBy(Pair::getValue, Collectors.counting()));
        return map.entrySet().stream().map(e -> new DutyTypeInterval(e.getKey(), e.getValue().intValue() / 2)).collect(Collectors.toList());
    }
}
