package com.bochkov.duty.jpa.entity;

import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "duty_type")
@Entity
@NoArgsConstructor
public class DutyType extends AbstractEntity<Integer> {

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

    public OvertimeData overtime(Day day) {
        OvertimeData data = new OvertimeData();
        Range<LocalDateTime> dayRange = day.range();
        boolean weekend = day.isWeekend();
        Set<LocalDateTime> set = Sets.newTreeSet();
        for (Period p1 : periods) {

        }
        return data;
    }

}
