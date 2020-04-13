package com.bochkov.duty.wicket.component.select2;

import lombok.NonNull;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

public class BootstrapSelect2Field<R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> extends EntitySelect2Field<R, T, ID> {
    public BootstrapSelect2Field(String id, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... searchProperties) {
        super(id, idConverter, repositorySupplier, searchProperties);
    }

    public BootstrapSelect2Field(String id, IModel<T> model, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... searchProperties) {
        super(id, model, idConverter, repositorySupplier, searchProperties);
    }

    public BootstrapSelect2Field(String id, String... searchProperties) {
        super(id, searchProperties);
    }

    public BootstrapSelect2Field(String id, IModel<T> model, String... searchProperties) {
        super(id, model, searchProperties);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new BootstrapSelect2Behavior());
    }
}
