package com.bochkov.duty.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VacationPart implements Serializable, Comparable<VacationPart> {

    public static Comparator<VacationPart> COMPARATOR = Comparator.comparing(VacationPart::getStart);

    @Column(name = "START", nullable = false)
    LocalDate start;

    @Column(name = "END", nullable = false)
    LocalDate end;

    @Column(name = "PART_NUMBER", nullable = false)
    Integer partNumber;

    public static VacationPart of(LocalDate start, LocalDate end, Integer partNumber) {
        return new VacationPart(start, end, partNumber);
    }

    @Override
    public int compareTo(VacationPart o) {
        return COMPARATOR.compare(this, o);
    }
}
