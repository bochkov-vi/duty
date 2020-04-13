package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Duty;
import com.bochkov.duty.jpa.entity.DutyPK;
import com.bochkov.duty.jpa.entity.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface DutyRepository extends BaseRepository<Duty, DutyPK> {

    default Optional<Duty> findByPersonAndDay(Person person, Day day) {
        return findById(new DutyPK(person, day));
    }

    @Query("SELECT o FROM Duty o WHERE o.id.idPerson =:person AND o.id.date>=:from AND o.id.date<=:to")
    List<Duty> find(@Param("person") String person, @Param("from") LocalDate from, @Param("to") LocalDate to);

    default List<Duty> findByPerson(Collection<Person> personList, LocalDate from, LocalDate to) {
        return personList.stream().map(Person::getId).flatMap(p -> find(p, from, to).stream()).collect(Collectors.toList());
    }


    @Transactional
    default Duty _saveWithChilds(Duty duty) {
        DutyPK prevPK = new DutyPK(duty.getId().getIdPerson(), duty.getId().getDate().minusDays(1));
        Duty prev = findById(prevPK).orElseGet(() -> {
            Duty d = new Duty().setId(prevPK).setDay(duty.getDay().getPrev()).setPerson(duty.getPerson());
            return save(d);
        });
        DutyPK nextPK = new DutyPK(duty.getId().getIdPerson(), duty.getId().getDate().plusDays(1));
        Duty next = findById(nextPK).orElseGet(() -> {
            Duty d = new Duty().setId(nextPK).setDay(duty.getDay().getNext()).setPerson(duty.getPerson());
            return save(d);
        });
        duty.setNext(next);
        next.setPrev(duty);
        duty.setPrev(prev);
        prev.setNext(duty);
        return save(duty);
    }

    @Transactional
    default Duty findOrCreate(Person person, Day day) {
        Optional<Duty> optionalDuty = findById(new DutyPK(person, day));
        Duty duty = optionalDuty.orElseGet(() -> Duty.of(person, day));
        return _save(duty);
    }

    @Transactional
    default Duty _save(Duty duty) {
        duty = _saveWithChilds(duty);
        return save(duty);
    }

}
