package com.bochkov.duty.wicket.page.employee;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeGroupRepository;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.duty.wicket.page.employeegroup.EmployeeGroupFieldSelect;
import com.bochkov.duty.wicket.page.rang.RangFieldSelect;
import com.bochkov.duty.wicket.page.shifttype.ShiftTypeFieldMultiSelect;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EmployeeInputPanel extends GenericPanel<Employee> {

    @SpringBean
    RangRepository rangRepository;

    @SpringBean
    EmployeeGroupRepository employeeGroupRepository;

    public EmployeeInputPanel(String id, IModel<Employee> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<Employee>)getModel()).bind();
        add(new TextField<>("id").setRequired(true));
        add(new RequiredTextField<>("firstName"));
        add(new RequiredTextField<>("middleName"));
        add(new RequiredTextField<>("lastName"));
        add(new RangFieldSelect("rang").setRequired(true));
        add(new EmployeeGroupFieldSelect("employeeGroup"));
        add(new ShiftTypeFieldMultiSelect("shiftTypes"));
        add(new RequiredTextField<>("post"));
        add(new TextField<>("createdDate").setEnabled(false));
    }
}
