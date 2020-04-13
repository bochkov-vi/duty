package com.bochkov.duty.wicket.component.select2;

import lombok.NonNull;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.wicketstuff.select2.Select2MultiChoice;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.Collection;

public class EntitySelect2MultiField<R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> extends Select2MultiChoice<T> {


    @NonNull
    private String[] searchProperties;

    @NonNull
    private SerializableFunction<String, ID> idConverter = str -> convertToId(str);

    @NonNull
    private SerializableSupplier<R> repositorySupplier = () -> getRepository();

    public EntitySelect2MultiField(String id, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... searchProperties) {
        super(id);
        this.idConverter = idConverter;
        this.repositorySupplier = repositorySupplier;
        this.searchProperties = searchProperties;
        setProvider(EntityChoiceProvider.of(idConverter, repositorySupplier, searchProperties));
    }

    public EntitySelect2MultiField(String id, IModel<Collection<T>> model, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... searchProperties) {
        super(id, model);
        this.idConverter = idConverter;
        this.repositorySupplier = repositorySupplier;
        this.searchProperties = searchProperties;
        setProvider(EntityChoiceProvider.of(idConverter, repositorySupplier, searchProperties));
    }

    public EntitySelect2MultiField(String id, String... searchProperties) {
        super(id);
        this.searchProperties = searchProperties;
        setProvider(EntityChoiceProvider.of(idConverter, repositorySupplier, getSearchProperties()));
    }

    public EntitySelect2MultiField(String id, IModel<Collection<T>> model, String... searchProperties) {
        super(id, model);
        this.searchProperties = searchProperties;
        setProvider(EntityChoiceProvider.of(idConverter, repositorySupplier, getSearchProperties()));
    }

    public static <R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> EntitySelect2MultiField<R, T, ID> of(String id, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... properties) {
        return new EntitySelect2MultiField<R, T, ID>(id, idConverter, repositorySupplier, properties);
    }

    public static <R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> EntitySelect2MultiField<R, T, ID> of(String id, IModel<Collection<T>> model, @NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, String... properties) {
        return new EntitySelect2MultiField<R, T, ID>(id, model, idConverter, repositorySupplier, properties);
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

}
