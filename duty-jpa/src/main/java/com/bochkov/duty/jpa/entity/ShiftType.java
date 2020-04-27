package com.bochkov.duty.jpa.entity;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "SHIFT_TYPE")
@Entity
@NoArgsConstructor
public class ShiftType extends AbstractEntity<Integer> implements IPeriodContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHIFT_TYPE_SEQ")
    @SequenceGenerator(name = "SHIFT_TYPE_SEQ", initialValue = 1000, allocationSize = 1)
    @Column(name = "ID_SHIFT_TYPE")
    Integer id;

    @Column(name = "SHIFT_TYPE")
    String name;

    @Embedded
    UiOptions uiOptions;

    @ElementCollection
    @CollectionTable(name = "SHIFT_TYPE_DAYS_TO_WEEKEND", foreignKey = @ForeignKey(name = "DAYS_TO_WEEKEND_SHIFT_TYPE_FK"),
            joinColumns = @JoinColumn(name = "ID_SHIFT_TYPE", referencedColumnName = "ID_SHIFT_TYPE", foreignKey = @ForeignKey(name = "DAYS_TO_WEEKEND_SHIFT_TYPE_FK")))
    @Column(name = "DAYS_TO_WEEKEND")
    List<Integer> daysToWeekend;

    @ElementCollection
    @CollectionTable(name = "SHIFT_TYPE_DAYS_FROM_WEEKEND", foreignKey = @ForeignKey(name = "DAYS_FROM_WEEKEND_SHIFT_TYPE_FK"),
            joinColumns = @JoinColumn(name = "ID_SHIFT_TYPE", referencedColumnName = "ID_SHIFT_TYPE"))
    @Column(name = "DAYS_FROM_WEEKEND")
    List<Integer> daysFromWeekend;

    @ElementCollection
    @CollectionTable(name = "SHIFT_TYPE_PERIOD", joinColumns = {
            @JoinColumn(name = "ID_SHIFT_TYPE", referencedColumnName = "ID_SHIFT_TYPE", foreignKey = @ForeignKey(name = "SHIFT_TYPE_PERIOD_FQ"))
    })
    Set<Period> periods;

    public ShiftType(String name) {
        this.name = name;
    }

    public ShiftType(String name, int hStart, int mStart, int hDuration, int mDuration) {
        this.name = name;
        Period period = Period.of(hStart, mStart, hDuration, mDuration);
        setPeriods(Sets.newHashSet(period));
    }

    public Period lastPeriod() {
        if (periods != null) {
            return Collections.max(periods);
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public boolean isStartOnWeekend(Day day) {
        return day.isWeekend();
    }

    public boolean isEndOnWeekend(Day day) {
        boolean result = false;
        if (day.getNext() != null && day.getNext().isWeekend()) {
            result = isEndOnNextDay(day);
        }
        return result;
    }

    public boolean isEndOnNextDay(Day day) {
        boolean result = false;
        if (day.getNext() != null) {
            Period last = lastPeriod();
            if (last != null) {
                result = last.range(day.getId()).isConnected(day.getNext().range());
            }
        }
        return result;
    }

    public OvertimeData overtime(Day day, Employee employee) {
        OvertimeData result = overtime(day);
        if (isStartOnWeekend(day)) {
            Optional.ofNullable(employee.getRoadToHomeTime()).ifPresent(rt ->
                    result.setWeekendTime(result.getWeekendTime().plus(rt)));
        }
        if (isEndOnWeekend(day)) {
            Optional.ofNullable(employee.getRoadToHomeTime()).ifPresent(rt ->
                    result.setWeekendTime(result.getWeekendTime().plus(rt)));
        }
        return result;
    }

    public OvertimeData overtime(Day day) {
        Duration overTime = Duration.ZERO;
        Duration weekendTime = Duration.ZERO;
        Duration restTime = Duration.ZERO;
        Duration overTime1 = this.diffDuration(day.getId(), day.getShiftType(), day.getId());

        if (day.isWeekend()) {
            weekendTime = weekendTime.plus(overTime1);
        } else {
            overTime = overTime.plus(overTime1);
        }

        Duration overTime2 = this.diffDuration(day.getId(), day.getNext().getId(), day.getNext().getShiftType(), day.getNext().getId());
        if (day.getNext().isWeekend()) {
            weekendTime = weekendTime.plus(overTime2);
        } else {
            overTime = overTime.plus(overTime2);
        }

        Duration resTime1 = day.getShiftType().diffDuration(day.getId(), this, day.getId());
        restTime = restTime.plus(resTime1);


        Duration resTime2 = day.getNext().getShiftType().diffDuration(day.getNext().getId(), day.getId(), this, day.getNext().getId());
        restTime = restTime.plus(resTime2);

        OvertimeData data = new OvertimeData(overTime, weekendTime, restTime);
        return data;
    }

    public Duration totalOvertime(Day day, Employee employee) {
        return overtime(day, employee).getTotal();
    }

    public Duration totalOvertime(Day day) {
        return overtime(day).getTotal();
    }
}
