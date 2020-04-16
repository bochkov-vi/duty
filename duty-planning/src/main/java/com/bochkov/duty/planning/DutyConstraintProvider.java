package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.entity.Person;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.*;
import org.optaplanner.core.api.score.stream.bi.BiConstraintCollector;
import org.optaplanner.core.api.score.stream.tri.TriConstraintCollector;
import org.optaplanner.core.impl.score.stream.bi.DefaultBiConstraintCollector;
import org.optaplanner.core.impl.score.stream.tri.DefaultTriConstraintCollector;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;

public class DutyConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{notInitDutyDay(factory),
                notNextDutyDay(factory),
                leastWeekend(factory),
                dispersionOfDutyCount(factory),
                moreIntervalBeteenDuties(factory)};
    }

    private Constraint notInitDutyDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class).filter(da -> da.person == null)
                .penalize("нельзя дежурства без людей", HardMediumSoftScore.ONE_MEDIUM);
    }

    private Constraint notNextDutyDay(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .join(DutyAssigment.class)
                .filter((d1, d2) -> Objects.equals(d1.getPerson(), d2.getPerson()) && d1.day.getId().equals(d2.getDay().getId().plusDays(1)))
                .penalize("нельзя одно и тоже дежурство за подряд", HardMediumSoftScore.ONE_HARD);
    }

    private Constraint leastWeekend(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .filter(DutyAssigment::isWeekend)
                .groupBy(DutyAssigment::getPerson, count())
                .filter((p, cnt) -> cnt > 1)
                .penalize("меньше выходных дежурств", HardMediumSoftScore.ONE_SOFT, (person, cnt) -> cnt * 2);
    }

    private Constraint dispersionOfDutyCount(ConstraintFactory factory) {
        return factory.from(Person.class)
                .join(DutyAssigment.class, Joiners.equal(Function.identity(), DutyAssigment::getPerson))
                .groupBy(new BiFunction<Person, DutyAssigment, Person>() {
                    @Override
                    public Person apply(Person person, DutyAssigment dutyAssigment) {
                        return person;
                    }
                }, ConstraintCollectors.countBi())
                .groupBy(new DefaultBiConstraintCollector<>(SynchronizedSummaryStatistics::new,
                        (ss, person, cnt) -> () -> ss.addValue(cnt),
                        SummaryStatistics::getVariance))
                .penalize("Распределение дежурств по людям должно быть равномерным", HardMediumSoftScore.ONE_SOFT, new ToIntFunction<Double>() {
                    @Override
                    public int applyAsInt(Double v) {
                        return new Double(v * 10).intValue();
                    }
                });
    }

    private Constraint dispersionOfDutyCount2(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .groupBy(DutyAssigment::getPerson, DutyAssigment::getWeekNumber, count())
                .groupBy(triVariance())
                .penalize("Распределение дежурств по неделям должно быть равномерным", HardMediumSoftScore.ONE_SOFT, new ToIntFunction<Double>() {
                    @Override
                    public int applyAsInt(Double v) {
                        return new Double(v * 10).intValue();
                    }
                });
    }

    private Constraint moreIntervalBeteenDuties(ConstraintFactory factory) {
        return factory.from(DutyAssigment.class)
                .join(Person.class)
                .filter((d, p) -> Objects.equals(d.getPerson(), p))
                .join(DutyAssigment.class)
                .filter((d1, p, d2) -> Objects.equals(d1.getPerson(), d2.getPerson()))
                .filter((d1, p, d2) -> d1.day.isBefore(d2.day))
                .filter((d1, p, d2) -> (int) ChronoUnit.DAYS.between(d1.day.getId(), d2.day.getId()) < 7)
                .penalize("больше интервал между дежурствами", HardMediumSoftScore.ONE_SOFT,
                        (d1, p, d2) -> 7 - (int) ChronoUnit.DAYS.between(d1.day.getId(), d2.day.getId()));
    }

    public BiConstraintCollector<Person, Integer, SummaryStatistics, Double> biVariance() {
        BiConstraintCollector<Person, Integer, SummaryStatistics, Double> result = null;
        result = new DefaultBiConstraintCollector<>(SynchronizedSummaryStatistics::new,
                (ss, person, cnt) -> () -> ss.addValue(cnt),
                SummaryStatistics::getVariance);
        return result;
    }

    public TriConstraintCollector<Person, Integer, Integer, SummaryStatistics, Double> triVariance() {
        TriConstraintCollector<Person, Integer, Integer, SummaryStatistics, Double> result = null;
        result = new DefaultTriConstraintCollector<>(SynchronizedSummaryStatistics::new,
                (ss, person, w, cnt) -> () -> ss.addValue(cnt), summaryStatistics -> summaryStatistics.getVariance());
        return result;
    }
}
