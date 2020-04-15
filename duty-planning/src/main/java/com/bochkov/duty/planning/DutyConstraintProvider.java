package com.bochkov.duty.planning;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.util.Objects;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;

public class DutyConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{notInitDutyDay(factory),
                notNextDutyDay(factory),
                leastWeekend(factory)};
    }

    private Constraint notInitDutyDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class).filter(da -> da.person == null)
                .penalize("нельзя дежурства без людей", HardSoftScore.ofHard(50));
    }

    private Constraint notNextDutyDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .join(DutyAssigment.class)
                .filter((d1, d2) -> Objects.equals(d1.getPerson(), d2.getPerson()) && d1.day.getId().equals(d2.getDay().getId().plusDays(1)))
                .penalize("нельзя одно и тоже дежурство за подряд", HardSoftScore.ONE_HARD);
    }

    private Constraint leastWeekend(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(DutyAssigment::isWeekend)
                .groupBy(DutyAssigment::getPerson, count())
                .filter((p, cnt) -> cnt > 1)
                .penalize("меньше выходных дежурств", HardSoftScore.ONE_SOFT, (person, cnt) -> cnt * 2);
    }
}
