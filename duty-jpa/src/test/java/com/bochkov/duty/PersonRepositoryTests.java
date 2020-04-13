package com.bochkov.duty;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
import com.bochkov.duty.jpa.repository.PersonGroupRepository;
import com.bochkov.duty.jpa.repository.PersonRepository;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DutyJpaConfig.class)
public class PersonRepositoryTests {

    @Autowired
    PersonRepository repository;

    @Autowired
    RangRepository rangRepository;

    @Autowired
    PersonGroupRepository personGroupRepository;

    @Autowired
    DutyTypeRepository dutyTypeRepository;

    @Before
    public void setupTest() {
        repository.deleteAll();
    }

    @Test
    public void saveBochkov() {
        Person person = new Person().setId("bochkov")
                .setRang(rangRepository.getOne((short) 25))
                .setFirstName("Виктор")
                .setLastName("Бочков")
                .setMiddleName("Иванович")
                .setPersonGroup(personGroupRepository.getOne(2))
                .setDutyTypes(Sets.newHashSet(dutyTypeRepository.findAll()));
        repository.save(person);
    }


}
