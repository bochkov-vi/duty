package com.bochkov.duty.jpa.entity.projection;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.entity.VacationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;

@Projection(name = "full-data", types = {Vacation.class})
public interface VacationFullData {

    Employee getEmployee();

    @Value("#{target.employee.toString()}")
    String getEmployeeFio();

    Integer getYear();

    VacationType getType();

    LocalDate getStart();

    LocalDate getEnd();

    String getNote();

    Integer getDayCount();
}
