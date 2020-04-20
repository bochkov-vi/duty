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
@Table(name = "duty_type")
@Entity
@NoArgsConstructor
public class DutyType extends AbstractEntity<Integer> implements IPeriodContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "duty_type_seq")
    @SequenceGenerator(name = "duty_type_seq", initialValue = 1000, allocationSize = 1)
    @Column(name = "id_duty_type")
    Integer id;

    @Column(name = "duty_type")
    String name;

    @Embedded
    UiOptions uiOptions;

    @ElementCollection
    @CollectionTable(name = "DUTY_TYPE_DAYS_TO_WEEKEND", foreignKey = @ForeignKey(name = "DAYS_TO_WEEKEND_DUTY_TYPE_FK"),
            joinColumns = @JoinColumn(name = "ID_DUTY_TYPE", referencedColumnName = "ID_DUTY_TYPE"))
    @Column(name = "DAYS_TO_WEEKEND")
    List<Integer> daysToWeekend;

    @ElementCollection
    @CollectionTable(name = "DUTY_TYPE_DAYS_FROM_WEEKEND", foreignKey = @ForeignKey(name = "DAYS_FROM_WEEKEND_DUTY_TYPE_FK"),
            joinColumns = @JoinColumn(name = "ID_DUTY_TYPE", referencedColumnName = "ID_DUTY_TYPE"))
    @Column(name = "DAYS_FROM_WEEKEND")
    List<Integer> daysFromWeekend;

    @ElementCollection
    @CollectionTable(name = "duty_type_period", joinColumns = {
            @JoinColumn(name = "id_duty_type", referencedColumnName = "id_duty_type")
    }, foreignKey = @ForeignKey(name = "duty_type_period_fq",
            foreignKeyDefinition = "foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE) ON UPDATE CASCADE ON DELETE CASCADE"))
    Set<Period> periods;

    public DutyType(String name) {
        this.name = name;
    }

    public DutyType(String name, int hStart, int mStart, int hDuration, int mDuration) {
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

    public OvertimeData overtime(Day day, Person person) {
        OvertimeData result = overtime(day);
        if (isStartOnWeekend(day)) {
            Optional.ofNullable(person.getRoadToHomeTime()).ifPresent(rt ->
                    result.setWeekendTime(result.getWeekendTime().plus(rt)));
        }
        if (isEndOnWeekend(day)) {
            Optional.ofNullable(person.getRoadToHomeTime()).ifPresent(rt ->
                    result.setWeekendTime(result.getWeekendTime().plus(rt)));
        }
        return result;
    }

    public OvertimeData overtime(Day day) {
        Duration overTime = Duration.ZERO;
        Duration weekendTime = Duration.ZERO;
        Duration restTime = Duration.ZERO;
        Duration overTime1 = this.diffDuration(day.getId(), day.getDutyType(), day.getId());

        if (day.isWeekend()) {
            weekendTime = weekendTime.plus(overTime1);
        } else {
            overTime = overTime.plus(overTime1);
        }

        Duration overTime2 = this.diffDuration(day.getId(), day.getNext().getId(), day.getNext().getDutyType(), day.getNext().getId());
        if (day.getNext().isWeekend()) {
            weekendTime = weekendTime.plus(overTime2);
        } else {
            overTime = overTime.plus(overTime2);
        }

        Duration resTime1 = day.getDutyType().diffDuration(day.getId(), this, day.getId());
        restTime = restTime.plus(resTime1);


        Duration resTime2 = day.getNext().getDutyType().diffDuration(day.getNext().getId(), day.getId(), this, day.getNext().getId());
        restTime = restTime.plus(resTime2);

        OvertimeData data = new OvertimeData(overTime, weekendTime, restTime);
        return data;
    }

    public Duration totalOvertime(Day day, Person person) {
        return overtime(day, person).getTotal();
    }

    public Duration totalOvertime(Day day) {
        return overtime(day).getTotal();
    }
}
