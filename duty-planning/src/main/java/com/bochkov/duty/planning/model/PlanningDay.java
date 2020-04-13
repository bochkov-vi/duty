package com.bochkov.duty.planning.model;

import com.bochkov.duty.jpa.entity.Day;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Comparator;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class PlanningDay implements Comparable<PlanningDay> {
    final static Comparator<PlanningDay> COMPARATOR = Comparator.comparing(PlanningDay::getDay);

    @NonNull
    protected Day day;

    private transient Integer index;

    private PlanningDay next;

    private PlanningDay prev;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningDay)) {
            return false;
        }
        PlanningDay that = (PlanningDay) o;
        return Objects.equal(day, that.day);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(day);
    }

    public LocalDate getDate() {
        return day.getId();
    }

    public DayType getDayType() {
        return day.getDayType();
    }

    public boolean isWeekend() {
        return day.isWeekend();
    }

    @Override
    public int compareTo(PlanningDay o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("index", index)
                .add("day", day)
                .toString();
    }

    public boolean isAfter(PlanningDay day) {
        return this.day.isAfter(day.getDay());
    }

    public boolean isBefore(PlanningDay day) {
        return this.day.isBefore(day.day);
    }

    public int daysTo(PlanningDay day) {
        return Long.valueOf(this.day.daysTo(day.day)).intValue();
    }
}
