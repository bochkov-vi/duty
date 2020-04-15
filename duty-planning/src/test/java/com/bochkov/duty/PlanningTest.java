package com.bochkov.duty;

import com.bochkov.duty.jpa.repository.*;
import com.bochkov.duty.planning.DutyAssigment;
import com.bochkov.duty.planning.DutyPlan;
import com.bochkov.duty.planning.PlanningSpringConfiguration;
import com.bochkov.duty.planning.service.PlanningService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.stream.Collectors;

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
    }
}
