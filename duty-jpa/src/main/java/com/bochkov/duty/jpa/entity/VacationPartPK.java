package com.bochkov.duty.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;

@Data
@Accessors(chain = true)
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class VacationPartPK implements Serializable, Comparable<VacationPartPK> {
    public static Comparator<VacationPartPK> COMPARATOR = Comparator.nullsFirst(
            Comparator.comparing(VacationPartPK::getYear).thenComparing(VacationPartPK::getIdEmployeer).thenComparing(VacationPartPK::getStart)
    );
    @Column(name = "YEAR")
    Integer year;

    @Column(name = "ID_EMPLOYEE")
    String IdEmployeer;

    @Column(name = "START")
    LocalDate start;

    @Override
    public int compareTo(VacationPartPK o) {
        return COMPARATOR.compare(this, o);
    }
}
