package com.bochkov.duty.wicket.page.employee;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.wicket.component.select2.data.PersistableChoiceProvider;
import org.apache.wicket.injection.Injector;

import javax.inject.Inject;

public class EmployeeDataProvider extends PersistableChoiceProvider<Employee, String> {

    @Inject
    EmployeeRepository repository;

    public EmployeeDataProvider() {
        super("lastName", "firstName", "id");
    }

    @Override
    public String idToString(String s) {
        return s;
    }

    @Override
    public EmployeeRepository getRepository() {
        if (repository == null) {
            Injector.get().inject(this);
        }
        return repository;
    }

    @Override
    public String toId(String str) {
        return str;
    }
}
