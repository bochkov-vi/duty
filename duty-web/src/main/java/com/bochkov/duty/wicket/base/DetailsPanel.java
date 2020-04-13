package com.bochkov.duty.wicket.base;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.List;

public class DetailsPanel<T> extends GenericPanel<T> {

    List<String> properties;

    String prefix;

    public DetailsPanel(String id, IModel<T> model, List<String> properties, String prefix) {
        super(id, model);
        this.properties = properties;
        this.prefix = prefix;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        ListView<String> view = new ListView<String>("row", properties) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("label", new ResourceModel(prefix + item.getModelObject())));
                item.add(new Label("value", new ComponentPropertyModel<>(item.getModelObject()).wrapOnAssignment(DetailsPanel.this)));
            }
        };
        add(view);
    }
}