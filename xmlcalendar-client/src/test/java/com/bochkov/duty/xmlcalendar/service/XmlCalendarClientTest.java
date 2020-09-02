package com.bochkov.duty.xmlcalendar.service;

import com.bochkov.duty.xmlcalendar.XmlCalendarConfig;
import com.bochkov.duty.xmlcalendar.entity.XmlCalendar;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = XmlCalendarConfig.class)
@RunWith(SpringRunner.class)
@Slf4j
public class XmlCalendarClientTest {
    @Autowired
    XmlCalendarClient client;

    @Test
    public void testDownload() {
        XmlCalendar calendar = client.download(2020);
        log.debug("Загружен {}!!!", calendar);
    }
}
