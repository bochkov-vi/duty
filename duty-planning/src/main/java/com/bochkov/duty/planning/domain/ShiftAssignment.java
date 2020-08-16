package com.bochkov.duty.planning.domain;

import com.bochkov.duty.jpa.entity.AbstractEntity;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Shift;
import com.bochkov.duty.jpa.entity.ShiftType;
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
@NoArgsConstructor
@ToString(of = {"id", "weekend", "day", "employee", "shift"})
public class ShiftAssignment {

    Integer id;

    Shift shift;

    @PlanningVariable(valueRangeProviderRefs = {"employees"})
    Employee employee;

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
        return getDay().getWeekIndex();
    }

    public boolean isWeekend() {
        return getShift().getWeekend();
    }

    public Duration getOverTime() {
        return getShift().getOverTime();
    }

    public Long getDayIndex() {
        return getDay().getDayIndex();
    }

    public DayOfWeek getDayOfWeek() {
        return getDay().getDayOfWeek();
    }

    public Boolean isEndOnNextDay() {
        return getShift().getEndOnNextDay();
    }

    public Boolean getWeekend() {
        return isWeekend();
    }
}
