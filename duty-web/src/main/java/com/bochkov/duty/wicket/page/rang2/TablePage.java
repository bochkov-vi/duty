package com.bochkov.duty.wicket.page.rang2;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.jpa.crud.CrudTablePanel;
import org.apache.commons.compress.utils.Lists;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@MountPath("rang2")
public class TablePage extends BootstrapPage<Rang> {

    @SpringBean
    RangRepository repository;

    public TablePage() {
    }

    public TablePage(IModel<Rang> model) {
        super(model);
    }

    public TablePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CrudTablePanel<Rang, Short>("content", Rang.class) {
            @Override
            public Class<? extends Page> getEditPageClass() {
                return EditPage.class;
            }

            @Override
            protected RangRepository getRepository() {
                return repository;
            }

            @Override
            protected List<? extends IColumn<Rang, String>> columns() {
                List<IColumn<Rang, String>> list = Lists.newArrayList();
                list.add(new PropertyColumn<Rang, String>(new ResourceModel("id"), "id", "id"));
                list.add(new PropertyColumn<Rang, String>(new ResourceModel("name"), "name", "name"));
                list.add(createEditColumn());
                list.add(createDeleteColumn());
                return list;
            }
        }.setAjax(true));
    }
}
