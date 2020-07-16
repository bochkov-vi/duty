package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.wicket.page.employee.EmployeeFieldSelect;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import com.bochkov.wicket.data.provider.SortedListModelDataProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeReportPanel extends FormComponentPanel<Collection<Employee>> {


    @Inject
    EmployeeRepository employeeRepository;
    IModel<Collection<Employee>> employeeModel = CollectionModel.of(employeeRepository::findById);
    Component table = table("table");
    IModel<Employee> selectedEmployee = PersistableModel.of(employeeRepository::findById);
    EmployeeFieldSelect select = new EmployeeFieldSelect("employee", selectedEmployee, employeeModel);

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
        add(table);
        select.setOutputMarkupId(true);
        select.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(table);
                addEmployeeAction();
                target.add(select);
            }
        });
        Form form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                addEmployeeAction();
            }
        };
        form.add(select);
        form.add(new AjaxSubmitLink("add", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                target.add(table, select);

            }
        });
        table.setOutputMarkupId(true);
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
        columns.add(new LambdaColumn<>(new ResourceModel("employee.lastName"), "lastName", Employee::getLastName));
        columns.add(new LambdaColumn<>(new ResourceModel("employee.firstName"), "firstName", Employee::getFirstName));
        columns.add(new LambdaColumn<>(new ResourceModel("employee.middleName"), "middleName", Employee::getMiddleName));
        columns.add(new LambdaColumn<>(new ResourceModel("employee.shiftTypes"), e -> e.getShiftTypes().stream().map(ShiftType::getName).collect(Collectors.joining("; "))));
        columns.add(new HeaderlessColumn<Employee, Object>() {
            @Override
            public void populateItem(Item<ICellPopulator<Employee>> cellItem, String componentId, IModel<Employee> rowModel) {
                cellItem.add(new AjaxLink<Employee>(componentId, rowModel) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        target.add(table);
                        Collection<Employee> employees = employeeModel.getObject();
                        employees.remove(getModelObject());
                    }
                }.setBody(Model.of("<button type='button' class='btn btn-outline-secondary'><i class='fa fa-close'></i></button>")).setEscapeModelStrings(false));
            }
        });
        return columns;
    }

    ISortableDataProvider<Employee, ?> dataProvider() {
        return SortedListModelDataProvider.of(employeeModel, (t -> PersistableModel.of(t, employeeRepository::findById)));
    }

    @Override
    protected void onBeforeRender() {
        employeeModel.setObject(getModelObject());
        super.onBeforeRender();
    }

    @Override
    public void convertInput() {
        setConvertedInput(employeeModel.getObject());
    }

    public void addEmployeeAction() {
        Employee employee = selectedEmployee.getObject();
        if (employee != null) {
            Collection<Employee> employees = employeeModel.getObject();
            employees.add(employee);
            employeeModel.setObject(Sets.newHashSet(employees));
        }
        select.setModelObject(null);
    }
}
