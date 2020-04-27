package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Streams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DutyJpaConfig.class)
public class ShiftJpaApplicationTests {

    @Autowired
    DayRepository dayRepository;

    @Test
    public void testDayRepository() {
       LocalDate date = LocalDate.now();
    }


    @Test
    public void testRanges() {
        Range<LocalDateTime> range1 = Range.closed(LocalDate.now().atTime(9, 0), LocalDate.now().atTime(13, 0));
        Range<LocalDateTime> range2 = Range.closed(LocalDate.now().atTime(13, 45), LocalDate.now().atTime(18, 00));
        List<Range<LocalDateTime>> daySchedule = Lists.newArrayList(range1, range2);
        Range<LocalDateTime> range3 = Range.closed(LocalDate.now().atTime(9, 0), LocalDate.now().plusDays(1).atTime(9, 0));
        List<Range<LocalDateTime>> dutySchedule = Lists.newArrayList(range3);
        Streams.concat(daySchedule.stream(), dutySchedule.stream()).forEach(System.out::println);

        for (Range r : daySchedule) {
            if (range3.isConnected(r)) {
                Range intersection = r.intersection(range3);
                System.out.println(intersection);
            }
        }

    }


}
