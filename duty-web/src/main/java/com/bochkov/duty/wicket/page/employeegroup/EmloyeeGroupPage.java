package com.bochkov.duty.wicket.page.employeegroup;

import com.bochkov.duty.jpa.entity.EmployeeGroup;
import com.bochkov.duty.jpa.repository.EmployeeGroupRepository;
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

@MountPath("employee-group")
public class EmloyeeGroupPage extends EntityPage<EmployeeGroup, Integer> {

    @SpringBean
    private EmployeeGroupRepository employeeGroupRepository;

    public EmloyeeGroupPage() {
        super(EmployeeGroup.class);
    }

    public EmloyeeGroupPage(EmployeeGroup entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<EmployeeGroup, String>> columns() {
        List<IColumn<EmployeeGroup, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<EmployeeGroup, String>(new ResourceModel("employeeGroup.id"), "id", EmployeeGroup::getId));
        list.add(new LambdaColumn<EmployeeGroup, String>(new ResourceModel("employeeGroup.name"), "name", EmployeeGroup::getName));
        return list;
    }


    @Override
    protected EmployeeGroupRepository getRepository() {
        return employeeGroupRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<EmployeeGroup> model) {
        return new EmployeeGroupInputPanel(id, model);
    }

    @Override
    protected GenericPanel<EmployeeGroup> createDetailsPanel(String id, IModel<EmployeeGroup> model) {
        return new DetailsPanel<EmployeeGroup>(id, getModel(), ImmutableList.of("id", "name", "createdDate"), "employeeGroup.");
    }

    @Override
    protected EmployeeGroup newInstance() {
        return new EmployeeGroup();
    }
}
