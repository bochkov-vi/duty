package com.bochkov.duty.wicket.service.converter;

import com.bochkov.duty.jpa.entity.*;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.util.convert.IConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SpringWicketConverterLocator extends ConverterLocator {

    @Autowired
    ApplicationContext context;

    @Autowired
    IConverter<ShiftType> shiftTypeConverter;

    @Autowired
    IConverter<Rang> rangConverter;

    @Autowired
    ShiftPkConverter shiftPkConverter;

    @Autowired
    IConverter<EmployeeGroup> employeeGroupConverter;

    @Autowired
    IConverter<Employee> employeeConverter;

    @Autowired
    IConverter<Day> dayConverter;

    public SpringWicketConverterLocator() {
        super();
    }

    @PostConstruct
    public void postConstruct() {
        set(ShiftPK.class, shiftPkConverter);
        set(ShiftType.class, shiftTypeConverter);
        set(Rang.class, rangConverter);
        set(EmployeeGroup.class, employeeGroupConverter);
        set(Employee.class, employeeConverter);
        set(Day.class, dayConverter);
    }


}
