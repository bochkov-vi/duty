package com.bochkov.duty.xmlcalendar.service;

import com.bochkov.duty.xmlcalendar.entity.XmlCalendar;

public interface XmlCalendarClient {
    XmlCalendar download(Integer year);
}
