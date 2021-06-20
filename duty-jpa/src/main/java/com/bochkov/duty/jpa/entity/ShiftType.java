package com.bochkov.duty.jpa.entity;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;
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
    @OrderBy("start")
    Set<Period> periods;

    public ShiftType(String name) {
        this.name = name;
    }

    public ShiftType(String name, int hStart, int mStart, int hDuration, int mDuration) {
        this.name = name;
        Period period = Period.of(hStart, mStart, hDuration, mDuration);
        setPeriods(Sets.newHashSet(period));
    }

    @Column(name = "LABEL", length = 3)
    String label;

    @Column(name = "ICON")
    String icon;

    @Override
    public String toString() {
        return name;
    }

    public Integer getId() {
        return id;
    }


}
