package com.bochkov.duty.wicket.page.day;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.wicket.base.DetailsPanel;
import com.bochkov.duty.wicket.base.EntityPage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDate;
import java.util.List;

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
    protected List<IColumn<Day, String>> columns() {
        List<IColumn<Day, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<Day, String>(new ResourceModel("day.id"), "id", Day::getId));

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
}
