package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Period;
import com.bochkov.duty.jpa.entity.UiOptions;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
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
public class DutyTypeRepositoryTests {

    @Autowired
    DutyTypeRepository repository;

    @Before
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void simpleTest() {
        DutyType weekend = newWeekend();
        repository.save(newWorkday2());
        repository.save(newWorkday());
        repository.save(newSoDuty());
    }

    public DutyType newWeekend() {
        return new DutyType("Выходной").setUiOptions(new UiOptions().setFaIcon("fa-home"));
    }

    public DutyType newWorkday() {
        return new DutyType("Рабочий день").setUiOptions(new UiOptions().setFaIcon("fa-circle-o"))
                .setPeriods(Sets.newHashSet(new Period(9, 0, 13, 0), new Period(13, 45, 18, 00)))
                .setDaysToWeekend(Lists.newArrayList(1, 2, 3, 4));
    }

    public DutyType newWorkday2() {
        return new DutyType("Сокр. день").setUiOptions(new UiOptions().setFaIcon("fa-circle-o"))
                .setPeriods(Sets.newHashSet(new Period(9, 0, 13, 0), new Period(13, 45, 17, 45)))
                .setDaysToWeekend(Lists.newArrayList(5));
    }

    public DutyType newSoDuty() {
        return new DutyType("ПОДКНО", 9, 0, 24, 0).setUiOptions(new UiOptions().setFaIcon("fa-circle-o"));
    }
}
