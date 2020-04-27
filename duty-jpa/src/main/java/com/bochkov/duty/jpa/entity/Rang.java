package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "RANG")
@Entity
public class Rang extends AbstractEntity<Short> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RANG_SEQ")
    @SequenceGenerator(name = "RANG_SEQ", allocationSize = 1)
    @Column(name = "ID_RANG")
    Short id;

    @Column(name = "RANG", nullable = false)
    String name;

    @Column(name = "FULL_NAME")
    String fullName;

    @Override
    public boolean isNew() {
        return super.isNew();
    }

    @Override
    public String toString() {
        return name;
    }
}
