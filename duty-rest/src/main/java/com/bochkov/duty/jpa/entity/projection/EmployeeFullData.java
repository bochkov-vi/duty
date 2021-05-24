package com.bochkov.duty.jpa.entity.projection;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.EmployeeGroup;
import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.entity.ShiftType;
import org.springframework.data.rest.core.config.Projection;

import java.time.Duration;
import java.util.Set;

@Projection(name = "full-data", types = {Employee.class})
public interface EmployeeFullData {

    Integer getId();

    String getLogin();

    Rang getRang();

    String getPost();

    String getFirstName();

    String getMiddleName();

    String getLastName();

    EmployeeGroup getEmployeeGroup();

    Set<ShiftType> getShiftTypes();

    Duration getRoadToHomeTime();
}
