package com.bochkov.duty.wicket.page.report;

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
import org.apache.wicket.model.Model;
import org.danekja.java.util.function.serializable.SerializableFunction;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Accessors(chain = true)
public abstract class ShiftGridPanel<T> extends GenericPanel<List<T>> {

    @Getter
    @Setter
    LocalDate dateFrom;

    @Getter
    @Setter
    LocalDate dateTo;

    @Getter
    @Setter
    private List<SerializableFunction<Integer, ? extends IColumn<T, ?>>> firstColmnCreators = Lists.newArrayList();


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
        ISortableDataProvider<T, String> provider = provider();
        List<? extends IColumn<T, ?>> columns = Lists.newArrayList();
        int i = 0;

        for (Function<Integer, ? extends IColumn<T, ?>> function : firstColmnCreators) {
            IColumn<T, ?> column =  function.apply(i++);
            columns.add(column);
        }
        columns.addAll(columns(dateFrom, dateTo));
        DataTable<T, String> table = new DataTable<T, String>("table", columns, provider, 100);
        table.addTopToolbar(new HeadersToolbar<>(table, provider));
        table.addTopToolbar(new DayToolbar(table, "EE"));
        add(table);
    }

    List<LocalDate> createData(LocalDate start, LocalDate end) {
        List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end))
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


    protected <C extends IColumn<T, String>> List<C> columns(LocalDate d1, LocalDate d2) {
        List<C> columns = Lists.newArrayList();
        createData(d1, d2).stream().map(this::createColumn).forEach(col -> {
            C column = (C) col;
            columns.add(column);
        });
        return columns;
    }


    protected IColumn<T, String> createColumn(LocalDate date) {
        return new DayColumn<T>(Model.of(date).map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM"))), date) {
            @Override
            public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
                ShiftGridPanel.this.populateItem(cellItem, componentId, rowModel, date);
            }
        };
    }

    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel, LocalDate date) {
        cellItem.add(new Label(componentId, Model.of(date).map(d -> d.format(DateTimeFormatter.ofPattern("dd")))));
    }

    public abstract IModel<T> model(T object);

    public static abstract class DayColumn<T> extends AbstractColumn<T, String> implements DayToolbar.IDateAware {

        LocalDate date;

        public DayColumn(IModel<String> displayModel, LocalDate date) {
            super(displayModel);
            this.date = date;
        }


        @Override
        public LocalDate getDate() {
            return date;
        }

    }

}
