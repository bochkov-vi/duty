package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.entity.VacationPart;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

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
        vacation = Vacation.of(2020, employeeRepository.getOne("bochkov")).setParts(Sets.newTreeSet(
                Sets.newHashSet(
                        VacationPart.of(LocalDate.of(2020, 5, 2), LocalDate.of(2020, 5, 26), 1),
                        VacationPart.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 15), 2)
                )));
    }

    @Test
    public void testCreate() {
        testDelete();
        vacation = repository.save(vacation);
        System.out.println(vacation);
    }

    @Test
    public void testDelete() {
        repository.deleteById(vacation.getId());
        System.out.println(vacation);
    }
}
