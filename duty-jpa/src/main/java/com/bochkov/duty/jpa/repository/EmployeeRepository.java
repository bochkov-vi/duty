package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.findbylike.FindByLikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends BaseRepository<Employee, Integer>, FindByLikeRepository<Employee> {

    List<Employee> findByShiftTypesContaining(ShiftType shiftType);

    List<Employee> findByShiftTypesIn(Iterable<ShiftType> shiftType);

    Optional<Employee> findByLogin(String login);

    @Query("SELECT o FROM Employee o WHERE o.firstName=:firstName AND o.middleName=:middleName AND o.lastName=:lastName")
    Page<Employee> findByFIO(Pageable pageable, String firstName, String middleName, String lastName);
}
