package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.BaseRepository;
import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.ShiftType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DayRepository extends BaseRepository<Day, LocalDate> {

    @Transactional
    default Day findOrCreate(LocalDate date) {
        Day result = null;
        Optional<Day> day = findById(date);
        result = day.map(this::safeSave).orElseGet(() -> safeSave(new Day().setId(date)));
        return result;
    }


    @Transactional
    default Collection<Day> _saveWithWeekendRelationCalculate(Collection<Day> days) {
        for (Day day : days) {
            int daysFromWeekend = calculateDaysToWeekend(day, -1);
            day.setDaysFromWeekend(daysFromWeekend);
            int daysToWeekend = calculateDaysToWeekend(day, 1);
            day.setDaysToWeekend(daysToWeekend);
            _setupDutyType(day);
        }
        return saveAll(days);
    }

    @Transactional
    default Day _saveWithWeekendRelationCalculate(Day day) {
        int daysFromWeekend = calculateDaysToWeekend(day, -1);
        day.setDaysFromWeekend(daysFromWeekend);
        int daysToWeekend = calculateDaysToWeekend(day, 1);
        day.setDaysToWeekend(daysToWeekend);
        _setupDutyType(day);
        return save(day);
    }

    @Transactional
    default Day _saveWithChilds(Day day) {
        Day prev = findById(day.getId().minusDays(1)).orElseGet(() -> {
            Day d = new Day().setId(day.getId().minusDays(1));
            return save(d);
        });
        Day next = findById(day.getId().plusDays(1)).orElseGet(() -> {
            Day d = new Day().setId(day.getId().plusDays(1));
            return save(d);
        });
        day.setNext(next);
        next.setPrev(day);
        day.setPrev(prev);
        prev.setNext(day);
        return save(day);
    }

    @Transactional
    default Day safeSave(Day day) {
        return _save(day);
    }

    @Transactional
    default List<Day> findChain(Day day, int limit) {
        long sign = Float.valueOf(Math.signum(limit)).intValue();
        return Stream.iterate(day.getId().plusDays(sign), d -> d.plusDays(sign)).limit(Math.abs(limit)).map(ld -> findById(ld).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Transactional
    default Day _save(Day day) {
        day = _saveWithChilds(day);
        day = _saveWithWeekendRelationCalculate(day);
        _saveWithWeekendRelationCalculate(findChain(day, -5));
        _saveWithWeekendRelationCalculate(findChain(day, 5));
        day = _setupDutyType(day);
        day = _setupDutyTypeTimeUsage(day);
        return save(day);
    }


    @Transactional
    default Day _setupDutyTypeTimeUsage(Day day) {
        ShiftType shiftType = findDutyTypeByDaysToWeekend(day.getDaysToWeekend());
        if (shiftType != null) {
            Day.setupDutyTypeTimeUsage(day, shiftType);
        }
        return save(day);
    }

    @Transactional
    default Day _setupDutyType(Day day) {
        ShiftType shiftType = findDutyTypeByDaysToWeekend(day.getDaysToWeekend());
        day.setShiftType(shiftType);
        return day;
    }

    @Transactional
    default int calculateDaysToWeekend(Day day, int direction) {
        if (day.isWeekend()) {
            return 0;
        }
        return calculateDaysToWeekend(day.getId(), direction);
    }

    @Transactional
    default int calculateDaysToWeekend(LocalDate date, int direction) {
        return calculateDaysToWeekend(date, direction, 14);
    }

    @Transactional
    default int calculateDaysToWeekend(LocalDate date, int direction, int limit) {
        int result = -1;
        int step = Float.valueOf(Math.signum(direction)).intValue();
        int i = 0;
        for (Iterator<LocalDate> ir = Stream.iterate(date, temp -> temp.plusDays(step)).limit(limit).iterator(); ir.hasNext(); ) {

            LocalDate temp = ir.next();
            if (findById(temp).map(Day::isWeekend).orElseGet(() -> Day.isWeekendDate(temp))) {
                result = i;
                break;
            }
            i++;
        }
        if (result < 0) {
            throw new RuntimeException();
        }
        return result;
    }

    @Transactional
    default List<Day> findOrCreate(LocalDate dateFrom, LocalDate dateTo) {
        return Stream.iterate(dateFrom, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(dateFrom, dateTo.plusDays(1)))
                .sorted()
                .map(this::findOrCreate)
                .collect(Collectors.toList());
    }

    List<Day> findAllByIdBetween(LocalDate d1, LocalDate d2);


    @Query("SELECT o FROM Day o WHERE o.id>=:f AND o.id<=:t")
    List<Day> findAll(@Param("f") LocalDate dateFrom, @Param("t") LocalDate dateTo);


    @Transactional
    @Query("SELECT o FROM ShiftType o JOIN o.daysToWeekend w WHERE w = ?1")
    public ShiftType findDutyTypeByDaysToWeekend(Integer daysToWeekend);


}
