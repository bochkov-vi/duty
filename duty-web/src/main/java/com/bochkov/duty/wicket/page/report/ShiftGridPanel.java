package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.provider.SortedListModelDataProvider;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Accessors(chain = true)
public abstract class ShiftGridPanel<T> extends GenericPanel<List<T>> {

    @Inject
    DayRepository dayRepository;

    @Getter
    @Setter
    LocalDate dateFrom;

    @Getter
    @Setter
    LocalDate dateTo;

    @Getter
    @Setter
    private List<? extends IColumn<T, ?>> firstColumns = Lists.newArrayList();


    public ShiftGridPanel(String id, IModel<List<T>> model, LocalDate dateFrom, LocalDate dateTo) {
        super(id, model);
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public ShiftGridPanel(String id, IModel<List<T>> model, IModel<LocalDate> dateFrom, IModel<LocalDate> dateTo) {
        super(id, model);
        this.dateFrom = dateFrom.getObject();
        this.dateTo = dateTo.getObject();
    }

    public ShiftGridPanel(String id, IModel<List<T>> model, Month month) {
        super(id, model);
        dateFrom = LocalDate.now().with(month).with(TemporalAdjusters.firstDayOfMonth());
        dateTo = dateFrom.with(TemporalAdjusters.lastDayOfMonth());

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        ISortableDataProvider<T, ?> provider = provider();
        List<? extends IColumn<T, ?>> columns = Lists.newArrayList(this.firstColumns);

        columns.addAll(columns(dateFrom, dateTo));
        DataTable table = new DataTable("table", columns, provider, 100);
        table.addTopToolbar(new HeadersToolbar(table, provider));
        table.addTopToolbar(new DayToolbar(table, "EE"));
        add(table);
    }

    List<LocalDate> createData(LocalDate start, LocalDate end) {
        List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end.plusDays(1)))
                .collect(Collectors.toList());
        return dates;
    }


    ISortableDataProvider<T, String> provider() {
        ISortableDataProvider<T, String> provider = new SortedListModelDataProvider<T>(getModel()) {
            @Override
            public IModel<T> model(T object) {
                return ShiftGridPanel.this.model(object);
            }

        };

        return provider;
    }


    protected <C extends IColumn> List<C> columns(LocalDate d1, LocalDate d2) {
        List<C> columns = Lists.newArrayList();
        createData(d1, d2).stream().map(this::createColumn).forEach(col -> {
            C column = (C) col;
            columns.add(column);
        });
        return columns;
    }

    protected DayColumn createColumn(LocalDate date) {
        DayColumn column = createColumn(PersistableModel.of(date, id -> Optional.of(dayRepository.findOrCreate(id))));
        return column;
    }

    protected DayColumn createColumn(IModel<Day> day) {
        DayColumn column = new DayColumn<T>(day.map(Day::getId).map(d -> d.format(DateTimeFormatter.ofPattern("dd"))), day) {
            @Override
            public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
                ShiftGridPanel.this.populateItem(cellItem, componentId, rowModel, day);
            }

            @Override
            public String getCssClass() {
                return ShiftGridPanel.this.getCssClass(this.day);
            }
        };

        return column;
    }

    public String getCssClass(IModel<Day> day) {
        return day.map(Day::isWeekend).orElse(false).getObject() ? "bg-danger" :null;
    }

    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel, IModel<Day> day) {
        cellItem.add(new Label(componentId, day.map(Day::getId).map(d -> d.format(DateTimeFormatter.ofPattern("dd")))));
    }

    public abstract IModel<T> model(T object);

    public static abstract class DayColumn<T> extends AbstractColumn<T, String> implements DayToolbar.IDayAware {

        IModel<Day> day;

        public DayColumn(IModel<String> displayModel, IModel<Day> day) {
            super(displayModel);
            this.day = day;
        }


        @Override
        public IModel<Day> getDay() {
            return day;
        }

    }

}
