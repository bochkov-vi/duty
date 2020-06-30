package com.bochkov.duty.wicket.page.shifttype;

import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.entity.UiOptions;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.duty.wicket.base.DetailsPanel;
import com.bochkov.duty.wicket.base.EntityPage;
import com.bochkov.duty.wicket.component.FaIcon;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@MountPath("shift-type")
public class ShiftTypePage extends EntityPage<ShiftType, Integer> {

    @SpringBean
    private ShiftTypeRepository shiftTypeRepository;

    public ShiftTypePage() {
        super();
    }

    public ShiftTypePage(ShiftType entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<ShiftType, String>> columns() {
        List<IColumn<ShiftType, String>> list = Lists.newArrayList();
        list.add(new PropertyColumn<>(new ResourceModel("shiftType.id"), "id", "id"));
        list.add(new PropertyColumn<ShiftType, String>(new ResourceModel("shiftType.name"), "name", "name"));
        list.add(new PropertyColumn<ShiftType, String>(new ResourceModel("shiftType.uiOptions.htmlClass"), "uiOptions.htmlClass", "uiOptions.htmlClass"));
        list.add(new PropertyColumn<ShiftType, String>(new ResourceModel("shiftType.uiOptions.plainText"), "uiOptions.plainText", "uiOptions.plainText"));
        list.add(new PropertyColumn<ShiftType, String>(new ResourceModel("shiftType.uiOptions.faIcon"), "uiOptions.faIcon", "uiOptions.faIcon") {
            @Override
            public void populateItem(Item<ICellPopulator<ShiftType>> item, String componentId, IModel<ShiftType> rowModel) {
                item.add(new FaIcon(componentId, getDataModel(rowModel)));
            }
        });
        return list;
    }


    @Override
    protected ShiftTypeRepository getRepository() {
        return shiftTypeRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<ShiftType> model) {
        return new ShiftTypeInputPanel(id, model);
    }

    @Override
    protected GenericPanel<ShiftType> createDetailsPanel(String id, IModel<ShiftType> model) {
        return new DetailsPanel<ShiftType>(id, getModel(), ImmutableList.of("id", "name"), "shiftType.");
    }

    @Override
    protected ShiftType newInstance() {
        return new ShiftType().setUiOptions(new UiOptions());
    }
}
