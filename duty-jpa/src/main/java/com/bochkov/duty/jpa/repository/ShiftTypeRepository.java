package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.findbylike.FindByLikeRepository;

public interface ShiftTypeRepository extends BaseRepository<ShiftType, Integer> , FindByLikeRepository<ShiftType> {
}
