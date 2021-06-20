package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.entity.Period;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DutyJpaConfig.class)
public class ShiftTypeRepositoryTests {

    @Autowired
    ShiftTypeRepository repository;

    @Before
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void simpleTest() {
        ShiftType weekend = newWeekend();
        repository.save(newWorkday2());
        repository.save(newWorkday());
        repository.save(newSoDuty());
    }

    public ShiftType newWeekend() {
        return new ShiftType("Выходной").setIcon("home");
    }

    public ShiftType newWorkday() {
        return new ShiftType("Рабочий день").setIcon("fa-circle-o")
                .setPeriods(Sets.newHashSet(new Period(9, 0, 13, 0), new Period(13, 45, 18, 00)))
                .setDaysToWeekend(Lists.newArrayList(1, 2, 3, 4));
    }

    public ShiftType newWorkday2() {
        return new ShiftType("Сокр. день").setIcon("fa-circle-o")
                .setPeriods(Sets.newHashSet(new Period(9, 0, 13, 0), new Period(13, 45, 17, 45)))
                .setDaysToWeekend(Lists.newArrayList(5));
    }

    public ShiftType newSoDuty() {
        return new ShiftType("ПОДКНО", 9, 0, 24, 0).setIcon("fa-circle-o");
    }
}
