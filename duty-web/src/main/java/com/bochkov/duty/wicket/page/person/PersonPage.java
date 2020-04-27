package com.bochkov.duty.wicket.page.person;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.EmployeeGroup;
import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.wicket.base.DetailsPanel;
import com.bochkov.duty.wicket.base.EntityPage;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;
import java.util.Optional;

@MountPath("personal")
public class PersonPage extends EntityPage<Employee, String> {
    @SpringBean
    private EmployeeRepository employeeRepository;

    public PersonPage() {
    }

    public PersonPage(Employee entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<Employee, String>> columns() {
        List<IColumn<Employee, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("person.id"), "id", Employee::getId));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("person.post"), "post", Employee::getPost));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("person.rang"), "rang", p-> Optional.ofNullable(p).map(Employee::getRang).map(Rang::getName).orElse(null)));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("person.firstName"), "firstName", Employee::getFirstName));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("person.middleName"), "middleName", Employee::getMiddleName));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("person.lastName"), "lastName", Employee::getLastName));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("person.employeeGroup"), "personGroup.name", p-> Optional.ofNullable(p).map(Employee::getEmployeeGroup).map(EmployeeGroup::getName).orElse(null)));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("person.shiftTypes"), "employeeGroup.shiftTypes", p-> Optional.ofNullable(p).map(Employee::getShiftTypes).map(dts-> Joiner.on("; ").skipNulls().join(dts)).orElse(null)));
        return list;
    }


    @Override
    protected EmployeeRepository getRepository() {
        return employeeRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Employee> model) {
        return new PersonInputPanel(id, model);
    }

    @Override
    protected GenericPanel<Employee> createDetailsPanel(String id, IModel<Employee> model) {
        return new DetailsPanel<Employee>(id, getModel(), ImmutableList.of("id", "post", "rang", "firstName", "middleName", "lastName", "createdDate"), "employee.");
    }

    @Override
    protected Employee newInstance() {
        return new Employee();
    }
}
