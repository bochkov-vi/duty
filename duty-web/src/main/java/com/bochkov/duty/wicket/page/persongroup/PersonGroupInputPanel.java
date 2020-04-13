package com.bochkov.duty.wicket.page.persongroup;

import com.bochkov.duty.jpa.entity.PersonGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;

public class PersonGroupInputPanel extends GenericPanel<PersonGroup> {
    public PersonGroupInputPanel(String id, IModel<PersonGroup> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<PersonGroup>)getModel()).bind();
        add(new TextField<>("personGroup.id", LambdaModel.of(getModel(), PersonGroup::getId, PersonGroup::setId)).setEnabled(false));
        add(new RequiredTextField<>("personGroup.name", LambdaModel.of(getModel(), PersonGroup::getName, PersonGroup::setName)));
        add(new TextField<>("personGroup.createdDate", LambdaModel.of(getModel(), PersonGroup::getCreatedDate, PersonGroup::setCreatedDate)).setEnabled(false));
    }
}
