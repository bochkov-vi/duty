package com.bochkov.duty.wicket.page.persongroup;

import com.bochkov.duty.jpa.entity.PersonGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class PersonGroupInputPanel extends GenericPanel<PersonGroup> {

    public PersonGroupInputPanel(String id, IModel<PersonGroup> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<PersonGroup>)getModel()).bind();
        add(new TextField<>("id").setEnabled(false));
        add(new RequiredTextField<>("name"));
        add(new TextField<>("createdDate").setEnabled(false));
    }
}
