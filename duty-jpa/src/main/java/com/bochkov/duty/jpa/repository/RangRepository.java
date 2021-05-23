package com.bochkov.duty.jpa.repository;


import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.findbylike.FindByLike;
import com.bochkov.findbylike.FindByLikeRepository;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RangRepository extends BaseRepository<Rang, Short>, FindByLikeRepository<Rang> {
    @Query("SELECT o FROM Rang o WHERE LOWER(o.name) LIKE LOWER(:search)")
    List<Rang> findByName(String search);

    @Query("SELECT o FROM Rang o WHERE LOWER(o.fullName) LIKE LOWER(:search)")
    List<Rang> findByFullName(String search);

    default Page<Rang> findByLike(String mask, Pageable pageable, String... properties) {
        Sort sort = Strings.isNullOrEmpty(mask) ? Sort.by("id") : Sort.unsorted();
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),sort);
        return this.findAll(FindByLike.maskSpecification(mask, Lists.newArrayList(properties)), pageRequest);
    }

    default Page<Rang> findByLike(String mask, Pageable pageable, Iterable<String> properties) {
        Sort sort = Strings.isNullOrEmpty(mask) ? Sort.by("id") : Sort.unsorted();
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),sort);
        return this.findAll(FindByLike.maskSpecification(mask, properties), pageRequest);
    }

}
