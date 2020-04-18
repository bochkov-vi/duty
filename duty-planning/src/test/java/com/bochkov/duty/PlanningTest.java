package com.bochkov.duty;

import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.*;
import com.bochkov.duty.planning.DutyAssigment;
import com.bochkov.duty.planning.DutyPlan;
import com.bochkov.duty.planning.PlanningSpringConfiguration;
import com.bochkov.duty.planning.service.PlanningService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    DutyRepository dutyRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    DutyTypeRepository dutyTypeRepository;

    LocalDate start = LocalDate.of(2020, 01, 01);

    LocalDate end = LocalDate.of(2020, 01, 31);


    @Test
    public void createReport() {
        DutyPlan dutyPlan = new DutyPlan();
        dutyPlan.setDays(dayRepository.findOrCreate(start, end));
        dutyPlan.setPersons(personRepository.findAll());
        dutyPlan.setDutyTypes(dutyTypeRepository.findAllById(Lists.newArrayList(3)));
        dutyPlan.setDuties(dutyPlan.getDays().stream()
                .map(d -> new DutyAssigment().setDay(d).setDutyType(dutyTypeRepository.findById(3).get())).collect(Collectors.toList()));
        DutyPlan plan = planningService.solve(dutyPlan);
        System.out.println(plan);
        Map<Person, Long> map = plan.getDuties().stream().collect(Collectors.groupingBy(DutyAssigment::getPerson, Collectors.counting()));
        System.out.println(Joiner.on("\n").withKeyValueSeparator("->").join(map));
        System.out.println("====================================");
        System.out.println(plan.getDuties().stream().sorted(Comparator.comparing(DutyAssigment::getDay)).map(d -> String.format("%s %s", d.getDay(), d.getPerson())).collect(Collectors.joining("\n")));
        System.out.println(plan.getScore());
    }

    @Test
    public void testStatistic() {

        SummaryStatistics ss = Stream.of(2, 3, 4, 5, 6, 7, 8, 9, 1).collect(SummaryStatistics::new, SummaryStatistics::addValue, (s1, s2) -> {
        });
        System.out.println(ss);
    }
}
