package com.bochkov.duty.xmlcalendar.service;

import com.bochkov.duty.xmlcalendar.entity.XmlCalendar;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class XmlCalendarClientImpl implements XmlCalendarClient {

    @Override
    public XmlCalendar download(Integer year) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<XmlCalendar> xmlCalendar = restTemplate.getForEntity(url(year), XmlCalendar.class);
        return xmlCalendar.getBody();
    }
    String url(Integer year){
        return String.format("http://xmlcalendar.ru/data/ru/%s/calendar.xml",year);
    }
}
