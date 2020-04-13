package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "rang")
@Entity
public class Rang extends AbstractEntity<Short> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rang_seq")
    @SequenceGenerator(name = "rang_seq", allocationSize = 1)
    @Column(name = "id_rang")
    Short id;

    @Column(name = "rang", nullable = false)
    String name;

    @Column(name = "full_name")
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
