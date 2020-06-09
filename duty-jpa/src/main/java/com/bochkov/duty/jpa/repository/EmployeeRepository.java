package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.ShiftType;

import java.util.List;

public interface EmployeeRepository extends BaseRepository<Employee, String> {

    List<Employee> findByShiftTypesContaining(ShiftType shiftType);


}
