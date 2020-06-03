package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.entity.VacationPart;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DutyJpaConfig.class)
public class VacationRepositoryTest {

    @Autowired
    VacationRepository repository;

    @Autowired
    EmployeeRepository employeeRepository;

    Vacation vacation = null;

    @Before
    public void setup() {
        vacation = Vacation.of(2020, employeeRepository.getOne("bochkov"));
        vacation.setParts(Sets.newTreeSet(
                Sets.newHashSet(
                        VacationPart.of(vacation, LocalDate.of(2020, 5, 2), LocalDate.of(2020, 5, 26), 1),
                        VacationPart.of(vacation, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 15), 2)
                )));
        testDelete();
        testCreate();
    }

    @Test
    public void testCreate() {
        testDelete();
        vacation = repository.save(vacation);
        System.out.println(vacation);
    }

    @Test
    public void testDelete() {
        if (repository.existsById(vacation.getId())) {
            repository.deleteById(vacation.getId());
        }
        System.out.println(vacation);
    }

    @Test
    public void testExists() {
        Employee employee = employeeRepository.findById("bochkov").get();
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 1, 15);
        repository.existsByPeriod(start, end, employee);
    }

    @Test
    public void findParts() {
        Employee employee = employeeRepository.findById("bochkov").get();
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 1, 15);
        List<VacationPart> parts = repository.findParts(start, end, employee);
        Assert.assertFalse(parts.isEmpty());
    }

    @Test
    public void testPercentOverlap() {
        Employee employee = employeeRepository.findById("bochkov").get();
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 1, 30);
        double po = repository.percentOverlap(start, end, employee);
        Assert.assertFalse(po <= 0);

    }
}
