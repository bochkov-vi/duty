package com.bochkov.duty.wicket.page.day.calendar;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.wicket.data.provider.ListModelDataProvider;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j
public class CalendarPanel extends GenericPanel<Month> {

    @SpringBean
    DayRepository dayRepository;

    public CalendarPanel(String id, IModel<Month> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);
        setOutputMarkupId(true);

        add(new Label("month-name", getModel().map(m -> m.getDisplayName(TextStyle.FULL_STANDALONE, Session.get().getLocale()))));
        IModel<List<Integer>> rowModel = LoadableDetachableModel.of(() -> {
            LocalDate start = getModel().map(month -> LocalDate.now().withMonth(month.getValue()).with(TemporalAdjusters.firstDayOfMonth()).with(DayOfWeek.MONDAY)).getObject();
            LocalDate end = getModel().map(month -> LocalDate.now().withMonth(month.getValue()).with(TemporalAdjusters.lastDayOfMonth()).with(DayOfWeek.SUNDAY)).getObject();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            List<Integer> weekNumbers = Lists.newArrayList();
            for (int weekNumber = start.get(weekFields.weekOfWeekBasedYear()); weekNumber <= end.get(weekFields.weekOfWeekBasedYear()); weekNumber++) {
                weekNumbers.add(weekNumber);
            }
            return weekNumbers;
        });

        add(new AjaxLink<Month>("month+", getModel()) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(CalendarPanel.this);
                setModelObject(getModel().map(m -> m.plus(1)).getObject());
            }
        });
        add(new AjaxLink<Month>("month-", getModel()) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(CalendarPanel.this);
                setModelObject(getModel().map(m -> m.minus(1)).getObject());
            }
        });

        List<DayOfWeek> dayOfWeeks = Lists.newArrayList(DayOfWeek.values());
        ListView<DayOfWeek> heads = new ListView<DayOfWeek>("heads", dayOfWeeks) {
            @Override
            protected void populateItem(ListItem<DayOfWeek> item) {
                item.add(new Label("head", item.getModel().map(dayOfWeek -> LocalDate.now().with(dayOfWeek).format(DateTimeFormatter.ofPattern("EE")))));
            }
        };
        add(heads);
        DataGridView<Integer> rows = new DataGridView<Integer>("rows",
                dayOfWeeks.stream().map(this::createCellPopulator).collect(Collectors.toList()),
                new ListModelDataProvider<Integer>(rowModel) {
                    @Override
                    public IModel<Integer> model(Integer object) {
                        return Model.of(object);
                    }
                });
        add(rows);
    }

    ICellPopulator<Integer> createCellPopulator(DayOfWeek dayOfWeek) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return new ICellPopulator<Integer>() {
            @Override
            public void populateItem(Item<ICellPopulator<Integer>> cellItem, String componentId, IModel<Integer> rowModel) {
                IModel<Day> dayIModel = LoadableDetachableModel.of(
                        () -> rowModel.map(weekNumber -> LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber.longValue()).with(dayOfWeek)).map(ld -> dayRepository.findOrCreate(ld)).getObject()
                );


                WebMarkupContainer cell = new WebMarkupContainer(componentId) {
                    @Override
                    protected void onConfigure() {
                        super.onConfigure();
                        boolean onRange = dayIModel.map(Day::getId).combineWith(getModel(), (d, m) -> d.getMonth().equals(m)).getObject();
                        setVisible(onRange);
                    }
                };
                cellItem.add(cell);
                cell.add(new Label("daysFromWeekend", dayIModel.map(Day::getDaysFromWeekend)) {
                    @Override
                    public boolean isVisible() {
                        return dayIModel.map(Day::getDaysFromWeekend).map(v -> v > 0).getObject();
                    }
                });
                cell.add(new Label("daysToWeekend", dayIModel.map(Day::getDaysToWeekend)) {
                    @Override
                    public boolean isVisible() {
                        return dayIModel.map(Day::getDaysToWeekend).map(v -> v > 0).getObject();
                    }
                });
                cell.add(new AjaxLink<Day>("link", dayIModel) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        log.debug(getModel().getObject());
                        Day day = getModelObject();
                        day.setWeekend(!day.isWeekend());
                        day = dayRepository.safeSave(day);
                        target.add(CalendarPanel.this);
                    }
                }.add(new Label("label", dayIModel.map(Day::getId).map(localDate -> localDate.format(DateTimeFormatter.ofPattern("dd.MM"))))
                ));
                AjaxLink weekend = new AjaxLink<Day>("weekend", dayIModel) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        target.add(CalendarPanel.this);
                        Day day = dayIModel.getObject();
                        day.setWeekend(!day.isWeekend());
                        if (day.isShortened() && day.isWeekend()) {
                            day.setShortened(false);
                        }
                        day = dayRepository.safeSave(day);
                        log.debug(day);
                    }
                };
                weekend.add(new ClassAttributeModifier() {
                    @Override
                    protected Set<String> update(Set<String> oldClasses) {
                        if (dayIModel.map(Day::isWeekend).getObject()) {
                            oldClasses.add("fa-thumbs-up");
                        } else {
                            oldClasses.add("fa-thumbs-o-up");
                        }
                        return oldClasses;
                    }
                });
                weekend.setOutputMarkupId(true);
                cell.add(weekend);
                cell.setOutputMarkupId(true);
                AjaxLink shortened = new AjaxLink<Day>("shortened", dayIModel) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        target.add(CalendarPanel.this);
                        Day day = dayIModel.getObject();
                        day.setShortened(!day.isShortened());
                        if (day.isShortened() && day.isWeekend()) {
                            day.setWeekend(false);

                        }
                        day = dayRepository.safeSave(day);
                        log.debug(day);
                    }
                };
                shortened.setOutputMarkupId(true);
                cell.add(shortened);
                shortened.add(new ClassAttributeModifier() {
                    @Override
                    protected Set<String> update(Set<String> oldClasses) {
                        if (dayIModel.map(Day::isShortened).getObject()) {
                            oldClasses.add("fa-hand-grab-o");
                        } else {
                            oldClasses.add("fa-hand-o-up");
                        }
                        return oldClasses;
                    }
                });
                cell.add(new Label("usageTime", dayIModel.map(Day::getTotalDuration).map(d -> DurationFormatUtils.formatDuration(d.toMillis(), "H:m"))));
                cell.add(new ClassAttributeModifier() {
                    @Override
                    protected Set<String> update(Set<String> oldClasses) {
                        if (dayIModel.map(Day::isWeekend).getObject()) {
                            oldClasses.add("text-danger");
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
