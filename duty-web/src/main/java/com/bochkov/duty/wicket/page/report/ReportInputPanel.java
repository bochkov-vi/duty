package com.bochkov.duty.wicket.page.report;

import com.bochkov.bootstrap.tempusdominus.LocalDateField;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.wicket.page.employee.EmployeeFieldSelect;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.springframework.data.domain.Persistable;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReportInputPanel extends GenericPanel<Report> {

    @Inject
    EmployeeRepository employeeRepository;

    FormComponent<Integer> idField = new TextField<>("id");
    FormComponent<LocalDate> dateField = new LocalDateField("date", getString("datePattern"));
    FormComponent<LocalDate> dateFromField = new LocalDateField("dateFrom", getString("datePattern"));
    FormComponent<LocalDate> dateToField = new LocalDateField("dateTo", getString("datePattern"));
    FormComponent<Employee> chiefField = new EmployeeFieldSelect("chief");
    FormComponent<Employee> executorField = new EmployeeFieldSelect("executor");
    FormComponent<Integer> titleField = new TextArea<>("title");
    FormComponent<Integer> dateTitleField = new TextField<>("dateTitle");
    FormComponent<Integer> genitiveDepartmentField = new TextField<>("genitiveDepartment");


    public ReportInputPanel(String id, IModel<Report> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(idField, dateField, dateFromField, dateToField, chiefField, executorField, titleField, dateTitleField, genitiveDepartmentField);
        IModel<Collection<Employee>> employees = LambdaModel.of(getModel(), Report::getEmployees, (SerializableBiConsumer<Report, Collection<Employee>>) (report, employees1) -> report.setEmployees(employees1 != null ? Sets.newHashSet(employees1) : Sets.newHashSet()));
        EmployeeReportPanel employeeReportPanel = new EmployeeReportPanel("employees", employees);
        employeeReportPanel.setOutputMarkupId(true);
        add(employeeReportPanel);
        IModel<Collection<ShiftType>> shiftTypes = LambdaModel.of(getModel(), Report::getShiftTypes, (SerializableBiConsumer<Report, Collection<ShiftType>>) (report, shiftTypes1) -> report.setShiftTypes(shiftTypes1 != null ? Sets.newHashSet(shiftTypes1) : Sets.newHashSet()));
        add(new ShiftTypeReportPanel("shiftTypes", shiftTypes));
        add(new AjaxLink<Void>("btn-add-empl-by-shift-type") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                List<Employee> findedEmployees = employeeRepository.findByShiftTypesIn(shiftTypes.getObject());
                Collection<Employee> resultEmployees = employees.getObject();
                resultEmployees = Streams.concat(findedEmployees.stream(), resultEmployees.stream()).collect(Collectors.toSet());
                employees.setObject(resultEmployees);
                target.add(employeeReportPanel);
            }
        });
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        idField.setEnabled(getModel().map(Persistable::isNew).orElse(false).getObject());
    }

}
