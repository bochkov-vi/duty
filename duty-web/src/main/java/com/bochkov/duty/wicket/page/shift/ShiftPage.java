package com.bochkov.duty.wicket.page.shift;

import com.bochkov.duty.jpa.entity.Shift;
import com.bochkov.duty.jpa.repository.ShiftRepository;
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

import java.util.List;

@MountPath("shift")
public class ShiftPage extends EntityPage<Shift, Integer> {
    @SpringBean
    private ShiftRepository repository;

    public ShiftPage() {
    }

    public ShiftPage(Shift entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<Shift, String>> columns() {
        List<IColumn<Shift, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<Shift, String>(new ResourceModel("shift.id"), "id", Shift::getId));

        return list;
    }


    @Override
    protected ShiftRepository getRepository() {
        return repository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Shift> model) {
        return new ShiftInputPanel(id, model);
    }

    @Override
    protected GenericPanel<Shift> createDetailsPanel(String id, IModel<Shift> model) {
        return new DetailsPanel<Shift>(id, getModel(), ImmutableList.of("id"), "shift.");
    }

    @Override
    protected Shift newInstance() {
        return new Shift();
    }
}
