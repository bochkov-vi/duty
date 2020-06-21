package com.bochkov.duty.wicket.page.schedule;

import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.google.common.collect.Lists;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@MountPath("test/calendar")
public class CalendarTestPage extends BootstrapPage<List<Integer>> {

    @Inject
    DayRepository dayRepository;

    IModel<LocalDate> dateFromModel;

    IModel<LocalDate> dateToModel;

    public CalendarTestPage() {
        this(LocalDate.now().getMonth());
    }

    public CalendarTestPage(IModel<LocalDate> dateFromModel, IModel<LocalDate> dateToModel) {
        this.dateFromModel = dateFromModel;
        this.dateToModel = dateToModel;
    }

    public CalendarTestPage(LocalDate dateFrom, LocalDate dateTo) {
        this(Model.of(dateFrom), Model.of(dateTo));
    }

    public CalendarTestPage(Month month) {
        this(LocalDate.now().with(month).with(TemporalAdjusters.firstDayOfMonth()), LocalDate.now().with(month).with(TemporalAdjusters.lastDayOfMonth()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setModel(LoadableDetachableModel.of(() -> {
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            List<Integer> list = dateFromModel.combineWith(dateToModel, (s, e) -> IntStream.range(s.get(weekFields.weekOfWeekBasedYear()), e.get(weekFields.weekOfWeekBasedYear())).distinct().iterator()).map(Lists::newArrayList).getObject();
            return list;
        }));

        ListView heads1 = new ListView<DayOfWeek>("heads1", Lists.newArrayList(DayOfWeek.values())) {
            @Override
            protected void populateItem(ListItem<DayOfWeek> item) {
                item.add(new Label("cell", item.getModel().map(dow -> LocalDate.now().with(dow).format(DateTimeFormatter.ofPattern("EE")))));
            }
        };
        add(heads1);
        ListView rows = new ListView<Integer>("rows", getModel()) {
            @Override
            protected void populateItem(ListItem<Integer> rowItem) {

                List<DayOfWeek> daysOfWeek = Lists.newArrayList(DayOfWeek.values());
                ListView<DayOfWeek> cells = new ListView<DayOfWeek>("cells", daysOfWeek) {
                    @Override
                    protected void populateItem(ListItem<DayOfWeek> cellItem) {
                        IModel<LocalDate> dateModel = rowItem.getModel().combineWith(cellItem.getModel(), (weekNumber, dayOfWeek) -> LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber.longValue()).with(dayOfWeek));
                        cellItem.add(populateCell("cell", dateModel));
                    }
                };
                rowItem.add(cells);
            }
        };
        add(rows);
    }

    public Component populateCell(String compId, IModel<LocalDate> dateModel) {
        Fragment fragment = new Fragment(compId, "cell-panel", getPage());
        fragment.add(new Label("label", dateModel.map(d -> d.format(DateTimeFormatter.ofPattern("dd")))));
        return fragment;
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        this.dateFromModel.detach();
        this.dateToModel.detach();
    }
}
