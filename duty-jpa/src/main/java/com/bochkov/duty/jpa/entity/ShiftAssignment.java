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

    /*@Transient
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
    Boolean endOnNextDay;*/

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
