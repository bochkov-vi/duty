package com.bochkov.duty.wicket.component.select2;

import lombok.NonNull;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Collection;

public class BootstrapSelect2MultiField<R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> extends EntitySelect2MultiField<R, T, ID> {
    public BootstrapSelect2MultiField(String id, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... searchProperties) {
        super(id, idConverter, repositorySupplier, searchProperties);
    }

    public BootstrapSelect2MultiField(String id, IModel<Collection<T>> model, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... searchProperties) {
        super(id, model, idConverter, repositorySupplier, searchProperties);
    }

    public BootstrapSelect2MultiField(String id, String... searchProperties) {
        super(id, searchProperties);
    }

    public BootstrapSelect2MultiField(String id, IModel<Collection<T>> model, String... searchProperties) {
        super(id, model, searchProperties);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        BootstrapSelect2Behavior behavior = new BootstrapSelect2Behavior();
        add(behavior);
    }
}
