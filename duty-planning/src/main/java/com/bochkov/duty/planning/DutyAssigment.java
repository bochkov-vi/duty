package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.temporal.WeekFields;
import java.util.Locale;

@PlanningEntity
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"weekend", "day", "person", "dutyType"})
@NoArgsConstructor
public class DutyAssigment implements Serializable {

    int dayIndex;

    @PlanningVariable(valueRangeProviderRefs = {"persons"})
    private Person person;

    private Day day;

    private DutyType dutyType;

    private boolean weekend;

    private int weekIndex;

    private Integer id;

    private DayOfWeek dayOfWeek;

    private Duration overTime;

    private boolean endOnNextDay;

    public static DutyAssigment of(Day day, DutyType dutyType) {
        DutyAssigment result = new DutyAssigment();
        result.setDay(day);
        result.setWeekIndex(day.getId().get(WeekFields.of(Locale.getDefault()).weekOfYear()));
        result.setDutyType(dutyType);
        result.setWeekend(dutyType.isStartOnWeekend(day) || dutyType.isEndOnWeekend(day));
        result.setOverTime(dutyType.totalOvertime(day));
        result.setDayIndex((int) day.getId().toEpochDay());
        result.dayOfWeek = day.getDayOfWeek();
        result.endOnNextDay = dutyType.isEndOnNextDay(day);
        return result;
    }

}
