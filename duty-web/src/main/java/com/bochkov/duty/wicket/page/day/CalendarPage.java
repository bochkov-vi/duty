package com.bochkov.duty.wicket.page.day;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@MountPath("calendar")
@Log4j
public class CalendarPage extends BootstrapPage<Month> {

    @SpringBean
    DayRepository dayRepository;

    WebMarkupContainer container = new WebMarkupContainer("container");

    public CalendarPage() {
        setModel(Model.of(LocalDate.now().getMonth()));
    }

    public CalendarPage(IModel<Month> model) {
        super(model);
    }

    public CalendarPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(container);
        container.setOutputMarkupId(true);
        LocalDate start = getModel().map(month -> LocalDate.now().withMonth(month.getValue()).with(TemporalAdjusters.firstDayOfMonth()).with(DayOfWeek.MONDAY)).getObject();
        LocalDate end = getModel().map(month -> LocalDate.now().withMonth(month.getValue()).with(TemporalAdjusters.lastDayOfMonth()).with(DayOfWeek.SUNDAY)).getObject();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        List<Integer> weekNumbers = Lists.newArrayList();
        for (int weekNumber = start.get(weekFields.weekOfWeekBasedYear()); weekNumber <= end.get(weekFields.weekOfWeekBasedYear()); weekNumber++) {
            weekNumbers.add(weekNumber);
        }
        List<DayOfWeek> dayOfWeeks = Lists.newArrayList(DayOfWeek.values());
        ListView<DayOfWeek> heads = new ListView<DayOfWeek>("heads", dayOfWeeks) {
            @Override
            protected void populateItem(ListItem<DayOfWeek> item) {
                item.add(new Label("head", item.getModel().map(dayOfWeek -> LocalDate.now().with(dayOfWeek).format(DateTimeFormatter.ofPattern("EE")))));
            }
        };
        container.add(heads);
        DataGridView<Integer> rows = new DataGridView<Integer>("rows",
                dayOfWeeks.stream().map(this::createCellPopulator).collect(Collectors.toList()),
                new ListDataProvider<Integer>(weekNumbers));
        container.add(rows);
    }

    ICellPopulator<Integer> createCellPopulator(DayOfWeek dayOfWeek) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return new ICellPopulator<Integer>() {
            @Override
            public void populateItem(Item<ICellPopulator<Integer>> cellItem, String componentId, IModel<Integer> rowModel) {
                IModel<Day> dayIModel = LoadableDetachableModel.of(
                        () -> rowModel.map(weekNumber -> LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber.longValue()).with(dayOfWeek)).map(ld -> dayRepository.findOrCreate(ld)).getObject()
                );
                WebMarkupContainer cell = new WebMarkupContainer(componentId);
                cellItem.add(cell);
                cell.add(new Label("daysFromWeekend", dayIModel.map(Day::getDaysFromWeekend)));
                cell.add(new Label("daysToWeekend", dayIModel.map(Day::getDaysToWeekend)));
                cell.add(new AjaxLink<Day>("link", dayIModel) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        log.debug(getModel().getObject());
                        Day day = getModelObject();
                        day.setWeekend(!day.isWeekend());
                        dayRepository.safeSave(day);
                        target.add(container);
                    }
                }.add(new Label("label", dayIModel.map(Day::getId).map(localDate -> localDate.format(DateTimeFormatter.ofPattern("dd"))))));
                cellItem.add(new ClassAttributeModifier() {
                    @Override
                    protected Set<String> update(Set<String> oldClasses) {
                        if (dayIModel.map(Day::isWeekend).getObject()) {
                            oldClasses.add("bg-danger");
                        }
                        return oldClasses;
                    }
                });
            }

            @Override
            public void detach() {

            }
        };
    }
}
