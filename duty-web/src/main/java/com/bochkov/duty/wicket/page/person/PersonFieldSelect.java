package com.bochkov.duty.wicket.page.person;

import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.PersonRepository;
import com.bochkov.duty.wicket.component.select2.BootstrapSelect2Field;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import java.util.Optional;

@Getter
public class PersonFieldSelect extends BootstrapSelect2Field<PersonRepository, Person, String> {
    @SpringBean
    PersonRepository repository;

    public PersonFieldSelect(String id) {
        super(id, "lastName", "firstName", "id");
    }

    public PersonFieldSelect(String id, IModel<Person> model) {
        super(id, model, "lastName", "firstName", "id");
    }

    @Override
    protected String convertToId(String str) {
        return Optional.ofNullable(str).filter(v -> !Strings.isEmpty(v)).orElse(null);
    }
}
