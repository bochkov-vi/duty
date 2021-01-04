package com.bochkov.duty.wicket.service.converter;

import com.bochkov.duty.jpa.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter extends AbstractWicketJpaConverter<Employee, Integer> {

    public EmployeeConverter() {
        super(Employee.class, Integer.class);
    }
}
