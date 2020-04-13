package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.UiOptions;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
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
import java.util.Optional;

@MountPath("duty-type")
public class DutyTypePage extends EntityPage<DutyType, Integer> {
    @SpringBean
    private DutyTypeRepository dutyTypeRepository;

    public DutyTypePage() {
    }

    public DutyTypePage(DutyType entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<DutyType, String>> columns() {
        List<IColumn<DutyType, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<DutyType, String>(new ResourceModel("dutyType.id"), "id", DutyType::getId));
        list.add(new LambdaColumn<DutyType, String>(new ResourceModel("dutyType.name"), "name", DutyType::getName));
        list.add(new LambdaColumn<DutyType, String>(new ResourceModel("dutyType.uiOptions.htmlClass"), "uiOptions.htmlClass", dt -> Optional.ofNullable(dt).map(DutyType::getUiOptions).map(UiOptions::getHtmlClass).orElse(null)));
        list.add(new LambdaColumn<DutyType, String>(new ResourceModel("dutyType.uiOptions.plainText"), "uiOptions.plainText", dt -> Optional.ofNullable(dt).map(DutyType::getUiOptions).map(UiOptions::getPlainText).orElse(null)));
        list.add(new LambdaColumn<DutyType, String>(new ResourceModel("dutyType.uiOptions.faIcon"), "uiOptions.faIcon", dt -> Optional.ofNullable(dt).map(DutyType::getUiOptions).map(UiOptions::getFaIcon).orElse(null)));
        return list;
    }


    @Override
    protected DutyTypeRepository getRepository() {
        return dutyTypeRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<DutyType> model) {
        return new DutyTypeInputPanel(id, model);
    }

    @Override
    protected GenericPanel<DutyType> createDetailsPanel(String id, IModel<DutyType> model) {
        return new DetailsPanel<DutyType>(id, getModel(), ImmutableList.of("id", "name"), "dutyType.");
    }

    @Override
    protected DutyType newInstance() {
        return new DutyType().setUiOptions(new UiOptions());
    }
}
