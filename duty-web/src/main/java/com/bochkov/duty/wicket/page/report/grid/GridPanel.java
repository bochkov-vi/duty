package com.bochkov.duty.wicket.page.report.grid;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import com.google.common.collect.Lists;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.List;

public class GridPanel<T> extends GenericPanel<T> {

    @Inject
    EmployeeRepository employeeRepository;
    WebMarkupContainer container = new WebMarkupContainer("container");
    IModel<LocalDate> modelStart;
    IModel<Collection<Employee>> employeesModel;
    IModel<Integer> borderModel = Model.of(3);

    public GridPanel(String id) {
        super(id);
        employeesModel = CollectionModel.of(employeeRepository::findById);
        employeesModel.setObject(employeeRepository.findAll());
        modelStart = Model.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
    }


    public GridPanel(String id, LocalDate dateOfMonth, IModel<Collection<Employee>> employeesModel) {
        super(id);
        this.modelStart = Model.of(dateOfMonth.with(TemporalAdjusters.firstDayOfMonth()));
        this.employeesModel = employeesModel;
    }

    public GridPanel(String id, IModel<T> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);

        IModel<List<LocalDate>> columnModel = LoadableDetachableModel.of(() -> {
            List<LocalDate> result = Lists.newArrayList();
            LocalDate start = modelStart.combineWith(borderModel, (s, b) -> s.minusDays(b)).getObject();
            LocalDate end = modelStart.combineWith(borderModel, (s, b) -> s.with(TemporalAdjusters.lastDayOfMonth()).plusDays(b)).getObject();

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
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
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(GridPanel.class, "GridPanel.js")));

    }
}
