package com.bochkov.duty.wicket.page.person;

import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.PersonGroupRepository;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.duty.wicket.page.dutytype.DutyTypeFieldMultiSelect;
import com.bochkov.duty.wicket.page.persongroup.PersonGroupFieldSelect;
import com.bochkov.duty.wicket.page.rang.RangFieldSelect;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collection;
import java.util.Optional;

public class PersonInputPanel extends GenericPanel<Person> {
    @SpringBean
    RangRepository rangRepository;

    @SpringBean
    PersonGroupRepository personGroupRepository;

    public PersonInputPanel(String id, IModel<Person> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<Person>)getModel()).bind();
        add(new TextField<>("person.id", LambdaModel.of(getModel(), Person::getId, Person::setId)));
        add(new RequiredTextField<>("person.firstName", LambdaModel.of(getModel(), Person::getFirstName, Person::setFirstName)));
        add(new RequiredTextField<>("person.middleName", LambdaModel.of(getModel(), Person::getMiddleName, Person::setMiddleName)));
        add(new RequiredTextField<>("person.lastName", LambdaModel.of(getModel(), Person::getLastName, Person::setLastName)));
        add(new RangFieldSelect("person.rang", LambdaModel.of(getModel(), Person::getRang, Person::setRang)).setRequired(true));
        add(new PersonGroupFieldSelect("person.personGroup", LambdaModel.of(getModel(), Person::getPersonGroup, Person::setPersonGroup)));
        add(new DutyTypeFieldMultiSelect("person.dutyTypes", LambdaModel.of(getModel(),
                person -> Optional.ofNullable(person.getDutyTypes()).map(s -> (Collection) s).orElseGet(ImmutableList::<DutyType>of),
                (p, c) -> p.setDutyTypes(Sets.newHashSet(c)))));
        add(new RequiredTextField<>("person.post", LambdaModel.of(getModel(), Person::getPost, Person::setPost)));
        add(new TextField<>("person.createdDate", LambdaModel.of(getModel(), Person::getCreatedDate, Person::setCreatedDate)).setEnabled(false));
    }
}
