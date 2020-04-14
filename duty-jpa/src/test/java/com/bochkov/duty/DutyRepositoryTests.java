package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Duty;
import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.DutyRepository;
import com.bochkov.duty.jpa.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DutyJpaConfig.class)
public class DutyRepositoryTests {

    @Autowired
    DutyRepository repository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    DayRepository dayRepository;

    LocalDate date = LocalDate.now();


    @Before
    public void setupTest() {
        repository.deleteAll();
    }

    @Test
    public void saveWithChilds() {
        //Day day = dayRepository.findOrCreate(date);
        Person person = personRepository.findById("bochkov").get();
        for (Day day : Stream.iterate(LocalDate.now(), d -> d.minusDays(1)).limit(30)
                .map(date -> dayRepository.findOrCreate(date)).collect(Collectors.toList())) {
            Duty duty = repository.findOrCreate(person, day);
        }
        //Duty duty = new Duty(personRepository.findById("bochkov").get(), day);
        ;
    }


}
