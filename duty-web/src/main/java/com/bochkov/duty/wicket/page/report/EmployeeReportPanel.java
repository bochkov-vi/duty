package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.wicket.page.employee.EmployeeFieldSelect;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import com.bochkov.wicket.data.provider.SortedListModelDataProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

public class EmployeeReportPanel extends FormComponentPanel<Collection<Employee>> {


    @Inject
    EmployeeRepository employeeRepository;

    IModel<Collection<Employee>> employeeModel = CollectionModel.of(employeeRepository::findById);

    public EmployeeReportPanel(String id) {
        super(id);
    }

    public EmployeeReportPanel(String id, IModel<Collection<Employee>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        employeeModel.setObject(getModelObject());
        Component table = table("table");
        add(table);
        EmployeeFieldSelect select = new EmployeeFieldSelect("employee", PersistableModel.of(employeeRepository::findById));
        Form form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                Employee employee = select.getModelObject();
                if (employee != null) {
                    Collection<Employee> employees = employeeModel.getObject();
                    employees.add(employee);
                    employeeModel.setObject(Sets.newHashSet(employees));
                }
            }
        };
        form.add(select);
        add(form);
    }

    private Component table(String id) {
        ISortableDataProvider dataProvider = dataProvider();
        DataTable table = new DataTable(id, columns(), dataProvider, 100);
        table.addTopToolbar(new HeadersToolbar(table, dataProvider));
        table.addBottomToolbar(new NoRecordsToolbar(table, new ResourceModel("datatable.no-records-found")));
        return table;

    }

    List<IColumn<Employee, ?>> columns() {
        List<IColumn<Employee, ?>> columns = Lists.newArrayList();
        columns.add(new LambdaColumn<>(new ResourceModel("employee.id"), "id", Employee::getId));
        columns.add(new LambdaColumn<>(new ResourceModel("employee.rang"), "rang.name", Employee::getRang));
        columns.add(new LambdaColumn<>(new ResourceModel("employee.firstName"), "firstName", Employee::getFirstName));
        columns.add(new LambdaColumn<>(new ResourceModel("employee.middleName"), "middleName", Employee::getMiddleName));
        columns.add(new LambdaColumn<>(new ResourceModel("employee.lastName"), "lastName", Employee::getLastName));
        return columns;
    }

    ISortableDataProvider<Employee, ?> dataProvider() {
        return SortedListModelDataProvider.of(getModel(), (t -> PersistableModel.of(t, employeeRepository::findById)));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        employeeModel.setObject(getModelObject());
    }

    @Override
    public void convertInput() {
        setModelObject(employeeModel.getObject());
    }
}
