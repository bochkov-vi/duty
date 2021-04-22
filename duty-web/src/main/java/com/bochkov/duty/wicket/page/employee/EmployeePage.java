package com.bochkov.duty.wicket.page.employee;

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

@MountPath("employee")
public class EmployeePage extends EntityPage<Employee, Integer> {

    @SpringBean
    private EmployeeRepository employeeRepository;

    public EmployeePage() {
        super(Employee.class);
    }

    public EmployeePage(Employee entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<Employee, String>> columns() {
        List<IColumn<Employee, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("employee.id"), "id", Employee::getId));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("employee.post"), "post", Employee::getPost));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("employee.rang"), "rang", p -> Optional.ofNullable(p).map(Employee::getRang).map(Rang::getName).orElse(null)));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("employee.firstName"), "firstName", Employee::getFirstName));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("employee.middleName"), "middleName", Employee::getMiddleName));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("employee.lastName"), "lastName", Employee::getLastName));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("employee.employeeGroup"), "employeeGroup.name", p -> Optional.ofNullable(p).map(Employee::getEmployeeGroup).map(EmployeeGroup::getName).orElse(null)));
        list.add(new LambdaColumn<Employee, String>(new ResourceModel("employee.shiftTypes"), "employeeGroup.shiftTypes", p -> Optional.ofNullable(p).map(Employee::getShiftTypes).map(dts -> Joiner.on("; ").skipNulls().join(dts)).orElse(null)));
        return list;
    }


    @Override
    protected EmployeeRepository getRepository() {
        return employeeRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Employee> model) {
        return new EmployeeInputPanel(id, model);
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
