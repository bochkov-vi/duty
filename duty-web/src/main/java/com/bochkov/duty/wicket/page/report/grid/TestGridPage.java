package com.bochkov.duty.wicket.page.report.grid;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.jpa.model.CollectionModel;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;

@MountPath("test-grid")
@Slf4j
public class TestGridPage<R, C, V> extends BootstrapPage<Table<R, C, V>> {

    @Inject
    EmployeeRepository employeeRepository;

    WebMarkupContainer container = new WebMarkupContainer("container");

    IModel<LocalDate> modelStart;

    IModel<Collection<Employee>> employeesModel;

    IModel<Integer> borderModel = Model.of(3);

    public TestGridPage() {
        employeesModel = CollectionModel.of(employeeRepository::findById);
        employeesModel.setObject(employeeRepository.findAll());
        modelStart = Model.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
    }

    public TestGridPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new GridPanel<Void>("grid", LocalDate.now(), CollectionModel.of(employeeRepository::findById, employeeRepository.findAll())));


    }


}
