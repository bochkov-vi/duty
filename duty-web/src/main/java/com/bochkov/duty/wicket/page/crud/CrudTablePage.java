package com.bochkov.duty.wicket.page.crud;

import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.jpa.crud.CrudTablePanel;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;

public abstract class CrudTablePage<T extends Persistable<ID>, ID extends Serializable> extends BootstrapPage<T> {

    Class<T> entityClass;

    public CrudTablePage(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public CrudTablePage(IModel<T> model, Class<T> entityClass) {
        super(model);
        this.entityClass = entityClass;
    }

    public CrudTablePage(PageParameters parameters, Class<T> entityClass) {
        super(parameters);
        this.entityClass = entityClass;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CrudTablePanel<T, ID>("content", entityClass) {
            @Override
            public Class<Page> getEditPageClass() {
                return CrudTablePage.this.getEditPageClass();
            }

            @Override
            protected <R extends CrudRepository<T, ID>> R getRepository() {
                return CrudTablePage.this.getRepository();
            }

            @Override
            protected List<? extends IColumn<T, String>> columns() {
                return CrudTablePage.this.columns();
            }
        });
    }

    public abstract Class<Page> getEditPageClass();

    protected abstract <R extends CrudRepository<T, ID>> R getRepository();

    protected abstract List<? extends IColumn<T, String>> columns();
}
