package com.bochkov.duty.wicket.component.select2;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class EntityChoiceProvider<R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> extends MaskableChoiceProvider<T, ID> {

    @NonNull
    SerializableFunction<String, ID> idConverter;



    @NonNull
    SerializableSupplier<R> repositorySupplier;

    public EntityChoiceProvider(@NonNull SerializableFunction<String, ID> idConverter, @NonNull SerializableSupplier<R> repositorySupplier, @NonNull String... maskedProperties) {
        super(maskedProperties);
        this.idConverter = idConverter;
        this.repositorySupplier = repositorySupplier;
    }

    public static <R extends JpaSpecificationExecutor<T> & Repository<T, ID>, T extends Persistable<ID>, ID extends Serializable> EntityChoiceProvider<R, T, ID> of(
            SerializableFunction<String, ID> idConverter, SerializableSupplier<R> repositorySupplier, @NonNull String... properties
    ) {
        EntityChoiceProvider<R, T, ID> provider = new EntityChoiceProvider<>(idConverter, repositorySupplier, properties);
        return provider;
    }

    @Override
    public R getRepository() {
        return repositorySupplier.get();
    }

    @Override
    public ID toId(String str) {
        return idConverter.apply(str);
    }
}
