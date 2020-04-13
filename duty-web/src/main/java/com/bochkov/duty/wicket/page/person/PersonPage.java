package com.bochkov.duty.wicket.page.person;

import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.entity.PersonGroup;
import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.PersonRepository;
import com.bochkov.duty.wicket.base.DetailsPanel;
import com.bochkov.duty.wicket.base.EntityPage;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;
import java.util.Optional;

@MountPath("personal")
public class PersonPage extends EntityPage<Person, String> {
    @SpringBean
    private PersonRepository personRepository;

    public PersonPage() {
    }

    public PersonPage(Person entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<Person, String>> columns() {
        List<IColumn<Person, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<Person, String>(new ResourceModel("person.id"), "id", Person::getId));
        list.add(new LambdaColumn<Person, String>(new ResourceModel("person.post"), "post", Person::getPost));
        list.add(new LambdaColumn<Person, String>(new ResourceModel("person.rang"), "rang",  p-> Optional.ofNullable(p).map(Person::getRang).map(Rang::getName).orElse(null)));
        list.add(new LambdaColumn<Person, String>(new ResourceModel("person.firstName"), "firstName", Person::getFirstName));
        list.add(new LambdaColumn<Person, String>(new ResourceModel("person.middleName"), "middleName", Person::getMiddleName));
        list.add(new LambdaColumn<Person, String>(new ResourceModel("person.lastName"), "lastName", Person::getLastName));
        list.add(new LambdaColumn<Person, String>(new ResourceModel("person.personGroup"), "personGroup.name", p-> Optional.ofNullable(p).map(Person::getPersonGroup).map(PersonGroup::getName).orElse(null)));
        list.add(new LambdaColumn<Person, String>(new ResourceModel("person.dutyTypes"), "personGroup.dutyTypes", p-> Optional.ofNullable(p).map(Person::getDutyTypes).map(dts-> Joiner.on("; ").skipNulls().join(dts)).orElse(null)));
        return list;
    }


    @Override
    protected PersonRepository getRepository() {
        return personRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Person> model) {
        return new PersonInputPanel(id, model);
    }

    @Override
    protected GenericPanel<Person> createDetailsPanel(String id, IModel<Person> model) {
        return new DetailsPanel<Person>(id, getModel(), ImmutableList.of("id", "post", "rang", "firstName", "middleName", "lastName", "createdDate"), "person.");
    }

    @Override
    protected Person newInstance() {
        return new Person();
    }
}
