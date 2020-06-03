package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Shift;

import java.util.List;

public interface ShiftRepository extends BaseRepository<Shift, Integer> {
    @Override
    List<Shift> findAll();

    /*  default Optional<Shift> findByPersonAndDay(Employee employee, Day day) {
        return findById(new DutyPK(employee, day));
    }*/

/*
    @Query("SELECT o FROM Shift o WHERE o.id.idPerson =:person AND o.id.date>=:from AND o.id.date<=:to")
    List<Shift> find(@Param("employee") String person, @Param("from") LocalDate from, @Param("to") LocalDate to);

    default List<Shift> findByPerson(Collection<Employee> employeeList, LocalDate from, LocalDate to) {
        return employeeList.stream().map(Employee::getId).flatMap(p -> find(p, from, to).stream()).collect(Collectors.toList());
    }
*/


  /*  @Transactional
    default Shift _saveWithChilds(Shift shift) {
        return save(shift);
    }

    @Transactional
    default Shift findOrCreate(Employee employee, Day day) {
        Optional<Shift> optionalDuty = findById(new DutyPK(employee, day));
        Shift shift = optionalDuty.orElseGet(() -> Shift.of(employee, day));
        return _save(shift);
    }

    @Transactional
    default Shift _save(Shift shift) {
        shift = _saveWithChilds(shift);
        return save(shift);
    }*/

}
