package com.bochkov.duty.wicket.page.employee;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.wicket.component.select2.data.PersistableChoiceProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import javax.inject.Inject;
import java.util.Collection;

public class EmployeeDataProvider extends PersistableChoiceProvider<Employee, String> {

    @Inject
    EmployeeRepository repository;

    IModel<Collection<Employee>> excludes;

    public EmployeeDataProvider() {
        super("lastName", "firstName", "id");
    }

    public EmployeeDataProvider(IModel<Collection<Employee>> excludes) {
        this();
        this.excludes = LoadableDetachableModel.of(excludes::getObject);
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
    public Collection<Employee> excludes() {
        return excludes.getObject();
    }

    @Override
    public String toId(String str) {
        return str;
    }

    @Override
    public void detach() {
        super.detach();
        if (excludes != null) {
            excludes.detach();
        }
    }
}
