package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.entity.VacationPK;
import com.bochkov.duty.jpa.entity.VacationPart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationRepository extends BaseRepository<Vacation, VacationPK> {
    @Query("SELECT o FROM Vacation  o JOIN o.parts p WHERE p.end>=:start AND p.start<=:end AND o.employee=:employee")
    List<Vacation> findAllByPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("employee") Employee employee);


    default Boolean existsByPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("employee") Employee employee) {
        return !findAllByPeriod(start, end, employee).isEmpty();
    }

    @Query("SELECT p FROM Vacation  o JOIN o.parts p WHERE p.end>=:start AND p.start<=:end AND o.employee=:employee")
    List<VacationPart> findParts(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("employee") Employee employee);

    default double percentOverlap(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("employee") Employee employee) {
        double result = 0.0;
        List<VacationPart> list = findParts(start, end, employee);
        for (VacationPart p : list) {
            double po = p.percentOverlap(start, end);
            if (po > 0) {
                result = po;
                break;
            }
        }
        return result;
    }
}
