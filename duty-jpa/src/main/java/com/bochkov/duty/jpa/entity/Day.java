package com.bochkov.duty.jpa.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "day")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Day extends AbstractEntity<LocalDate> implements Comparable<Day> {

    final static Comparator<Day> COMPARATOR = Comparator.nullsLast(Comparator.comparing(Day::getId));

    @Id
    @Column(name = "date")
    protected LocalDate id;

    @Column(name = "weekend")
    protected boolean weekend = false;

    @OneToOne
    @JoinColumn(name = "next", referencedColumnName = "date", foreignKey = @ForeignKey(name = "NEXT_DAY_FK"))
    protected Day next;

    @OneToOne(mappedBy = "next")
    protected Day prev;

    @Column(name = "days_to_weekend")
    protected Integer daysToWeekend;

    @Column(name = "days_from_weekend")
    protected Integer daysFromWeekend;

    @ElementCollection
    @CollectionTable(name = "day_period", joinColumns = @JoinColumn(name = "date", referencedColumnName = "date"), foreignKey = @ForeignKey(name = "day_time_usage_fk", foreignKeyDefinition = "foreign key (DATE) references DAY (DATE)"))
    protected Set<Period> periods;

    public Day(LocalDate date) {
        this.id = date;
        weekend = isWeekendDate(date);
    }

    static public boolean isWeekendDate(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
    /*public Integer daysToWeekend() {
        Integer result = null;
        if (next != null) {
            if (next.getWeekend() != null && next.getWeekend()) {
                result = 1;
            } else {
                result = next.daysToWeekend() + 1;
            }
        }
        return result;
    }*/

    public static Day setupDutyTypeTimeUsage(Day day, DutyType dutyType) {
        day.setPeriods(calculateDutyTypeTimeUsage(day, dutyType));
        return day;
    }

    public static Set<Period> calculateDutyTypeTimeUsage(Day day, DutyType dutyType) {
        Set<Period> periods = Sets.newHashSet();
        if (dutyType != null) {
            Range<LocalDateTime> dayRange = day.range();
            for (Period p : dutyType.getPeriods()) {
                Range range = p.range(day.getId());
                if (dayRange.isConnected(range)) {
                    Range<LocalDateTime> intersection = dayRange.intersection(range);
                    Period period = Period.of(intersection);
                    periods.add(period);
                }
            }
        }
        return periods;
    }


    @PrePersist
    public void prePersist() {
        if (daysFromWeekend != null && daysFromWeekend == 0) {
            weekend = true;
        } else if (daysToWeekend != null && daysToWeekend == 0) {
            weekend = true;
        } else {
            weekend = isWeekendDate(id);
        }
    }

    @Override

    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EEE")))
                .add("weekend", weekend)
                .toString();
    }

    @Override
    public int compareTo(Day o) {
        return COMPARATOR.compare(this, o);
    }

    public boolean isAfter(Day day) {
        return id.isAfter(Objects.requireNonNull(day.getId()));
    }

    public boolean isBefore(Day day) {
        return id.isBefore(Objects.requireNonNull(day.getId()));
    }

    public long daysTo(Day day) {
        return ChronoUnit.DAYS.between(this.id, day.id);
    }

    public Range<LocalDateTime> range() {
        return Period.of().range(id);
    }


}
