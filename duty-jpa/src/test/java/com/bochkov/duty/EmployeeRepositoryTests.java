package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.duty.jpa.repository.EmployeeGroupRepository;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DutyJpaConfig.class)
public class EmployeeRepositoryTests {

    @Autowired
    EmployeeRepository repository;

    @Autowired
    RangRepository rangRepository;

    @Autowired
    EmployeeGroupRepository employeeGroupRepository;

    @Autowired
    ShiftTypeRepository shiftTypeRepository;

    @Before
    public void setupTest() {
        repository.deleteAll();
    }

    @Test
    public void saveBochkov() {
        Employee employee = new Employee().setId("bochkov")
                .setRang(rangRepository.getOne((short) 25))
                .setFirstName("Виктор")
                .setLastName("Бочков")
                .setMiddleName("Иванович")
                .setEmployeeGroup(employeeGroupRepository.getOne(2))
                .setShiftTypes(Sets.newHashSet(shiftTypeRepository.findAll()));
        repository.save(employee);
    }


}
