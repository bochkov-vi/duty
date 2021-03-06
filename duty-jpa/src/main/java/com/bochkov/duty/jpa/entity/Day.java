package com.bochkov.duty.jpa.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "DAY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Day extends AbstractEntity<LocalDate> implements Comparable<Day>, IPeriodContainer {

    final static Comparator<Day> COMPARATOR = Comparator.nullsLast(Comparator.comparing(Day::getId));

    @Id
    @Column(name = "DATE")
    protected LocalDate id;

    @Column(name = "WEEKEND")
    protected boolean weekend = false;

    @OneToOne
    @JoinColumn(name = "NEXT", referencedColumnName = "DATE", foreignKey = @ForeignKey(name = "NEXT_DAY_FK"))
    @Fetch(FetchMode.JOIN)
    protected Day next;

    @OneToOne(mappedBy = "next")
    protected Day prev;

    @Column(name = "DAYS_TO_WEEKEND")
    protected Integer daysToWeekend;

    @Column(name = "DAYS_FROM_WEEKEND")
    protected Integer daysFromWeekend;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "DAY_PERIOD", joinColumns = @JoinColumn(name = "DATE", referencedColumnName = "DATE"), foreignKey = @ForeignKey(name = "DAY_TIME_USAGE_FK", foreignKeyDefinition = "foreign key (DATE) references DAY (DATE)"))
    protected Set<Period> periods;

    @ManyToOne
    @JoinColumn(name = "ID_SHIFT_TYPE_DEFAULT", referencedColumnName = "ID_SHIFT_TYPE")
    ShiftType shiftType;

    @Transient
    Integer weekIndex;

    @Transient
    Long dayIndex;

    @Transient
    DayOfWeek dayOfWeek;

    @OneToOne
    @JoinColumn(name = "DATE", referencedColumnName = "DATE")
    @Fetch(FetchMode.JOIN)
    HolidayDay holiday;


    public Day(LocalDate date) {
        this.id = date;
        weekend = isWeekendDate(date);
    }

    static public boolean isWeekendDate(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public static Day setupDutyTypeTimeUsage(Day day, ShiftType shiftType) {
        day.setPeriods(calculateDutyTypeTimeUsage(day, shiftType));
        if (day.isShortened()) {
            Period last = day.lastPeriod();
            if (last != null) {
                last.setDuration(last.getDuration().minus(Duration.ofHours(1)));
                System.out.println(last);
            }
        }
        return day;
    }

    public static Set<Period> calculateDutyTypeTimeUsage(Day day, ShiftType shiftType) {
        Set<Period> periods = Sets.newHashSet();
        if (shiftType != null) {
            Range<LocalDateTime> dayRange = day.range();
            for (Period p : shiftType.getPeriods()) {
                assert day.getId() != null;
                Range<LocalDateTime> range = p.range(day.getId());
                if (dayRange.isConnected(range)) {
                    Range<LocalDateTime> intersection = dayRange.intersection(range);
                    Period period = Period.of(intersection);
                    periods.add(period);
                }
            }
        }
        return periods;
    }

    public boolean isShortened() {
        return Optional.ofNullable(holiday).map(HolidayDay::isShortened).orElse(false);
    }

    public boolean isHolidayWeekend() {
        return Optional.ofNullable(holiday).map(HolidayDay::isWeekend).orElse(false);
    }

    public boolean isHolidayWorkday() {
        return Optional.ofNullable(holiday).map(HolidayDay::isWeekend).orElse(false);
    }

    @PostLoad
    @PostUpdate
    @PostPersist
    public void calculateTransient() {
        weekIndex = weekIndex();
        dayIndex = dayIndex();
        dayOfWeek = dayOfWeek();
    }

    @PrePersist
    public void prePersist() {
        if (isHolidayWeekend()) {
            weekend = true;
        } else if (daysFromWeekend != null && daysFromWeekend == 0) {
            weekend = true;
        } else if (daysToWeekend != null && daysToWeekend == 0) {
            weekend = true;
        } else {
            weekend = isWeekendDate(id);
        }
    }


    @PreUpdate
    public void preUpdate() {
        if (isHolidayWeekend()) {
            weekend = true;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EEE")))
                .add("weekend", weekend)
                .add("shortened", isShortened())
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

    public int daysTo(Day day) {
        return (int) ChronoUnit.DAYS.between(this.id, day.id);
    }

    public Range<LocalDateTime> range() {
        return Period.of().range(id);
    }

    private DayOfWeek dayOfWeek() {
        return id.getDayOfWeek();
    }

    private Integer weekIndex() {
        return id.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    private Long dayIndex() {
        return id.toEpochDay();
    }
}
