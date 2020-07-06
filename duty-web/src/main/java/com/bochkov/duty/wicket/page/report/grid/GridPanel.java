package com.bochkov.duty.wicket.page.report.grid;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import com.google.common.collect.Lists;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.StyleAttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
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
import java.util.*;

public class GridPanel<T> extends GenericPanel<T> {

    @Inject
    EmployeeRepository employeeRepository;
    @Inject
    DayRepository dayRepository;

    WebMarkupContainer container = new WebMarkupContainer("container");
    IModel<LocalDate> modelStart;
    IModel<LocalDate> modelEnd;
    IModel<Collection<Employee>> employeesModel;
    IModel<Integer> borderModel = Model.of(3);
    String firstColumnDateId = null;

    public GridPanel(String id) {
        super(id);
        employeesModel = CollectionModel.of(employeeRepository::findById);
        employeesModel.setObject(employeeRepository.findAll());
        modelStart = Model.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
        modelEnd = LoadableDetachableModel.of(() -> modelStart.getObject().with(TemporalAdjusters.lastDayOfMonth()));
    }


    public GridPanel(String id, LocalDate dateOfMonth, IModel<Collection<Employee>> employeesModel) {
        super(id);
        this.modelStart = Model.of(dateOfMonth.with(TemporalAdjusters.firstDayOfMonth()));
        this.employeesModel = employeesModel;
        modelEnd = LoadableDetachableModel.of(() -> modelStart.getObject().with(TemporalAdjusters.lastDayOfMonth()));
    }

    public GridPanel(String id, IModel<T> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);

        IModel<List<LocalDate>> columnsModel = LoadableDetachableModel.of(() -> {
            List<LocalDate> result = Lists.newArrayList();
            LocalDate start = modelStart.combineWith(borderModel, (date, b) -> date.minusDays(b)).getObject();
            LocalDate end = modelEnd.combineWith(borderModel, (date, b) -> date.plusDays(b)).getObject();

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                result.add(date);
            }
            return result;
        });
        IModel<List<Employee>> rowModel = LoadableDetachableModel.of(() -> employeesModel.map(Lists::newArrayList).getObject());
        ListView<Employee> primaryColumn = new ListView<Employee>("primary-column", rowModel) {
            @Override
            protected void populateItem(ListItem<Employee> cellItem) {
                cellItem.add(new Label("cell", cellItem.getModel().map(Employee::toString)));
                cellItem.add(new AttributeAppender("data-employee-id", cellItem.getModel().map(Employee::getId)));
            }
        };
        add(primaryColumn);


        ListView<LocalDate> columns = new ListView<LocalDate>("columns", columnsModel) {
            @Override
            protected void populateItem(ListItem<LocalDate> columnItem) {

                IModel<Day> dayModel = LoadableDetachableModel.of(() -> dayRepository.findOrCreate(columnItem.getModelObject()));

                if (dayModel.isPresent().getObject()) {
                    columnItem.add(new StyleAttributeModifier() {
                        @Override
                        protected Map<String, String> update(Map<String, String> oldStyles) {
                            switch (dayModel.map(Day::getDaysToWeekend).getObject()) {
                                case 0: {
                                    oldStyles.put("background", "pink");
                                    break;
                                }
                                case 1: {
                                    oldStyles.put("background", "rgb(255,228,181)");
                                    break;
                                }
                                case 2: {
                                    oldStyles.put("background", "rgb(255,255,224)");
                                    break;
                                }
                            }
                            return oldStyles;
                        }
                    });
                }
                boolean enabled = columnItem.getModel().combineWith(modelStart, (columnDate, start) -> !columnDate.isBefore(start)).getObject()
                        && columnItem.getModel().combineWith(modelEnd, (columnDate, end) -> !columnDate.isAfter(end)).getObject();
                ListView<Employee> cells = new ListView<Employee>("cells", rowModel) {
                    @Override
                    protected void populateItem(ListItem<Employee> cellItem) {
                        pupulateCellItem(cellItem, "cell", columnItem.getModel(), cellItem.getModel());
                        cellItem.add(new AttributeAppender("data-employee-id", cellItem.getModel().map(Employee::getId)));
                        if (!enabled) {
                            cellItem.add(new StyleAttributeModifier() {
                                @Override
                                protected Map<String, String> update(Map<String, String> oldStyles) {
                                    oldStyles.put("background", " rgba(0,0,0,0.25)");
                                    return oldStyles;
                                }
                            });
                        }
                    }
                };

                columnItem.setEnabled(enabled);
                IModel<String> ddMmModel = columnItem.getModel().map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM")));
                if (!enabled) {
                    columnItem.add(new AttributeAppender("disabled", true));
                    columnItem.add(new ClassAttributeModifier() {
                        @Override
                        protected Set<String> update(Set<String> oldClasses) {
                            oldClasses.add("disabled");
                            return oldClasses;
                        }
                    });
                   /* columnItem.add(new StyleAttributeModifier() {
                        @Override
                        protected Map<String, String> update(Map<String, String> oldStyles) {
                            oldStyles.put("background", " rgb(119,136,153)");
                            return oldStyles;
                        }
                    });*/
                } else if (firstColumnDateId == null) {
                    firstColumnDateId = ddMmModel.getObject();
                }
                if (dayModel.map(Day::getId).map(date -> Objects.equals(date, LocalDate.now())).getObject()) {
                    firstColumnDateId = ddMmModel.getObject();
                }


                columnItem.add(cells);

                columnItem.add(new Label("date-cell", ddMmModel));
                columnItem.add(new AttributeAppender("data-date-id", ddMmModel));
                columnItem.add(new Label("day-of-week-cell", columnItem.getModel().map(d -> d.format(DateTimeFormatter.ofPattern("EE")))));

            }
        };
        container.add(columns);
        add(container);


    }

    public void pupulateCellItem(ListItem<Employee> cellItem, String compId, IModel<LocalDate> dateModel, IModel<Employee> employeeModel) {
        Component component = new Label(compId, "-");
        cellItem.add(component);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(GridPanel.class, "GridPanel.js")));
        response.render(OnDomReadyHeaderItem.forScript("enableCellHover()"));
        if (firstColumnDateId != null) {
            response.render(OnDomReadyHeaderItem.forScript(String.format("scrollToDateColumn('%s','%s')", container.getMarkupId(), firstColumnDateId)));
        }
    }
}
