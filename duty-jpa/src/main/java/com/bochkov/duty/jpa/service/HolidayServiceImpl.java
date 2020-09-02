package com.bochkov.duty.jpa.service;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Holiday;
import com.bochkov.duty.jpa.entity.HolidayDay;
import com.bochkov.duty.jpa.entity.HolidayPK;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.HolidayDayRepository;
import com.bochkov.duty.jpa.repository.HolidayRepository;
import com.bochkov.duty.xmlcalendar.entity.XmlCalendar;
import com.bochkov.duty.xmlcalendar.entity.XmlDay;
import com.bochkov.duty.xmlcalendar.entity.XmlHoliday;
import com.bochkov.duty.xmlcalendar.service.XmlCalendarClient;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HolidayServiceImpl implements HolidayService {
    @Autowired
    XmlCalendarClient client;

    @Autowired
    HolidayRepository holidayRepository;

    @Autowired
    HolidayDayRepository holidayDayRepository;

    @Autowired
    DayRepository dayRepository;

    @Override
    public List<HolidayDay> downloadAndSave(Integer year) {
        XmlCalendar calendar = client.download(year);
        List<HolidayDay> days = extractDays(calendar);
        holidayDayRepository.saveAll(days);
        List<Holiday> holidays = extractHolidays(calendar);
        holidayRepository.saveAll(holidays);
        for (HolidayDay hd : days) {
            Day day = dayRepository.findOrCreate(hd.getId());
            log.debug("saved day {}", day);
        }
        return days;
    }

    public List<Holiday> extractHolidays(XmlCalendar calendar) {
        List<Holiday> result = Lists.newArrayList();
        for (XmlHoliday h : calendar.getHolidays()) {
            Holiday holiday = holiday(h);
            result.add(holiday);
        }
        return result;
    }

    public List<HolidayDay> extractDays(XmlCalendar calendar) {
        List<HolidayDay> result = Lists.newArrayList();
        for (XmlDay d : calendar.getDays()) {
            HolidayDay day = day(d);
            result.add(day);
        }
        return result;
    }

    Holiday holiday(XmlHoliday xml) {
        Holiday result = new Holiday();
        result.setTitle(xml.getTitle());
        result.setId(new HolidayPK(xml.getCalendar().getYear(), xml.getId()));
        result.setHolidayDays(xml.getDays().stream().map(this::day).map(d -> d.setHoliday(result)).collect(Collectors.toList()));
        return result;
    }

    HolidayDay day(XmlDay xml) {
        HolidayDay result = new HolidayDay();
        result.setId(xml.getDate().get());
        result.setType(type(xml));
        return result;
    }

    HolidayDay.HolidayType type(XmlDay xml) {
        HolidayDay.HolidayType type = null;
        switch (xml.getT()) {
            case 1: {
                type = HolidayDay.HolidayType.WEEKEND;
                break;
            }
            case 2: {
                type = HolidayDay.HolidayType.SHORTENED;
                break;
            }
            case 3: {
                type = HolidayDay.HolidayType.WORKED;
                break;
            }
        }
        return type;
    }
}
