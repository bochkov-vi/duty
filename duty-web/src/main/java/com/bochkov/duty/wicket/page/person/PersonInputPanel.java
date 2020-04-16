package com.bochkov.duty.wicket.page.person;

import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.PersonGroupRepository;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.duty.wicket.page.dutytype.DutyTypeFieldMultiSelect;
import com.bochkov.duty.wicket.page.persongroup.PersonGroupFieldSelect;
import com.bochkov.duty.wicket.page.rang.RangFieldSelect;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
        add(new TextField<>("id"));
        add(new RequiredTextField<>("firstName"));
        add(new RequiredTextField<>("middleName"));
        add(new RequiredTextField<>("lastName"));
        add(new RangFieldSelect("rang").setRequired(true));
        add(new PersonGroupFieldSelect("personGroup"));
        add(new DutyTypeFieldMultiSelect("dutyTypes"));
        add(new RequiredTextField<>("post"));
        add(new TextField<>("createdDate").setEnabled(false));
    }
}
