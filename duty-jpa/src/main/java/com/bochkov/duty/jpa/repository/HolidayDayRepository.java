package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.HolidayDay;

import java.time.LocalDate;

public interface HolidayDayRepository extends BaseRepository<HolidayDay, LocalDate> {
}
