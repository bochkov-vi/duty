package com.bochkov.duty.jpa.entity;

import com.google.common.collect.Range;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface IPeriodContainer {


    static <C extends Collection<Range<LocalDateTime>>> List<Range<LocalDateTime>> clip(Range<LocalDateTime> range, C ranges) {
        return ranges.stream().filter(r -> range.isConnected(r))
                .map(r -> r.intersection(range))
                .filter(r -> !r.isEmpty()).collect(Collectors.toList());
    }

    static <C extends Collection<Range<LocalDateTime>>> List<Range<LocalDateTime>> clip(LocalDate date, C ranges) {
        return clip(range(date), ranges);
    }

    static Duration duration(Range<LocalDateTime> range) {
        return Duration.between(range.lowerEndpoint(), range.upperEndpoint());
    }

    static Duration diffDurationOfPeriod(LocalDate date, Collection<Period> l1, Collection<Period> l2, LocalDate actualClipingDate) {
        return diffDurationOfPeriod(date, l1, date, l2, actualClipingDate);
    }

    static Duration diffDurationOfPeriod(LocalDate d1, Collection<Period> l1, LocalDate d2, Collection<Period> l2, LocalDate actualClipingDate) {
        List<Range<LocalDateTime>> ranges1 = clip(actualClipingDate, toRangeList(d1, l1));
        List<Range<LocalDateTime>> ranges2 = clip(actualClipingDate, toRangeList(d2, l2));
        Duration result = diffDuration(ranges1, ranges2);
        return result;
    }

    static Duration intersectDurationOfPeriod(LocalDate date, Collection<Period> l1, Collection<Period> l2) {
        return intersectDurationOfPeriod(date, l1, date, l2);
    }

    static Duration intersectDurationOfPeriod(LocalDate d1, Collection<Period> l1, LocalDate d2, Collection<Period> l2) {
        List<Range<LocalDateTime>> ranges1 = toRangeList(d1, l1);
        List<Range<LocalDateTime>> ranges2 = toRangeList(d2, l2);
        Duration result = intersectDuration(ranges1, ranges2);
        return result;
    }

    static List<Range<LocalDateTime>> toRangeList(LocalDate date, Collection<Period> periods) {

        List<Range<LocalDateTime>> result = periods.stream().map(period -> range(date, period)).collect(Collectors.toList());
        return result;
    }

    static Duration diffDuration(Collection<Range<LocalDateTime>> l1, Collection<Range<LocalDateTime>> l2) {
        Duration result = duration(l1);
        for (Range<LocalDateTime> r1 : l1) {
            for (Range<LocalDateTime> r2 : l2) {
                if (r1.isConnected(r2)) {
                    Range<LocalDateTime> i = r1.intersection(r2);
                    result = result.minus(duration(i));
                }
            }
        }
        return result;
    }

    static Duration intersectDuration(Collection<Range<LocalDateTime>> l1, Collection<Range<LocalDateTime>> l2) {
        Duration result = Duration.ZERO;
        for (Range<LocalDateTime> r1 : l1) {
            for (Range<LocalDateTime> r2 : l2) {
                if (r1.isConnected(r2)) {
                    Range<LocalDateTime> i = r1.intersection(r2);
                    result = result.plus(duration(i));
                }
            }
        }
        return result;
    }

    static Duration duration(Collection<Range<LocalDateTime>> c) {
        Duration result = Duration.ofNanos(c.stream().map(range -> duration(range)).mapToLong(Duration::toNanos).sum());
        return result;
    }

    static Range<LocalDateTime> range(LocalDate date, Period period) {
        LocalDateTime d1 = date.atTime(period.getStart());
        LocalDateTime d2 = d1.plus(period.getDuration());
        return Range.closed(d1, d2);
    }

    static Range<LocalDateTime> range(LocalDate date) {
        return Range.closed(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    default Duration diffDuration(LocalDate date, LocalDate otherDate, IPeriodContainer other, LocalDate actualClipingDate) {
        return diffDurationOfPeriod(date, getPeriods(), otherDate, other.getPeriods(), actualClipingDate);
    }

    default Duration diffDuration(LocalDate date, IPeriodContainer other, LocalDate actualClipingDate) {
        return this.diffDuration(date, date, other, actualClipingDate);
    }

    <C extends Collection<Period>> C getPeriods();
}
