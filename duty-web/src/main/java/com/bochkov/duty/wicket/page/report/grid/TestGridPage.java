package com.bochkov.duty.wicket.page.report.grid;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import lombok.extern.log4j.Log4j;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.List;

@MountPath("test-grid")
@Log4j
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

        setOutputMarkupId(true);

        IModel<List<LocalDate>> columnModel = LoadableDetachableModel.of(() -> {
            List<LocalDate> result = Lists.newArrayList();
            LocalDate start = modelStart.combineWith(borderModel, (s, b) -> s.minusDays(b)).getObject();
            LocalDate end = modelStart.combineWith(borderModel, (s, b) -> s.with(TemporalAdjusters.lastDayOfMonth()).plusDays(b)).getObject();

            for (LocalDate date = start; !date.isAfter(end); date=date.plusDays(1)) {
                result.add(date);
            }
            return result;
        });
        IModel<List<Employee>> rowModel = LoadableDetachableModel.of(() -> employeesModel.map(Lists::newArrayList).getObject());
        ListView<Employee> primaryColumn = new ListView<Employee>("primary-column", rowModel) {
            @Override
            protected void populateItem(ListItem<Employee> item) {
                item.add(new Label("cell", item.getModel().map(Employee::toString)));
            }
        };
        add(primaryColumn);


        ListView<LocalDate> columns = new ListView<LocalDate>("columns", columnModel) {
            @Override
            protected void populateItem(ListItem<LocalDate> columnItem) {
                ListView<Employee> cells = new ListView<Employee>("cells", rowModel) {
                    @Override
                    protected void populateItem(ListItem<Employee> cellItem) {
                        cellItem.add(new Label("cell", "-"));
                    }
                };
                columnItem.add(cells);
                columnItem.add(new Label("date-cell", columnItem.getModel().map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM")))));
                columnItem.add(new Label("day-of-week-cell", columnItem.getModel().map(d -> d.format(DateTimeFormatter.ofPattern("EE")))));

            }
        };
        container.add(columns);
        add(container);


    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(TestGridPage.class, "GridPanel.js")));

    }
}
