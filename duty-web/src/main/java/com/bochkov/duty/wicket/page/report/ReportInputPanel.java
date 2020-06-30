package com.bochkov.duty.wicket.page.report;

import com.bochkov.bootstrap.tempusdominus.LocalDateField;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.wicket.page.employee.EmployeeFieldSelect;
import com.google.common.collect.Sets;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.util.Collection;

public class ReportInputPanel extends GenericPanel<Report> {

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
        IModel<Collection<Employee>> emmployees = LambdaModel.of(getModel(), Report::getEmployees, (SerializableBiConsumer<Report, Collection<Employee>>) (report, employees) -> report.setEmployees(Sets.newHashSet(employees)));
        add(new EmployeeReportPanel("employees", emmployees));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        idField.setEnabled(getModel().map(Persistable::isNew).orElse(false).getObject());
    }

}
