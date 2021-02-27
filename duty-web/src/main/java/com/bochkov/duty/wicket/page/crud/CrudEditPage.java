package com.bochkov.duty.wicket.page.crud;

import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.jpa.crud.CrudEditPanel;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public abstract class CrudEditPage<T extends Persistable<ID>, ID extends Serializable> extends BootstrapPage<T> {

    Class<T> entityClass;

    Class<ID> idClass;

    public CrudEditPage(Class<T> entityClass, Class<ID> idClass) {
        this.entityClass = entityClass;
        this.idClass = idClass;
    }

    public CrudEditPage(IModel<T> model, Class<T> entityClass, Class<ID> idClass) {
        super(model);
        this.entityClass = entityClass;
        this.idClass = idClass;
    }

    public CrudEditPage(PageParameters parameters, Class<T> entityClass, Class<ID> idClass) {
        super(parameters);
        this.entityClass = entityClass;
        this.idClass = idClass;
    }

    @Override
    protected void onInitialize() {
        add(new CrudEditPanel<T, ID>("content", entityClass) {
            @Override
            protected <R extends CrudRepository<T, ID>> R getRepository() {
                return CrudEditPage.this.getRepository();
            }

            @Override
            protected Component createInputPanel(String id, IModel<T> model) {
                return CrudEditPage.this.createInputPanel(id, model);
            }
        });
    }

    protected abstract <R extends CrudRepository<T, ID>> R getRepository();

    protected abstract Component createInputPanel(String id, IModel<T> model);
}
