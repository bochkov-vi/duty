package com.bochkov.duty.jpa.service;

import com.bochkov.duty.jpa.entity.HolidayDay;

import java.util.List;

public interface HolidayService {
    List<HolidayDay> downloadAndSave(Integer year);
}
