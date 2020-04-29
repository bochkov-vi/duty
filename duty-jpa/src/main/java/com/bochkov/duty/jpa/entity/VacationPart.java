package com.bochkov.duty.jpa.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode(of = "start")
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

    @Override
    public String toString() {
        return String.format("%s. (%s-%s)", partNumber, start, end);
    }

    public String toString(DateTimeFormatter formatter) {
        return String.format("%s. (%s)", partNumber, Stream.of(start, end).map(d -> d.format(formatter)).collect(Collectors.joining("-")));
    }
}
