package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "VACATION")
@Entity
@NoArgsConstructor
public class Vacation extends AbstractEntity<Long> {

    public final static Comparator<Vacation> COMPARATOR = Comparator.comparing(Vacation::getStart).thenComparing(Vacation::getEnd);

    @Id
    @Column(name = "ID_VACATION")
    @GeneratedValue(generator = "VACATION_SEQ")
    Long id;

    @ManyToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE")
    Employee employee;

    @Column(name = "YEAR")
    Integer year;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "VACATION_TYPE")
    VacationType type = VacationType.MAIN;

    @Column(name = "START", nullable = false)
    LocalDate start;

    @Column(name = "END", nullable = false)
    LocalDate end;

    @Column(name = "NOTE")
    String note;

    @Column(name = "DAY_COUNT")
    Integer dayCount;

    public static Vacation of(Employee employee, LocalDate start, LocalDate end) {
        Vacation vacation = new Vacation().setEmployee(employee).setStart(start).setEnd(end);
        vacation.preUpdate();
        return vacation;
    }

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        dayCount = (int) ChronoUnit.DAYS.between(start, end) + 1;

    }

}
