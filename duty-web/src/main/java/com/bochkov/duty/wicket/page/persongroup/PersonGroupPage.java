package com.bochkov.duty.wicket.page.persongroup;

import com.bochkov.duty.jpa.entity.PersonGroup;
import com.bochkov.duty.jpa.repository.PersonGroupRepository;
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

@MountPath("personal-group")
public class PersonGroupPage extends EntityPage<PersonGroup, Integer> {
    @SpringBean
    private PersonGroupRepository personGroupRepository;

    public PersonGroupPage() {
    }

    public PersonGroupPage(PersonGroup entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<PersonGroup, String>> columns() {
        List<IColumn<PersonGroup, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<PersonGroup, String>(new ResourceModel("personGroup.id"), "id", PersonGroup::getId));
        list.add(new LambdaColumn<PersonGroup, String>(new ResourceModel("personGroup.name"), "name", PersonGroup::getName));
        return list;
    }


    @Override
    protected PersonGroupRepository getRepository() {
        return personGroupRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<PersonGroup> model) {
        return new PersonGroupInputPanel(id, model);
    }

    @Override
    protected GenericPanel<PersonGroup> createDetailsPanel(String id, IModel<PersonGroup> model) {
        return new DetailsPanel<PersonGroup>(id, getModel(), ImmutableList.of("id", "name","createdDate"), "personGroup.");
    }

    @Override
    protected PersonGroup newInstance() {
        return new PersonGroup();
    }
}
