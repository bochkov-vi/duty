package com.bochkov.duty.wicket.page.person;

import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.PersonRepository;
import com.bochkov.wicket.component.select2.data.PersistableChoiceProvider;
import org.apache.wicket.injection.Injector;

import javax.inject.Inject;

public class PersonDataProvider extends PersistableChoiceProvider<Person, String> {

    @Inject
    PersonRepository repository;

    public PersonDataProvider() {
        super("lastName", "firstName", "id");
    }

    @Override
    public String idToString(String s) {
        return s;
    }

    @Override
    public PersonRepository getRepository() {
        if (repository == null) {
            Injector.get().inject(this);
        }
        return repository;
    }

    @Override
    public String toId(String str) {
        return str;
    }
}
