package com.bochkov.duty.wicket.service.converter;

import com.bochkov.duty.jpa.entity.ShiftType;
import org.springframework.stereotype.Component;

@Component
public class ShiftTypeConverter extends AbstractWicketJpaConverter<ShiftType, Integer> {

    public ShiftTypeConverter() {
        super(ShiftType.class, Integer.class);
    }
}
