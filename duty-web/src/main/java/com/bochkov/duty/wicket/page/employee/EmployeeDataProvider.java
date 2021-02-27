package com.bochkov.duty.wicket.page.employee;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.wicket.select2.data.PersistableChoiceProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import javax.inject.Inject;
import java.util.Collection;

public class EmployeeDataProvider extends PersistableChoiceProvider<Employee, Integer> {

    @Inject
    EmployeeRepository repository;

    IModel<Collection<Employee>> excludes;

    public EmployeeDataProvider() {
        super(Employee.class,Integer.class,"lastName", "firstName", "id");
    }

    public EmployeeDataProvider(IModel<Collection<Employee>> excludes) {
        this();
        this.excludes = LoadableDetachableModel.of(excludes::getObject);
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
        if (excludes != null) {
            return excludes.getObject();
        }
        return null;
    }


    @Override
    public void detach() {
        super.detach();
        if (excludes != null) {
            excludes.detach();
        }
    }
}
