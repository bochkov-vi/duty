package com.bochkov.duty.wicket.service.converter;

import com.bochkov.duty.jpa.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter extends AbstractWicketJpaConverter<Employee, String> {

    public EmployeeConverter() {
        super(Employee.class, String.class);
    }
}
