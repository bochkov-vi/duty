package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.Duration;

@PlanningEntity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "SHIFT_ASSIGNMENT")
@Entity
@NoArgsConstructor
@ToString(of = {"id", "weekend", "day", "employee", "shift"})
public class ShiftAssignment extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHIFT_ASSIGNMENT_SEQ")
    @SequenceGenerator(name = "SHIFT_ASSIGNMENT_SEQ", initialValue = 1000, allocationSize = 1)
    @Column(name = "ID_SHIFT_ASSIGNMENT")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_SHIFT", referencedColumnName = "ID_SHIFT")
    Shift shift;

    @PlanningVariable(valueRangeProviderRefs = {"employees"})
    @ManyToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    Employee employee;

    @Transient
    Integer weekIndex;

    @Transient
    Boolean weekend;

    @Transient
    Duration overTime;

    @Transient
    int dayIndex = -1;

    @Transient
    DayOfWeek dayOfWeek;

    @Transient
    Boolean endOnNextDay;

    public static ShiftAssignment of(Shift s) {
        return new ShiftAssignment().setShift(s);
    }

    public ShiftType getShiftType() {
        return shift.getShiftType();
    }

    public Day getDay() {
        return shift.getDay();
    }

    public Integer getWeekIndex() {
        if (weekIndex == null) {
            return weekIndex = getDay().weekIndex();
        }
        return weekIndex;
    }

    public boolean isWeekend() {
        if (weekend == null) {
            return weekend = getShiftType().isStartOnWeekend(getDay()) || getShiftType().isEndOnWeekend(getDay());
        }
        return weekend;
    }

    public Duration getOverTime() {
        if (overTime == null) {
            return overTime = getShiftType().totalOvertime(getDay());
        }
        return overTime;
    }

    public int getDayIndex() {
        if (dayIndex < 0) {
            dayIndex = (int) getDay().dayIndex();
        }
        return dayIndex;
    }

    public DayOfWeek getDayOfWeek() {
        if (dayOfWeek == null) {
            return dayOfWeek = getDay().dayOfWeek();
        }
        return dayOfWeek;
    }

    public boolean isEndOnNextDay() {
        if (endOnNextDay == null) {
            endOnNextDay = getShiftType().isEndOnNextDay(getDay());
        }
        return endOnNextDay;
    }

    public Boolean getWeekend() {
        return isWeekend();
    }
}
