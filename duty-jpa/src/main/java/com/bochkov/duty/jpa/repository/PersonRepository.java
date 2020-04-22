package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Person;

import java.util.List;

public interface PersonRepository extends BaseRepository<Person, String> {
    List<Person> findByDutyTypesContaining(DutyType dutyType);
}
