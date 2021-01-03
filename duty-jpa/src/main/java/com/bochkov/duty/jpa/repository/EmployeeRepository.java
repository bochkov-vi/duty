package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.ShiftType;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends BaseRepository<Employee, Integer> {

    List<Employee> findByShiftTypesContaining(ShiftType shiftType);

    List<Employee> findByShiftTypesIn(Iterable<ShiftType> shiftType);

    Optional<Employee> findByLogin(String login);
}
