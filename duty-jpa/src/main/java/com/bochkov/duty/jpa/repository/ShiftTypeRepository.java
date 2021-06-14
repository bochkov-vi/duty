package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.findbylike.FindByLikeRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShiftTypeRepository extends BaseRepository<ShiftType, Integer> , FindByLikeRepository<ShiftType> {
    @Query("SELECT o FROM ShiftType o WHERE LOWER(o.name) LIKE LOWER(:search)")
    List<ShiftType> findByName(String search);
}
