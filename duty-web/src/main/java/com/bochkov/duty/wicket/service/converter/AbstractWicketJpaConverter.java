package com.bochkov.duty.wicket.service.converter;

import org.apache.wicket.Application;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;

public abstract class AbstractWicketJpaConverter<T extends Persistable<ID>, ID extends Serializable> implements IConverter<T> {

    Class<T> domainClass;
    Class<ID> idClass;

    @Autowired
    JpaRepository<T, ID> repository;

    public AbstractWicketJpaConverter(Class<T> domainClass, Class<ID> idClass) {
        this.domainClass = domainClass;
        this.idClass = idClass;
    }

    @Override
    public T convertToObject(String s, Locale locale) throws ConversionException {
        return Optional.ofNullable(s).map(v -> convertIdToObject(v, locale)).flatMap(repository::findById).orElse(null);
    }

    @Override
    public String convertToString(T t, Locale locale) {
        return Optional.ofNullable(t).map(Persistable::getId).map(id -> convertIdToString(id, locale)).orElse(null);
    }

    public ID convertIdToObject(String string, Locale locale) {
        return Application.get().getConverterLocator().getConverter(idClass).convertToObject(string, locale);
    }


    public String convertIdToString(ID id, Locale locale) {
        return Application.get().getConverterLocator().getConverter(idClass).convertToString(id, locale);
    }


}
