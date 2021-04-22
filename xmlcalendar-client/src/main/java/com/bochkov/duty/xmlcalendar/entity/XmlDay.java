package com.bochkov.duty.xmlcalendar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Data
@ToString(of = {"d", "t", "h"})
@XmlRootElement(name = "day")
@Slf4j
public class XmlDay {
    @XmlAttribute(name = "d")
    String d;

    @XmlAttribute(name = "t")
    Integer t;

    @XmlAttribute(name = "h")
    Integer h;

    @JsonBackReference
    XmlCalendar calendar;

    public Optional<XmlHoliday> getHoliday() {
        return calendar.holidays.stream().filter(hd -> Objects.equals(hd.id, this.h)).findFirst();
    }

    public Optional<LocalDate> getDate() {
        LocalDate date = null;
        try {
            String[] data = d.split("\\.", 2);

            date = LocalDate.now().withMonth(Integer.parseInt(data[0])).withDayOfMonth(Integer.parseInt(data[1]));
            date = date.withYear(calendar.year);
        } catch (Exception e) {
            log.debug("Ошибка извлечения даты", e);
        }
        return Optional.ofNullable(date);
    }
}
