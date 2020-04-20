package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Period;
import com.bochkov.duty.jpa.entity.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.WeekFields;
import java.util.Locale;

@PlanningEntity
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"day", "dutyType"})
@ToString(of = {"weekend", "day", "person", "dutyType"})
public class DutyAssigment implements Serializable {

    @PlanningVariable(valueRangeProviderRefs = {"persons"})
    Person person;

    Day day;

    DutyType dutyType;

    Boolean weekend;

    Duration overTime;

    public Integer getWeekNumber() {
        return day.getId().get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public boolean isWeekend() {
        if (weekend == null) {
            weekend = dutyType.isStartOnWeekend(day) || dutyType.isEndOnWeekend(day);
        }
        return weekend;
    }

    public Duration getOverTime() {
        if (overTime == null) {
            overTime = calculateOverTime();
        }
        return overTime;
    }

    Duration calculateOverTime() {
        Duration result = Duration.ZERO;
        for (Period dutyPeriod : dutyType.getPeriods()) {
            for (Period dayPeriod : day.getPeriods()) {

            }
        }
        return result;
    }
}
