package com.bochkov.duty.wicket.page.report.grid;

import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.duty.wicket.page.shifttype.ShiftTypeLabel;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.List;

public class ShiftTypeEditor extends GenericPanel<ShiftType> {
    @Inject
    ShiftTypeRepository shiftTypeRepository;

    Form<ShiftType> form = new Form<>("form");
    FormComponent<ShiftType> select = new HiddenField<>("select", ShiftType.class);
    ShiftTypeLabel label = new ShiftTypeLabel("label");
    IModel<List<ShiftType>> shiftTypeListModel = CollectionModel.asList(shiftTypeRepository::findById);
    WebMarkupContainer dropDownMenu = new WebMarkupContainer("drop-down-menu");
    WebMarkupContainer dropDown = new WebMarkupContainer("dropdown");

    public ShiftTypeEditor(String id) {
        super(id);
    }

    public ShiftTypeEditor(String id, IModel<ShiftType> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        dropDown.setOutputMarkupId(true);
        form.setModel(getModel());
        label.setModel(getModel());
        select.setModel(getModel());
        label.setModel(getModel());
        form.setOutputMarkupId(true);
        add(form);
        form.add(dropDown);
        form.add(select);
        dropDownMenu.setOutputMarkupId(true);
        dropDown.add(new AjaxLink<ShiftType>("btn-select", getModel()) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                shiftTypeListModel.setObject(shiftTypeRepository.findAll());
                target.add(dropDownMenu);
                target.appendJavaScript(String.format("$('%s').dropdown('show')", this.getOutputMarkupId()));
            }
        }.add(label));

        ListView items = new ListView<ShiftType>("items", shiftTypeListModel) {
            @Override
            protected void populateItem(ListItem<ShiftType> item) {
                item.add(new ShiftTypeLabel("label", item.getModel()));
                item.add(new Label("shiftTypeName", item.getModel().map(ShiftType::getName)));
            }
        };
        dropDownMenu.add(items);
        dropDown.add(dropDownMenu);
    }
}
