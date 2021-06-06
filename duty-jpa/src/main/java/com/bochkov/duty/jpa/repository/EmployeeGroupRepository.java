package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.EmployeeGroup;
import com.bochkov.findbylike.FindByLikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeGroupRepository extends BaseRepository<EmployeeGroup, Integer>, FindByLikeRepository<EmployeeGroup> {
    @Query("SELECT o FROM EmployeeGroup o WHERE LOWER(o.name) LIKE LOWER(:search)")
    List<EmployeeGroup> findByName(String search);

    default Page<EmployeeGroup> findByLike(String mask, Pageable pageable) {
        return FindByLikeRepository.super.findByLike(mask, pageable, "name", "id");
    }
}
