package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Vacation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationRepository extends BaseRepository<Vacation, Long> {

    @Query("SELECT o FROM Vacation  o WHERE o.end>=:start AND o.start<=:end AND o.employee=:employee")
    List<Vacation> findAllByPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("employee") Employee employee);
}
