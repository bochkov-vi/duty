package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Period;
import com.bochkov.duty.jpa.entity.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.io.Serializable;
import java.time.temporal.WeekFields;
import java.util.Locale;

@PlanningEntity
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"day", "dutyType"})
public class DutyAssigment implements Serializable {

    @PlanningVariable(valueRangeProviderRefs = {"persons"})
    Person person;

    Day day;

    DutyType dutyType;

    Boolean weekend;

    public Integer getWeekNumber() {
        return day.getId().get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public boolean isWeekend() {
        if (weekend == null) {
            weekend = day.isWeekend();
            if (!weekend && day.getNext() != null && day.getNext().isWeekend()) {
                Period last = dutyType.lastPeriod();
                if (last != null) {
                    weekend = last.range(day.getId()).isConnected(day.getNext().range());
                }
            }
        }
        return weekend;
    }

}
