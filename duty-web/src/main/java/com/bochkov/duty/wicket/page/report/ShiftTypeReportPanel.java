package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.duty.wicket.page.shifttype.ShiftTypeFieldSelect;
import com.bochkov.duty.wicket.page.shifttype.ShiftTypeLabel;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import com.bochkov.wicket.data.provider.SortedListModelDataProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

public class ShiftTypeReportPanel extends FormComponentPanel<Collection<ShiftType>> {
    @Inject
    ShiftTypeRepository shiftTypeRepository;

    IModel<Collection<ShiftType>> shiftTypeModel = CollectionModel.of(shiftTypeRepository::findById);
    FormComponent<ShiftType> select = new ShiftTypeFieldSelect("shiftType", PersistableModel.of(shiftTypeRepository::findById), shiftTypeModel);
    Component table = table("table");

    public ShiftTypeReportPanel(String id) {
        super(id);
    }

    public ShiftTypeReportPanel(String id, IModel<Collection<ShiftType>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(table);
        select.setOutputMarkupId(true);
        select.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(table);
                addAction();
                target.add(select);
            }
        });
        Form form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                addAction();
            }
        };
        form.add(select);
        form.add(new AjaxSubmitLink("add", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                target.add(table, select);

            }
        });
        table.setOutputMarkupId(true);
        add(form);
    }

    private Component table(String id) {
        ISortableDataProvider dataProvider = dataProvider();
        DataTable table = new DataTable(id, columns(), dataProvider, 100);
        table.addTopToolbar(new HeadersToolbar(table, dataProvider));
        table.addBottomToolbar(new NoRecordsToolbar(table, new ResourceModel("datatable.no-records-found")));
        return table;

    }

    List<IColumn<ShiftType, ?>> columns() {
        List<IColumn<ShiftType, ?>> columns = Lists.newArrayList();
        columns.add(new LambdaColumn<>(new ResourceModel("shiftType.id"), "id", ShiftType::getId));
        columns.add(new LambdaColumn<>(new ResourceModel("shiftType.name"), "rang.name", ShiftType::getName));
        columns.add(new AbstractColumn<ShiftType, Object>(new ResourceModel("shiftType.uiOptions"), "uiOptions.faIcon") {
            @Override
            public void populateItem(Item<ICellPopulator<ShiftType>> cellItem, String componentId, IModel<ShiftType> rowModel) {
                cellItem.add(new ShiftTypeLabel(componentId, rowModel));
            }
        });
        columns.add(new HeaderlessColumn<ShiftType, Object>() {
            @Override
            public void populateItem(Item<ICellPopulator<ShiftType>> cellItem, String componentId, IModel<ShiftType> rowModel) {
                cellItem.add(new AjaxLink<ShiftType>(componentId, rowModel) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        target.add(table);
                        Collection<ShiftType> collection = shiftTypeModel.getObject();
                        collection.remove(getModelObject());
                    }
                }.setBody(Model.of("<button type='button' class='btn btn-outline-secondary'><i class='fa fa-close'></i></button>")).setEscapeModelStrings(false));
            }
        });
        return columns;
    }

    ISortableDataProvider<ShiftType, ?> dataProvider() {
        return SortedListModelDataProvider.of(shiftTypeModel, (t -> PersistableModel.of(t, shiftTypeRepository::findById)));
    }

    @Override
    protected void onBeforeRender() {
        shiftTypeModel.setObject(getModelObject());
        super.onBeforeRender();
    }

    @Override
    public void convertInput() {
        setConvertedInput(shiftTypeModel.getObject());
    }

    public void addAction() {
        ShiftType selected = select.getModelObject();
        if (selected != null) {
            Collection<ShiftType> shiftTypes = shiftTypeModel.getObject();
            shiftTypes.add(selected);
            shiftTypeModel.setObject(Sets.newHashSet(shiftTypes));
        }
        select.setModelObject(null);
    }
}
