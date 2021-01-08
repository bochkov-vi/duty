package com.bochkov.duty.jpa.entity;

import com.bochkov.duty.jpa.entity.converter.DurationConverter;
import com.google.common.collect.Range;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Period implements Serializable, Comparable<Period> {

    @Column(name = "START", nullable = false)
    LocalTime start;

    @Convert(converter = DurationConverter.class)
    @Column(name = "DURATION")
    Duration duration;

    public Period(int h, int m, int minutes) {
        start = LocalTime.of(h, m);
        duration = Duration.ofMinutes(minutes);
    }

    public Period(int h1, int m1, int h2, int m2) {
        start = LocalTime.of(h1, m2);
        Duration d = Duration.ZERO.plusHours(h2).plusMinutes(m2).minus(Duration.ZERO.plusHours(h1).plusMinutes(m1));
        duration = d;
    }

    public static Period of() {
        return new Period(0, 0, 24, 0);
    }

    public static Period of(Range<LocalDateTime> range) {
        Period result = new Period();
        result.setStart(range.lowerEndpoint().toLocalTime());
        Duration duration = Duration.between(range.lowerEndpoint(), range.upperEndpoint());
        result.setDuration(duration);
        return result;
    }

    public static Period of(int hStart, int mStart, int hDuration, int mDuration) {
        Period result = new Period();
        result.start = LocalTime.of(hStart, mStart);
        result.duration = Duration.ZERO.plusHours(hDuration).plusMinutes(mDuration);
        return result;
    }

    public static Duration totalDuration(Collection<Period> periods) {
        Duration result = Duration.ZERO;
        if (periods != null) {
            for (Period period : periods) {
                result = result.plus(period.getDuration());
            }
        }
        return result;
    }

    public static boolean isIntersects(Period p1, Period p2) {
        return p1 != null && p2 != null && p1.isIntersects(p2);
    }

    public Range<LocalDateTime> range(LocalDate date) {
        LocalDateTime d1 = date.atTime(start);
        LocalDateTime d2 = d1.plus(duration);
        return Range.closed(d1, d2);
    }

    @Override
    public Period clone() {
        return new Period(start, duration);
    }

    @Override
    public int compareTo(Period period) {
        return start.compareTo(period.start);
    }

    public boolean isIntersects(Period p, LocalDate date) {
        return range(date).isConnected(p.range(date));
    }

    public boolean isIntersects(Period p) {
        return isIntersects(p, LocalDate.now());
    }
}
