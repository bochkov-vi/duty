package com.bochkov.duty.jpa.entity;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bochkov
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "REPORT")
public class Report extends AbstractAuditableEntity<Integer> {

    @Id
    @GeneratedValue(generator = "REPORT_SEQ")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "REPORT_SEQ")
    @Column(name = "ID_REPORT", nullable = false)
    Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "REPORT_EMPLOYEE", joinColumns = @JoinColumn(name = "ID_REPORT", referencedColumnName = "ID_REPORT"), inverseJoinColumns = @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE"))
    Set<Employee> employees;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "REPORT_SHIFT_TYPE", joinColumns = @JoinColumn(name = "ID_REPORT", referencedColumnName = "ID_REPORT"), inverseJoinColumns = @JoinColumn(name = "ID_SHIFT_TYPE", referencedColumnName = "ID_SHIFT_TYPE"))
    Set<ShiftType> shiftTypes;

    @Column(name = "DATE", nullable = false)
    LocalDate date;

    @Column(name = "DATE_FROM", nullable = false)
    LocalDate dateFrom;

    @Column(name = "DATE_TO", nullable = false)
    LocalDate dateTo;

    @ManyToOne
    @JoinColumn(name = "CHIEF", nullable = false)
    Employee chief;

    @ManyToOne
    @JoinColumn(name = "EXECUTOR", nullable = false)
    Employee executor;

    @Column(name = "REPORT_TITLE", nullable = false)
    String title;

    @Column(name = "DATE_TITLE", nullable = false)
    String dateTitle;

    @Column(name = "GENITIVE_DEPARTMENT_NAME", nullable = false)
    String genitiveDepartment;

    @Override
    public String toString() {
        return title;
    }

}
