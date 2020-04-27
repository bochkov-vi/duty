package com.bochkov.duty.wicket.page.employee;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.select2.Select2Choice;

@Getter
public class EmployeeFieldSelect extends Select2Choice<Employee> {

    @SpringBean
    EmployeeRepository repository;

    public EmployeeFieldSelect(String id) {
        super(id);
    }

    public EmployeeFieldSelect(String id, IModel<Employee> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        getSettings().setCloseOnSelect(true);
        setProvider(new EmployeeDataProvider());
        super.onInitialize();
    }
}
