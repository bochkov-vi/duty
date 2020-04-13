package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "person")
@Entity
public class Person extends AbstractEntity<String> {

    @Id
    @Column(name = "id_person", length = 15)
    String id;

    @ManyToOne
    @JoinColumn(name = "id_rang", nullable = false)
    Rang rang;

    @Column(name = "post")
    String post;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "middle_name")
    String middleName;

    @Column(name = "last_name")
    String lastName;

    @ManyToOne
    @JoinColumn(name = "id_person_group",foreignKey = @ForeignKey(name = "PERSON_PERSON_GROUP_FK"))
    PersonGroup personGroup;

    @JoinTable(name = "person_duty_type", joinColumns = @JoinColumn(name = "id_person", referencedColumnName = "id_person"),
            inverseJoinColumns = @JoinColumn(name = "id_duty_type", referencedColumnName = "id_duty_type"),
            foreignKey = @ForeignKey(name = "PERSON_DUTY_TRYPE_FK"))
    @ManyToMany
    Set<DutyType> dutyTypes;


    @Override
    public String toString() {
        return String.format("%s %s %.1s.%.1s.", Optional.ofNullable(rang).map(Rang::getName).orElse("-"), lastName, firstName, middleName);
    }


}
