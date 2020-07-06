package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.ShiftAssignment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShiftAssignmentRepository extends BaseRepository<ShiftAssignment, Integer> {
    @Query("SELECT o FROM ShiftAssignment  o JOIN o.shift s JOIN s.day d WHERE o.employee=:employee AND d.id=:date")
    List<ShiftAssignment> findAll(@Param("date") LocalDate date,@Param("employee") Employee employee);
}
