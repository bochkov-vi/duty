package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Report;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DutyJpaConfig.class)
public class ShiftRepositoryTest {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RangRepository rangRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void create() {
        Report report = new Report();
        report.setDate(LocalDate.now());
        report.setDateFrom(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
        report.setDateTo(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
        report.setDateTitle(report.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        report.setGenitiveDepartment("координационного отдела");
        report.setTitle("тестовый график");
        Employee chief = employeeRepository.findById("chief").orElseGet(() -> employeeRepository.saveAndFlush(
                new Employee().setId("chief").setFirstName("chief").setLastName("chief")
                        .setRang(rangRepository.getOne((short) 27))));
        report.setChief(chief);
        report.setEmployees(Sets.newTreeSet(employeeRepository.findAll()));
        report.setExecutor(employeeRepository.getOne("bochkov"));
        reportRepository.save(report);
        reportRepository.delete(report);
    }
}
