package com.bochkov.duty.wicket.service.converter;

import com.bochkov.duty.jpa.entity.Day;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DayConverter extends AbstractWicketJpaConverter<Day, LocalDate> {

    public DayConverter() {
        super(Day.class, LocalDate.class);
    }
}
