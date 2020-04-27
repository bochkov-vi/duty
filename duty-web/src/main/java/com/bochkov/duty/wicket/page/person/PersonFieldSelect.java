package com.bochkov.duty.wicket.page.person;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.select2.Select2Choice;

@Getter
public class PersonFieldSelect extends Select2Choice<Employee> {

    @SpringBean
    EmployeeRepository repository;

    public PersonFieldSelect(String id) {
        super(id);
    }

    public PersonFieldSelect(String id, IModel<Employee> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        getSettings().setCloseOnSelect(true);
        setProvider(new PersonDataProvider());
        super.onInitialize();
    }
}
