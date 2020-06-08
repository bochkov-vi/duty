package com.bochkov.duty.wicket.page.day;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.wicket.base.DetailsPanel;
import com.bochkov.duty.wicket.base.EntityPage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Sort;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@MountPath("day")
public class DayPage extends EntityPage<Day, LocalDate> {


    @SpringBean
    private DayRepository repository;

    public DayPage() {
    }

    public DayPage(Day entity) {
        super(entity);
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        tableContainer.add(new ClassAttributeModifier() {
            @Override
            protected Set<String> update(Set<String> oldClasses) {
                oldClasses.remove("container");
                oldClasses.add("container-fluid");
                return oldClasses;
            }
        });
    }

    @Override
    protected List<IColumn<Day, String>> columns() {
        List<IColumn<Day, String>> list = Lists.newArrayList();
        //list.add(new LambdaColumn<Day, String>(new ResourceModel("day.id"), "id", Day::getId));
        list.addAll(reflectiveColumns(Day.class, "day.", "id", "weekend", "next", "prev", "daysToWeekend", "daysFromWeekend", "totalDuration", "shiftType.name", "weekIndex", "dayIndex", "dayOfWeek"));
        return list;
    }


    @Override
    protected DayRepository getRepository() {
        return repository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Day> model) {
        return new DayInputPanel(id, model);
    }

    @Override
    protected GenericPanel<Day> createDetailsPanel(String id, IModel<Day> model) {
        return new DetailsPanel<Day>(id, getModel(), ImmutableList.of("id"), "day.");
    }

    @Override
    protected Day newInstance() {
        return new Day();
    }

    @Override
    public Sort createSort() {
        return Sort.by(Sort.Direction.ASC, "id");
    }

    protected IColumn<Day, String> createPropertyColumn(String pname, boolean enableSort, Class propertyClass, String resourcePrefix) {
        if ("totalDuration".equalsIgnoreCase(pname)) {
            return new LambdaColumn<Day, String>(new ResourceModel("day.totalDuration"), day -> DurationFormatUtils.formatDuration(day.getTotalDuration().toMillis(), "HH:mm"));
        }
        return super.createPropertyColumn(pname, enableSort, propertyClass, resourcePrefix);
    }

}
