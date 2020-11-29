package com.bochkov.duty.wicket.service.converter;

import com.bochkov.duty.jpa.entity.ShiftPK;
import org.springframework.stereotype.Component;

@Component
public class ShiftPkConverter extends CompositeConverter<ShiftPK> {

    public ShiftPkConverter() {
        super(ShiftPK.class);
    }
}
