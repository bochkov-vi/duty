package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Duration;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "SHIFT")
@Entity
@NoArgsConstructor
@ToString(of = {"id", "day", "shiftType"})
public class Shift extends AbstractAuditableEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHIFT_SEQ")
    @SequenceGenerator(name = "SHIFT_SEQ", allocationSize = 1)
    @Column(name = "ID_SHIFT")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "DATE", nullable = false, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "DUTY_DAY_FK"))
    Day day;

    @ManyToOne
    @JoinColumn(name = "ID_SHIFT_TYPE", nullable = false, foreignKey = @ForeignKey(name = "DUTY_DUTY_TYPE_FK", value = ConstraintMode.NO_CONSTRAINT))
    ShiftType shiftType;

    @ElementCollection
    @CollectionTable(name = "SHIFT_PERIOD", joinColumns = {@JoinColumn(name = "ID_SHIFT", referencedColumnName = "ID_SHIFT")}
            , foreignKey = @ForeignKey(name = "SHIFT_PERIOD_FK", foreignKeyDefinition = "foreign key (ID_EMPLOYEE, DATE) references DUTY(ID_EMPLOYEE,DATE) ON UPDATE CASCADE ON DELETE CASCADE")
    )
    Set<Period> periods;

    @Transient
    Boolean weekend;

    @Transient
    Duration overTime;

    @Transient
    Boolean endOnNextDay;

    public static Shift of(Day day, ShiftType shiftType) {
        Shift _this = new Shift();
        _this.shiftType = shiftType;
        _this.day = day;
        return _this;
    }

    @PostPersist
    @PostUpdate
    @PostLoad
    public void calculateTransientFields() {
        weekend = getShiftType().isStartOnWeekend(getDay()) || getShiftType().isEndOnWeekend(getDay());
        overTime = getShiftType().totalOvertime(getDay());
        endOnNextDay = getShiftType().isEndOnNextDay(getDay());
    }


}
