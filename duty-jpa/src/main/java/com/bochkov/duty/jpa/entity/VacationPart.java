package com.bochkov.duty.jpa.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VACATION_PART")
public class VacationPart extends AbstractEntity<VacationPartPK> implements Comparable<VacationPart> {

    public static Comparator<VacationPart> COMPARATOR = Comparator.comparing(VacationPart::getId);

    @EmbeddedId
    VacationPartPK id;

    @Column(name = "END", nullable = false)
    LocalDate end;

    @Column(name = "PART_NUMBER", nullable = false)
    Integer partNumber;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "YEAR", referencedColumnName = "YEAR", insertable = false, updatable = false),
            @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE", insertable = false, updatable = false)})
    Vacation vacation;

    public static VacationPart of(Vacation vacation, LocalDate start, LocalDate end, Integer partNumber) {
        assert vacation.getId() != null;
        return new VacationPart(new VacationPartPK(vacation.getId().getYear(), vacation.getId().getIdEmployeer(), start), end, partNumber, vacation);
    }

   /* public static List<VacationPart> concat(VacationPart p1, VacationPart p2) {
        Range<LocalDate> r1 = p1.asRange();
        Range<LocalDate> r2 = p2.asRange();
        List<VacationPart> result = null;
        if (r1.isConnected(r2)) {
            result = Lists.newArrayList(Optional.of(r1.span(r2)).map(new VacationPart()))
        }
        return result;
    }*/

    @Override
    public int compareTo(VacationPart o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s. (%s-%s)", partNumber, id.getStart(), end);
    }

    public String toString(DateTimeFormatter formatter) {
        return String.format("%s. (%s)", partNumber, Stream.of(id.getStart(), end).map(d -> d.format(formatter)).collect(Collectors.joining("-")));
    }

    public double percentOverlap(LocalDate start, LocalDate end) {
        double result = 0.0;
        Range<LocalDate> request = Range.closed(start, end);
        Range<LocalDate> thisData = Range.closed(id.getStart(), this.end);
        if (request.isConnected(thisData)) {
            double count = ChronoUnit.DAYS.between(start, end);
            Range<LocalDate> i = request.intersection(thisData);
            double icount = ChronoUnit.DAYS.between(i.lowerEndpoint(), i.upperEndpoint());
            if (icount > 0) {
                result = icount / count;
            }
        }
        return result;
    }

    public long daysCount() {
        return ChronoUnit.DAYS.between(id.getStart(), end);
    }

    public Range<LocalDate> asRange() {
        Range<LocalDate> result = null;
        LocalDate start = Optional.ofNullable(id).map(VacationPartPK::getStart).orElse(null);
        if (start != null && end != null) {
            result = Range.closed(start, end);
        }
        return result;
    }
}
