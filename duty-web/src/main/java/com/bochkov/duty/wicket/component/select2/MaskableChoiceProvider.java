package com.bochkov.duty.wicket.component.select2;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Response;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Accessors(chain = true)
public abstract class MaskableChoiceProvider<T extends Persistable<ID>, ID extends Serializable> extends ChoiceProvider<T> implements Maskable {

    Iterable<String> maskedProperties;

    @NonNull
    @Getter
    @Setter
    SerializableFunction<T, String> choiceRenderer = e -> Optional.ofNullable(e).map(T::toString).orElse("");

    public MaskableChoiceProvider(Iterable<String> maskedProperties) {
        this.maskedProperties = maskedProperties;
    }

    public MaskableChoiceProvider(String... maskedProperties) {
        this.maskedProperties = Lists.newArrayList(maskedProperties);
    }

    @Override
    public String getDisplayValue(T object) {
        return choiceRenderer.apply(object);
    }

    @Override
    public String getIdValue(T object) {
        return Optional.ofNullable(object).map(Persistable::getId).map(Object::toString).orElse("");
    }

    @Override
    public void query(String term, int page, Response<T> response) {
        org.springframework.data.domain.Pageable pageRequest = PageRequest.of(page, getPageSize());
        Page<T> pageResponse = findByMask(term, pageRequest);
        response.setResults(pageResponse.getContent());
        response.setHasMore(pageResponse.hasNext());
    }


    public Page<T> findByMask(String term, Pageable pageRequest) {
        Specification<T> maskedSpecification = Maskable.maskSpecification(term, maskedProperties);
        return getRepository().findAll(Optional.ofNullable(maskedSpecification).map(m -> m.and(excludeSpecification())).orElse(null), pageRequest);
    }

    @Override
    public Collection<T> toChoices(Collection<String> ids) {
        return ids.stream()
                .map(this::toId)
                .map(id -> getRepository().findById(id)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public abstract <R extends JpaSpecificationExecutor<T> & JpaRepository<T, Serializable>> R getRepository();

    public int getPageSize() {
        return 10;
    }

    public Iterable<String> getMaskedProperties() {
        return maskedProperties;
    }

    public MaskableChoiceProvider<T, ID> setMaskedProperties(Iterable<String> maskedProperties) {
        this.maskedProperties = maskedProperties;
        return this;
    }

    public abstract ID toId(String str);

    public List<T> excludes() {
        return null;
    }

    protected Specification<T> excludeSpecification() {
        List<T> excludes = excludes();
        if (excludes != null) {
            return ((root, q, b) -> b.not(root.in(excludes)));
        }
        return null;
    }
}
