package com.bochkov.duty.jpa.entity;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = "ROSTER")
@ToString
public class Roster extends AbstractAuditableEntity<Integer> {

    @Id
    @GeneratedValue(generator = "ROSTER_SEQ")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "ROSTER_SEQ")
    @Column(name = "ID_ROSTER", nullable = false)
    Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ROSTER_EMPLOYEE", joinColumns = @JoinColumn(name = "ID_ROSTER", referencedColumnName = "ID_ROSTER"), inverseJoinColumns = @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE"))
    Set<Employee> employees;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ROSTER_SHIFT_TYPE", joinColumns = @JoinColumn(name = "ID_ROSTER", referencedColumnName = "ID_ROSTER"), inverseJoinColumns = @JoinColumn(name = "ID_SHIFT_TYPE", referencedColumnName = "ID_SHIFT_TYPE"))
    Set<ShiftType> shiftTypes;

    @Column(name = "DATE", nullable = false)
    LocalDate date;

    @Column(name = "DATE_FROM", nullable = false)
    LocalDate dateFrom;

    @Column(name = "DATE_TO", nullable = false)
    LocalDate dateTo;

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
