package com.bochkov.duty.planning.service;

import com.bochkov.duty.jpa.entity.*;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.DutyRepository;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.planning.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PlanningReportServiceImpl implements PlanningReportService {
    @Autowired
    DayRepository dayRepository;

    @Autowired
    DutyRepository dutyRepository;

    @Autowired
    DutyTypeRepository dutyTypeRepository;

    @Autowired
    ReportRepository reportRepository;

    @Override
    public PlanningReport loadReport(Integer id) {
        return reportRepository.findById(id).map(this::create).orElse(null);
    }

    @Override
    public PlanningReport create(Report report) {
        PlanningReport planningReport = new PlanningReport();
        planningReport.setPersonList(report.getPersons().stream().map(this::create).collect(Collectors.toList()));
        planningReport.setDayList(days(dayRepository.findOrCreate(report.getDateFrom(), report.getDateTo())));
        planningReport.setDutyTypeList(Stream.concat(Stream.of(PlanningDutyType.EMPTY), Stream.of(dutyTypeRepository.getOne(3)).map(this::create)).collect(Collectors.toList()));
        planningReport.setDutyList(planningReport.getPersonList().stream().flatMap(p -> {
            return create(p, planningReport.getDayList()).stream();
        }).collect(Collectors.toList()));
        return planningReport;
    }

    public PlanningPerson create(Person person) {
        PlanningPerson planningPerson = new PlanningPerson(person);
        return planningPerson;
    }

    public PlanningDay create(Day day) {
        PlanningDay planningDay = new PlanningDay(day);
        return planningDay;
    }

    public List<PlanningDay> days(List<Day> days) {
        List<PlanningDay> planningDays = days.stream().sorted(Comparator.comparing(Day::getId)).map(PlanningDay::new).collect(Collectors.toList());
        PlanningDay pd1 = null;
        Integer index = 0;
        for (PlanningDay pd2 : planningDays) {
            if (pd1 != null) {
                pd1.setNext(pd2);
                pd1.setIndex(index);
                pd2.setPrev(pd1);
                pd2.setIndex(index + 1);
            }
            index++;
            pd1 = pd2;
        }
        return planningDays;
    }

    public List<PlanningDuty> create(PlanningPerson person, List<PlanningDay> days) {
        List<PlanningDuty> duties = days.stream().sorted(Comparator.comparing(PlanningDay::getDate)).map(d -> create(person, d)).collect(Collectors.toList());
        /*PlanningDuty d1 = null;
        for (PlanningDuty d2 : duties) {
            if (d1 != null) {
                d1.setNext(d2);
                d2.setPrev(d1);
            }
            d1 = d2;
        }*/
        return duties;
    }

    public PlanningDuty create(PlanningPerson person, PlanningDay day) {
        Duty duty = dutyRepository.findByPersonAndDay(person.getPerson(), day.getDay()).orElseGet(() -> new Duty(person.getPerson(), day.getDay()));
        PlanningDuty planningDuty = new PlanningDuty(duty, person, day);
        if (duty.getDutyType() != null) {
            planningDuty.setDutyType(create(duty.getDutyType()));
        } else {
            planningDuty.setDutyType(PlanningDutyType.EMPTY);
        }
        return planningDuty;
    }

    public PlanningDutyType create(DutyType dutyType) {
        PlanningDutyType planningDutyType = new PlanningDutyType(dutyType);
        return planningDutyType;
    }
}
