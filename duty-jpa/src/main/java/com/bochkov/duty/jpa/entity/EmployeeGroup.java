package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "EMPLOYEE_GROUP")
@Entity
public class EmployeeGroup extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEE_GROUP_SEQ")
    @SequenceGenerator(name = "EMPLOYEE_GROUP_SEQ", allocationSize = 1)
    @Column(name = "ID_EMPLOYEE_GROUP")
    Integer id;

    @Column(name = "EMPLOYEE_GROUP", nullable = false, unique = true)
    String name;

    @Override
    public String toString() {
        return name;
    }
}
