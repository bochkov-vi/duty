package com.bochkov.duty.xmlcalendar.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.List;

@Data
@XmlRootElement(name = "calendar")
public class XmlCalendar {

    //    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlAttribute
    @JsonFormat(pattern = "yyyy.MM.dd")
    public LocalDate date;

    @XmlAttribute
    Integer year;

    @XmlAttribute
    String lang;
    @JsonManagedReference
    List<XmlHoliday> holidays;

    @JsonManagedReference
    List<XmlDay> days;

    /*static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
        @Override
        public LocalDate unmarshal(String value) throws Exception {
            return Optional.ofNullable(value).map(s -> LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .orElse(null);
        }

        @Override
        public String marshal(LocalDate date) throws Exception {
            return Optional.ofNullable(date).map(d -> d.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .orElse(null);
        }
    }*/
}
