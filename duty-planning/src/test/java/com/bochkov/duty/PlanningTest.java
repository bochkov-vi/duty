package com.bochkov.duty;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.*;
import com.bochkov.duty.planning.DutyAssigment;
import com.bochkov.duty.planning.DutyPlan;
import com.bochkov.duty.planning.PlanningSpringConfiguration;
import com.bochkov.duty.planning.service.PlanningService;
import com.google.common.base.Joiner;
import com.google.common.collect.*;
import dnl.utils.text.table.GuavaTableModel;
import dnl.utils.text.table.TextTable;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PlanningSpringConfiguration.class)
public class PlanningTest {

    @Autowired
    PlanningService planningService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    DayRepository dayRepository;

    @Autowired
    DutyRepository dutyRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    DutyTypeRepository dutyTypeRepository;

    LocalDate start = LocalDate.of(2020, 01, 01);

    LocalDate end = LocalDate.of(2020, 01, 31);


    @Test
    public void createReport() {

        DutyPlan dutyPlan = new DutyPlan();

        dayRepository.findOrCreate(start, start.plusDays(9)).forEach(day -> dayRepository.safeSave(day.setWeekend(true)));
        dutyPlan.setDays(dayRepository.findOrCreate(start, end));

        dutyPlan.setPersons(personRepository.findAll().stream().limit(8).collect(Collectors.toList()));
        dutyPlan.setDutyTypes(dutyTypeRepository.findAllById(Lists.newArrayList(3)));
        int[] index = new int[]{0};
        dutyPlan.setDuties(dutyPlan.getDays().stream()
                .map(d -> DutyAssigment.of(d, dutyTypeRepository.findById(3).get()).setId(index[0]++)).collect(Collectors.toList()));

        dutyPlan.getDutyPlanOptions().setMinInterval((int)Math.ceil(dutyPlan.getPersons().size() / 2.0));
        DutyPlan plan = planningService.solve(dutyPlan);
        System.out.println(plan);
        Map<Person, Long> map = plan.getDuties().stream().collect(Collectors.groupingBy(DutyAssigment::getPerson, Collectors.counting()));
        System.out.println(Joiner.on("\n").withKeyValueSeparator("->").join(map));
        System.out.println("========== ВЫХОДНЫЕ ==========");
        System.out.println(Joiner.on("\n").withKeyValueSeparator("->").join(
                plan.getDuties().stream().filter(DutyAssigment::isWeekend).collect(Collectors.groupingBy(DutyAssigment::getPerson, Collectors.counting()))
        ));

        System.out.println("========== ВРЕМЯ ==========");
        System.out.println(Joiner.on("\n").withKeyValueSeparator("->").join(
                Maps.transformValues(plan.getDuties().stream().collect(Collectors.groupingBy(DutyAssigment::getPerson, Collectors.summingLong(da -> da.getOverTime().toMinutes()))), Duration::ofMinutes)
        ));

//        System.out.println("====================================");
//        System.out.println(plan.getDuties().stream().sorted(Comparator.comparing(DutyAssigment::getDay)).map(d -> String.format("%s %s %s", d.isWeekend(), d.getDay(), d.getPerson())).collect(Collectors.joining("\n")));

        System.out.println(plan.getScore());
        printData(plan);
    }

    @Test
    public void testStatistic() {

        SummaryStatistics ss = Stream.of(2, 3, 4, 5, 6, 7, 8, 9, 1).collect(SummaryStatistics::new, SummaryStatistics::addValue, (s1, s2) -> {
        });
        System.out.println(ss);
    }

    public void printData(DutyPlan plan) {
        List<Person> personList = plan.getPersons();
        int startIndex = 2;
        Table<Integer, String, String> table = plan.getDuties().stream().collect(Tables.toTable(da -> personList.indexOf(da.getPerson()) + startIndex
                , da -> da.getDay().getId().format(DateTimeFormatter.ofPattern("dd"))
                , da -> da.getDutyType().getUiOptions().getPlainText(),
                () -> TreeBasedTable.create()));
        for (Person person : personList) {
            int index = personList.indexOf(person);
            table.put(index + startIndex, "----", person.toString());
        }
        table.put(0, "----", "");
        for (Day day : plan.getDays()) {
            table.put(0, day.getId().format(DateTimeFormatter.ofPattern("dd")), day.getId().format(DateTimeFormatter.ofPattern("EE")));
        }

        table.put(1, "----", "");
        for (Day day : plan.getDays()) {
            table.put(1, day.getId().format(DateTimeFormatter.ofPattern("dd")),
                    Optional.ofNullable(day).map(Day::isWeekend).filter(v -> v).map(v -> "*").orElse(""));
        }
        GuavaTableModel model = new GuavaTableModel(table);
        TextTable textTable = new TextTable(model);
        textTable.printTable();
    }
}
