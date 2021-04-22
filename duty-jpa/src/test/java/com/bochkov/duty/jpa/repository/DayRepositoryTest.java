package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.HolidayDay;
import com.bochkov.duty.jpa.service.HolidayService;
import com.bochkov.duty.xmlcalendar.XmlCalendarConfig;
import com.bochkov.duty.xmlcalendar.service.XmlCalendarClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {XmlCalendarConfig.class, DutyJpaConfig.class})
@Slf4j
public class DayRepositoryTest {
    @Autowired
    DayRepository dayRepository;

    @Autowired
    XmlCalendarClient calendarClient;

    @Autowired
    HolidayService holidayService;

    @Test
    public void testCreateDaysForToday() {
        Day day = dayRepository.findOrCreate(LocalDate.now());
        log.info("{}", day);
    }

    @Test
    public void testCreateDaysForYear() {
        dayRepository.findOrCreate(LocalDate.of(2020,1,1),LocalDate.of(2020,12,31));
    }

    @Test
    public void testDownloadAndSaveXml() {
        List<HolidayDay> holidays = holidayService.downloadAndSave(2020);
        log.info("{}", holidays);

    }

    @Test
    public void testCreateDaysForMonth() {
        List<Day> days = dayRepository.findOrCreate(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
        days.forEach(d -> log.info("{}", d));
        List<HolidayDay> holidays = holidayService.downloadAndSave(2020);
        log.info("{}", holidays);

        Day day = dayRepository.findOrCreate(LocalDate.of(2020,1,1));
        log.info("{}", day);

    }
}
