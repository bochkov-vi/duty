package com.bochkov.duty.jpa.entity;

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
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.TypeDef;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.persistence.jpa.impl.score.buildin.hardmediumsoft.HardMediumSoftScoreHibernateType;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@PlanningSolution
@Getter
@Setter
@Accessors(chain = true)
@Entity
@javax.persistence.Table(name = "DUTY_ROSTER")
@TypeDef(defaultForType = HardMediumSoftScore.class, typeClass = HardMediumSoftScoreHibernateType.class)
public class DutyRoster extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DUTY_ROSTER_SEQ")
    @SequenceGenerator(name = "DUTY_ROSTER_SEQ", initialValue = 1000, allocationSize = 1)
    @Column(name = "ID_DUTY_ROSTER")
    Integer id;

    @ManyToMany
    @JoinTable(name = "DUTY_ROSTER_SHIFT_TYPE", joinColumns = @JoinColumn(name = "ID_DUTY_ROSTER", referencedColumnName = "ID_DUTY_ROSTER")
            , inverseJoinColumns = @JoinColumn(name = "ID_SHIFT_TYPE", referencedColumnName = "ID_SHIFT_TYPE"))
    @ProblemFactCollectionProperty
    List<ShiftType> shiftTypes;

    @OneToMany(mappedBy = "dutyRoster", cascade = CascadeType.ALL, orphanRemoval = true)
    @ProblemFactCollectionProperty
    List<EmployeeShiftTypeLimit> employeeShiftTypeLimits;

    @ManyToMany
    @JoinTable(name = "DUTY_ROSTER_DAY", joinColumns = @JoinColumn(name = "ID_DUTY_ROSTER", referencedColumnName = "ID_DUTY_ROSTER")
            , inverseJoinColumns = @JoinColumn(name = "DATE", referencedColumnName = "DATE"))
    @ProblemFactCollectionProperty
    List<Day> days;

    @ManyToMany
    @JoinTable(name = "DUTY_ROSTER_SHIFT", joinColumns = @JoinColumn(name = "ID_DUTY_ROSTER", referencedColumnName = "ID_DUTY_ROSTER")
            , inverseJoinColumns = @JoinColumn(name = "ID_SHIFT", referencedColumnName = "ID_SHIFT"))
    @ProblemFactCollectionProperty
    List<Shift> shifts;


    @ValueRangeProvider(id = "employees")
    @ManyToMany
    @JoinTable(name = "DUTY_ROSTER_EMPLOYEE", joinColumns = @JoinColumn(name = "ID_DUTY_ROSTER", referencedColumnName = "ID_DUTY_ROSTER")
            , inverseJoinColumns = @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE"))
    @ProblemFactCollectionProperty
    List<Employee> employees;

    @PlanningEntityCollectionProperty
    @ManyToMany
    @JoinTable(name = "DUTY_ROSTER_EMPLOYEE", joinColumns =
    @JoinColumn(name = "ID_DUTY_ROSTER", referencedColumnName = "ID_DUTY_ROSTER"),
            inverseJoinColumns = @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE"))
    List<ShiftAssignment> shiftAssignments;


    @PlanningScore
    @Columns(columns = {@Column(name = "initScore"),
            @Column(name = "hardScore"), @Column(name = "mediumScore"), @Column(name = "softScore")})
    private HardMediumSoftScore score;

    public static void printData(DutyRoster plan, PrintStream printStream, int indent) {
        List<Employee> employeeList = plan.getEmployees();
        int startIndex = 2;
        Table<Integer, String, String> table = plan.getShiftAssignments().stream().collect(Tables.toTable(da -> employeeList.indexOf(da.getEmployee()) + startIndex
                , da -> da.getDay().getId().format(DateTimeFormatter.ofPattern("dd"))
                , da -> da.getShiftType().getUiOptions().getPlainText(),
                () -> TreeBasedTable.create()));
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
