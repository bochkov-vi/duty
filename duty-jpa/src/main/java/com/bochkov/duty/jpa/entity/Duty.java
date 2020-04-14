package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "duty")
@Entity
public class Duty extends AbstractAuditableEntity<DutyPK> {
    @EmbeddedId
    DutyPK id;

    @ManyToOne
    @JoinColumn(name = "id_person", nullable = false, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "DUTY_PERSON_FK"))
    Person person;

    @ManyToOne
    @JoinColumn(name = "date", nullable = false, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "DUTY_DAY_FK"))
    Day day;

    @ManyToOne
    @JoinColumn(name = "id_duty_type", nullable = true, foreignKey = @ForeignKey(name = "DUTY_DUTY_TYPE_FK", value = ConstraintMode.NO_CONSTRAINT))
    DutyType dutyType;

    @OneToOne
    /*@JoinColumns(value = {
            @JoinColumn(name = "id_person", referencedColumnName = "id_person", insertable = false, updatable = false),
            @JoinColumn(name = "next", referencedColumnName = "date")
    }, foreignKey = @ForeignKey(name = "DUTY_NEXT_DUTY_FK"))*/
    @JoinTable(name = "duty_next", joinColumns = {@JoinColumn(name = "id_person", referencedColumnName = "id_person")
            , @JoinColumn(name = "date", referencedColumnName = "date")},
            inverseJoinColumns = {@JoinColumn(name = "id_person_next", referencedColumnName = "id_person")
                    , @JoinColumn(name = "date_next", referencedColumnName = "date")})

    Duty next;

    @OneToOne(mappedBy = "next")
    Duty prev;

    @ElementCollection
    @CollectionTable(name = "duty_period", joinColumns = {@JoinColumn(name = "id_person", referencedColumnName = "id_person"),
            @JoinColumn(name = "date", referencedColumnName = "date")}
            , foreignKey = @ForeignKey(name = "duty_period_fk", foreignKeyDefinition = "foreign key (ID_PERSON, DATE) references DUTY(ID_PERSON,DATE) ON UPDATE CASCADE ON DELETE CASCADE")
    )
    Set<Period> periods;


    public Duty() {
    }

    public Duty(DutyPK id) {
        this.id = id;
    }

    public static Duty of(Person person, Day day) {
        Duty _this = new Duty();
        _this.setId(new DutyPK(person, day));
        _this.person = person;
        _this.day = day;
        return _this;
    }

    public static Duty of(Person person, Day day, DutyType dutyType) {
        Duty _this = new Duty();
        if (person != null && day != null) {
            _this.setId(new DutyPK(person, day));
        }
        _this.person = person;
        _this.day = day;
        _this.dutyType = dutyType;
        return _this;
    }

    @PrePersist
    public void prePersist() {
        if (dutyType == null && day != null) {
            setDutyType(day.getDutyType());
        }
    }

}
