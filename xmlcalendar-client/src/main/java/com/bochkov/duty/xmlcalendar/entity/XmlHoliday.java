package com.bochkov.duty.xmlcalendar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@ToString(of = {"id", "title"})
@XmlRootElement(name = "holiday")
public class XmlHoliday {
    @EqualsAndHashCode.Include
    @XmlAttribute
    Integer id;

    @XmlAttribute
    String title;

    @JsonBackReference
    XmlCalendar calendar;

    public List<XmlDay> getDays() {
        List<XmlDay> result;
        result = calendar.days.stream().filter(d -> Objects.equals(d.h, this.id)).collect(Collectors.toList());
        return result;
    }

}
