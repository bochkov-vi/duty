package com.bochkov.duty.wicket.service.converter;

import com.bochkov.duty.jpa.entity.EmployeeGroup;
import org.springframework.stereotype.Component;

@Component
public class EmployeeGroupConverter extends AbstractWicketJpaConverter<EmployeeGroup, Integer> {

    public EmployeeGroupConverter() {
        super(EmployeeGroup.class, Integer.class);
    }
}
