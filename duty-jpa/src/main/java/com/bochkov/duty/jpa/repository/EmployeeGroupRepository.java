package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.EmployeeGroup;
import com.bochkov.duty.jpa.entity.Rang;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeGroupRepository extends BaseRepository<EmployeeGroup, Integer> {
    @Query("SELECT o FROM EmployeeGroup o WHERE LOWER(o.name) LIKE LOWER(:search)")
    List<EmployeeGroup> findByName(String search);
}
