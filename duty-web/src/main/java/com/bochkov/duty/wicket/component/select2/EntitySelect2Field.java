package com.bochkov.duty.wicket.component.select2;

import lombok.NonNull;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.wicketstuff.select2.Select2Choice;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

public class EntitySelect2Field<R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> extends Select2Choice<T> {

    @NonNull
    private String[] searchProperties;

    @NonNull
    private SerializableFunction<String, ID> idConverter = str -> convertToId(str);

    @NonNull
    private SerializableSupplier<R> repositorySupplier = () -> getRepository();

    public EntitySelect2Field(String id, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... searchProperties) {
        super(id);
        this.idConverter = idConverter;
        this.repositorySupplier = repositorySupplier;
        this.searchProperties = searchProperties;
        setProvider(EntityChoiceProvider.of(idConverter, repositorySupplier, searchProperties));
    }

    public EntitySelect2Field(String id, IModel<T> model, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... searchProperties) {
        super(id, model);
        this.idConverter = idConverter;
        this.repositorySupplier = repositorySupplier;
        this.searchProperties = searchProperties;
        setProvider(newDataProvider());
    }

    public EntitySelect2Field(String id, String... searchProperties) {
        super(id);
        this.searchProperties = searchProperties;
        setProvider(newDataProvider());
    }

    public EntitySelect2Field(String id, IModel<T> model, String... searchProperties) {
        super(id, model);
        this.searchProperties = searchProperties;
        setProvider(newDataProvider());
    }

    public static <R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> EntitySelect2Field<R, T, ID> of(String id, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... properties) {
        return new EntitySelect2Field<R, T, ID>(id, idConverter, repositorySupplier, properties);
    }

    public static <R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> EntitySelect2Field<R, T, ID> of(String id, IModel<T> model, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... properties) {
        return new EntitySelect2Field<R, T, ID>(id, model, idConverter, repositorySupplier, properties);
    }

    protected R getRepository() {
        throw new NotImplementedException();
    }

    protected ID convertToId(String str) {
        throw new NotImplementedException();
    }

    protected String[] getSearchProperties() {
        return searchProperties;
    }

    protected EntityChoiceProvider<R, T, ID> newDataProvider() {
        return EntityChoiceProvider.of(idConverter, repositorySupplier, getSearchProperties());
    }
}
