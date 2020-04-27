package com.bochkov.duty.wicket.page.employeegroup;

import com.bochkov.duty.jpa.entity.EmployeeGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class EmployeeGroupInputPanel extends GenericPanel<EmployeeGroup> {

    public EmployeeGroupInputPanel(String id, IModel<EmployeeGroup> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<EmployeeGroup>)getModel()).bind();
        add(new TextField<>("id").setEnabled(false));
        add(new RequiredTextField<>("name"));
        add(new TextField<>("createdDate").setEnabled(false));
    }
}
