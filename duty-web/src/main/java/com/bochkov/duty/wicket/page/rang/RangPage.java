package com.bochkov.duty.wicket.page.rang;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
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

@MountPath("rang")
public class RangPage extends EntityPage<Rang, Short> {
    @SpringBean
    private RangRepository rangRepository;

    public RangPage() {
    }

    public RangPage(Rang entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<Rang, String>> columns() {
        List<IColumn<Rang, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<Rang, String>(new ResourceModel("rang.id"), "id", Rang::getId));
        list.add(new LambdaColumn<Rang, String>(new ResourceModel("rang.name"), "name", Rang::getName));
        list.add(new LambdaColumn<Rang, String>(new ResourceModel("rang.fullName"), "fullName", Rang::getFullName));
        return list;
    }


    @Override
    protected RangRepository getRepository() {
        return rangRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Rang> model) {
        return new RangInputPanel(id, model);
    }

    @Override
    protected GenericPanel<Rang> createDetailsPanel(String id, IModel<Rang> model) {
        return new DetailsPanel<Rang>(id, getModel(), ImmutableList.of("id", "name", "fullName"), "rang.");
    }

    @Override
    protected Rang newInstance() {
        return new Rang();
    }
}
