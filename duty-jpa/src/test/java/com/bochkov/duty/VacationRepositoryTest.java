package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DutyJpaConfig.class)
public class VacationRepositoryTest {

    @Autowired
    VacationRepository repository;

    @Autowired
    EmployeeRepository employeeRepository;

    List<Vacation> list = null;

    @Before
    public void setup() {
        Vacation vacation1 = Vacation.of(employeeRepository.findByLogin("bochkov").get(), LocalDate.of(2020, 5, 2), LocalDate.of(2020, 5, 26));
        Vacation vacation2 = Vacation.of(employeeRepository.findByLogin("bochkov").get(), LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 15));
        list = Lists.newArrayList(vacation1, vacation2);
    }

    @Test
    public void testCreate() {
        list = repository.saveAll(list);
        System.out.println(list);
        testDelete();
    }

    @Test
    public void testDelete() {
        for (Vacation vacation : list) {
            if (repository.existsById(vacation.getId())) {
                repository.deleteById(vacation.getId());
            }
            System.out.println(vacation);
        }
    }
}
