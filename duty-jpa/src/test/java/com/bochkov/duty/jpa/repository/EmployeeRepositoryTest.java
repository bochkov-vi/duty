package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Employee;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DutyJpaConfig.class)
public class EmployeeRepositoryTest {
    @Autowired
    EmployeeRepository repository;
    @Autowired
    RangRepository rangRepository;


    @Test
    public void createEmployee() {
        Employee employee = new Employee().setId("bochkov").setRang(rangRepository.getOne((short) 25)).setFirstName("Виктор").setMiddleName("Иванович").setLastName("Бочков");
        employee = repository.save(employee);
        Assert.assertNotNull(employee);
    }

    @Test
    public void findByShiftTypesContaining() {
    }

    @Test
    public void findByShiftTypesIn() {
    }
}
