package com.bochkov.duty.wicket.service.converter;

import com.bochkov.duty.jpa.entity.Rang;
import org.springframework.stereotype.Component;

@Component
public class RangConverter extends AbstractWicketJpaConverter<Rang, Short> {

    public RangConverter() {
        super(Rang.class, Short.class);
    }
}
