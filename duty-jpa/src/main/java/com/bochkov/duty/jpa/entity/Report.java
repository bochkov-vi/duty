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
@Table(name = "report")
public class Report extends AbstractAuditableEntity<Integer> {

    @Id
    @GeneratedValue(generator = "report_seq")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "report_seq")
    @Column(name = "id_report", nullable = false)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "id_duty_type")
    DutyType dutyType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "report_person", joinColumns = @JoinColumn(name = "id_report"), inverseJoinColumns = @JoinColumn(name = "id_person"))
    Set<Person> persons;

    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(name = "date_from", nullable = false)
    LocalDate dateFrom;

    @Column(name = "date_to", nullable = false)
    LocalDate dateTo;

    @ManyToOne
    @JoinColumn(name = "chief", nullable = false)
    Person chief;

    @ManyToOne
    @JoinColumn(name = "executor", nullable = false)
    Person executor;

    @Column(name = "report_title", nullable = false)
    String title;

    @Column(name = "date_title", nullable = false)
    String dateTitle;

    @Column(name = "genitive_department_name", nullable = false)
    String genitiveDepartment;

    @Override
    public String toString() {
        return title;
    }


    public List<LocalDate> createDateList() {
        List<LocalDate> dateList = Stream.iterate(dateFrom, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(dateFrom, dateTo))
                .collect(Collectors.toList());
        return dateList;
    }

    public boolean isInPeriod(LocalDate date) {
        if (date != null) {
            return !date.isBefore(dateFrom) && !date.isAfter(dateTo);
        }
        return false;
    }
}
