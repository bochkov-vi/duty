package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Rang;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RangRepository extends BaseRepository<Rang, Short> {
    @Query("SELECT o FROM Rang o WHERE LOWER(o.name) LIKE LOWER(:search)")
    List<Rang> findByName(String search);
    @Query("SELECT o FROM Rang o WHERE LOWER(o.fullName) LIKE LOWER(:search)")
    List<Rang> findByFullName(String search);
}
