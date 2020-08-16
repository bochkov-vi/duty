package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.OvertimeData;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@SpringBootTest(classes = DutyJpaConfig.class)
@RunWith(SpringRunner.class)
public class DayRepositoryTests {

    @Autowired
    DayRepository dayRepository;

    @Autowired
    ShiftTypeRepository shiftTypeRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    LocalDate date = LocalDate.now();

    Day SATURDAY = new Day(date.with(DayOfWeek.SATURDAY));

    Day MONDAY = new Day(date.with(DayOfWeek.MONDAY));

    Day TUESDAY = new Day(date.with(DayOfWeek.TUESDAY));

    Day WEDNESDAY = new Day(date.with(DayOfWeek.WEDNESDAY));

    Day THURSDAY = new Day(date.with(DayOfWeek.THURSDAY));

    Day FRIDAY = new Day(date.with(DayOfWeek.FRIDAY));

    Day SUNDAY = new Day(date.with(DayOfWeek.SUNDAY));

    @Before
    public void setupTest() {
        dayRepository.deleteAll();
    }

    @Test
    public void saveWithChilds() {
        dayRepository.deleteAll();
        Day day = dayRepository._saveWithChilds(this.THURSDAY);
        Assert.assertEquals(day.getNext(), FRIDAY);
        Assert.assertEquals(day.getNext().getPrev(), day);
        Assert.assertEquals(day.getPrev(), WEDNESDAY);
        Assert.assertEquals(day.getPrev().getNext(), day);
    }

    @Test
    public void saveWithWeekendRelationCalculate() {
        dayRepository.deleteAll();
        THURSDAY = dayRepository._saveWithWeekendRelationCalculate(THURSDAY);
        Assert.assertEquals(THURSDAY.getDaysFromWeekend(), Integer.valueOf(4));
        Assert.assertEquals(THURSDAY.getDaysToWeekend(), Integer.valueOf(2));
    }

    @Test
    public void _save() {
        dayRepository.deleteAll();
        Day day = dayRepository._save(this.THURSDAY);
        Assert.assertEquals(day.getDaysFromWeekend(), Integer.valueOf(4));
        Assert.assertEquals(day.getDaysToWeekend(), Integer.valueOf(2));
        Assert.assertEquals(day.getNext(), FRIDAY);
        Assert.assertEquals(day.getPrev(), WEDNESDAY);
        day.setWeekend(true);
        day = dayRepository._save(day);
        Assert.assertEquals(day.getDaysFromWeekend(), Integer.valueOf(0));
        Assert.assertEquals(day.getDaysToWeekend(), Integer.valueOf(0));
        Assert.assertEquals(day.getPrev().getDaysToWeekend(), Integer.valueOf(1));
        Assert.assertEquals(day.getNext().getDaysFromWeekend(), Integer.valueOf(1));
    }

    @Test
    public void saveForWeek() {
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = start.plusWeeks(1);
        dayRepository.findOrCreate(start, end);
    }

    @Test
    public void saveForMonth() {
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
        dayRepository.findOrCreate(start, end);

    }

    @Test
    public void overtimeCalculates() {
        ShiftType shiftType = shiftTypeRepository.findById(3).get();
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
        for (Day day : dayRepository.findOrCreate(start, end)) {
            OvertimeData overtimeData = shiftType.overtime(day);
            System.out.printf("%s\t%s\t%s\n", shiftType, day, overtimeData);
        }
    }

    @Test
    public void overtimeCalculatesForPerson() {
        ShiftType shiftType = shiftTypeRepository.findById(3).get();
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
        for (Day day : dayRepository.findOrCreate(start, end)) {
            for (Employee p : employeeRepository.findAll()) {
                OvertimeData overtimeData = shiftType.overtime(day,p);
                System.out.printf("%s\t%s\t%s\t%s\t%s\n", shiftType, day, overtimeData, p,p.getRoadToHomeTime());
            }
        }
    }

}
