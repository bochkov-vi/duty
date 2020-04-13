package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "person_group")
@Entity
public class PersonGroup extends AbstractEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_group_seq")
    @SequenceGenerator(name = "person_group_seq", allocationSize = 1)
    @Column(name = "id_person_group")
    Integer id;

    @Column(name = "person_group", nullable = false)
    String name;

    @Override
    public String toString() {
        return name;
    }
}
