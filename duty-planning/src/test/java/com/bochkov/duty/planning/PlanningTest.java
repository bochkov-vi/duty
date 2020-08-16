package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Shift;
import com.bochkov.duty.jpa.entity.ShiftAssignment;
import com.bochkov.duty.jpa.repository.*;
import com.bochkov.duty.jpa.entity.ShiftRostering;
import com.bochkov.duty.planning.PlanningSpringConfiguration;
import com.bochkov.duty.jpa.entity.EmployeeShiftTypeLimit;
import com.bochkov.duty.planning.service.PlanningService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PlanningSpringConfiguration.class)
public class PlanningTest {

    @Autowired
    PlanningService planningService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    DayRepository dayRepository;

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ShiftTypeRepository shiftTypeRepository;

    LocalDate start = LocalDate.of(2020, 01, 01);

    LocalDate end = LocalDate.of(2020, 01, 31);


    @Test
    public void createReport() {

        ShiftRostering shiftRostering = new ShiftRostering();

        shiftRostering.setEmployeeShiftTypeLimits(
                Lists.newArrayList(
                        employeeRepository.findById("nod1").map(EmployeeShiftTypeLimit::new).map(l -> l.setMax(1)).get()
                        , employeeRepository.findById("nod2").map(EmployeeShiftTypeLimit::new).map(l -> l.setMax(2)).get()
                        , employeeRepository.findById("nod3").map(EmployeeShiftTypeLimit::new).map(l -> l.setMax(3)).get()
                        , employeeRepository.findById("nod4").map(EmployeeShiftTypeLimit::new).map(l -> l.setMax(4)).get()
                        , employeeRepository.findById("nod5").map(EmployeeShiftTypeLimit::new).map(l -> l.setMax(5)).get()
                )
        );

        //dayRepository.findOrCreate(start, start.plusDays(9)).forEach(day -> dayRepository.safeSave(day.setWeekend(true)));
        shiftRostering.setDays(dayRepository.findOrCreate(start, end));

        shiftRostering.setEmployees(employeeRepository.findAll().stream().limit(8).collect(Collectors.toList()));
        shiftRostering.setShiftTypes(shiftTypeRepository.findAllById(Lists.newArrayList(3)));
        int[] index = new int[]{0};

        shiftRostering.setShifts(
                dayRepository.findOrCreate(start, end).stream()
                        .map(d -> Shift.of(d, shiftTypeRepository.findById(3).get()).setId(index[0]++)).collect(Collectors.toList()));

        shiftRostering.getShifts().addAll(dayRepository.findOrCreate(start, start.plusDays(9)).stream()
                .map(d -> Shift.of(d, shiftTypeRepository.findById(4).get()).setId(index[0]++)).collect(Collectors.toList()));

//        dutyRoster.getShifts().addAll(dayRepository.findOrCreate(start, end).stream()
//                .map(d -> Shift.of(d, shiftTypeRepository.findById(5).get()).setId(index[0]++)).collect(Collectors.toList()));
        int[] id = new int[]{0, 0};
        shiftRostering.setShiftAssignments(shiftRostering.getShifts().stream().map(s -> s.setId(id[0]++))
                .map(ShiftAssignment::of).map(sa -> sa.setId(id[1]++)).collect(Collectors.toList()));

        ShiftRostering plan = planningService.solve(shiftRostering);


        System.out.println(plan);
        Map<Employee, Long> map = plan.getShiftAssignments().stream().collect(Collectors.groupingBy(ShiftAssignment::getEmployee, Collectors.counting()));
        System.out.println(Joiner.on("\n").withKeyValueSeparator("->").join(map));
        System.out.println("========== ВЫХОДНЫЕ ==========");
        System.out.println(Joiner.on("\n").withKeyValueSeparator("->").join(
                plan.getShiftAssignments().stream().filter(ShiftAssignment::isWeekend).collect(Collectors.groupingBy(ShiftAssignment::getEmployee, Collectors.counting()))
        ));

        System.out.println("========== ВРЕМЯ ==========");
        System.out.println(Joiner.on("\n").join(
                plan.getShiftAssignments().stream().collect(
                        Collectors.groupingBy(
                                ShiftAssignment::getEmployee,
                                Collectors.summingLong(da -> da.getOverTime().toMinutes()))
                ).entrySet().stream().map(e -> Pair.of(e.getKey(), Duration.ofMinutes(e.getValue())))
                        .sorted(Comparator.comparing(Pair::getValue))
                        .collect(Collectors.toList())
        ));

//        System.out.println("====================================");
//        System.out.println(plan.getDuties().stream().sorted(Comparator.comparing(ShiftAssignment::getDay)).map(d -> String.format("%s %s %s", d.isWeekend(), d.getDay(), d.getEmployee())).collect(Collectors.joining("\n")));

        System.out.println(plan.getScore());
        System.out.println(plan.asTextTable());
    }

    @Test
    public void testStatistic() {

        SummaryStatistics ss = Stream.of(2, 3, 4, 5, 6, 7, 8, 9, 1).collect(SummaryStatistics::new, SummaryStatistics::addValue, (s1, s2) -> {
        });
        System.out.println(ss);
    }


}
