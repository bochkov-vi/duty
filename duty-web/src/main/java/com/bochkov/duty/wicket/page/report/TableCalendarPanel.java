package com.bochkov.duty.wicket.page.report;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.util.ListModel;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Accessors(chain = true)
@Getter
@Setter
public class TableCalendarPanel<R, V> extends GenericPanel<Collection<R>> {

    IModel<List<LocalDate>> datesModel;

    IModel<List<ICellPopulator<R>>> topHeadersModel = new ListModel<>();

    IModel<List<ICellPopulator<R>>> endHeadersModel = new ListModel<>();


    public TableCalendarPanel(String id) {
        this(id, null, LocalDate.now().getMonth());
    }

    public TableCalendarPanel(String id, IModel<Collection<R>> model) {
        this(id, model, LocalDate.now().getMonth());
    }

    public TableCalendarPanel(String id, IModel<Collection<R>> model, IModel<LocalDate> start, IModel<LocalDate> end) {
        super(id, model);
        datesModel = start.combineWith(end, this::createData);
    }

    public TableCalendarPanel(String id, IModel<Collection<R>> model, Month month) {
        super(id, model);
        datesModel = LoadableDetachableModel.of(() -> createData(month));
    }

    List<LocalDate> createData(LocalDate start, LocalDate end) {
        List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end))
                .collect(Collectors.toList());
        return dates;
    }

    List<LocalDate> createData(Month month) {
        LocalDate start = LocalDate.now().with(month).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
        return createData(start, end);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
        ListView topHeaders = new ListView<ICellPopulator<R>>("top-headers", topHeadersModel) {
            @Override
            protected void populateItem(ListItem<ICellPopulator<R>> item) {

            }
        };
        add(topHeaders);

        ListView endHeaders = new ListView<ICellPopulator<R>>("end-headers", endHeadersModel) {
            @Override
            protected void populateItem(ListItem<ICellPopulator<R>> item) {

            }
        };
        add(endHeaders);

        ListView cellHeaders = new ListView("cell-headers", datesModel) {
            @Override
            protected void populateItem(ListItem item) {

            }
        };
        add(cellHeaders);




    }

    @Override
    protected void onDetach() {
        super.onDetach();
        datesModel.detach();
        topHeadersModel.detach();
        endHeadersModel.detach();
    }


    public void populateCellItem(Item<ICellPopulator<R>> cellItem, String componentId, IModel<R> rowModel, LocalDate date) {

    }

    public void populateHeaderItem(Item<ICellPopulator<R>> cellItem, String componentId, IModel<R> rowModel, LocalDate date) {

    }
}
