package com.bochkov.duty.wicket.service.converter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.wicket.Application;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositeConverter<ID> implements IConverter<ID> {

    Class<ID> idClass;

    List<PropertyDescriptor> descriptors;
    //Supplier<IConverterLocator> converterLocatorSupplier;


    public CompositeConverter(Class<ID> idClass) {
        this(idClass, BeanUtils.getPropertyDescriptors(idClass));
    }

    public CompositeConverter(Class<ID> idClass,String... properties) {
        this(idClass, Stream.of(properties).map(property -> BeanUtils.getPropertyDescriptor(idClass, property)).collect(Collectors.toList()));
    }

    public CompositeConverter(Class<ID> idClass, List<PropertyDescriptor> propertyDescriptors) {
        this.idClass = idClass;
        descriptors = propertyDescriptors;
    }

    public CompositeConverter(Class<ID> idClass,  PropertyDescriptor... propertyDescriptors) {
        this(idClass, Stream.of(propertyDescriptors).collect(Collectors.toList()));
    }

    @Override
    public ID convertToObject(String s, Locale locale) throws ConversionException {
        ID pk = null;
        Optional<List<String>> optional = Optional.ofNullable(s).map(v -> Splitter.on("~").splitToList(v));
        if (optional.isPresent()) {
            pk = BeanUtils.instantiateClass(idClass);
            Iterator<String> data = optional.map(strings -> strings.iterator()).get();

            for (Iterator<PropertyDescriptor> iterator = descriptors.iterator(); iterator.hasNext() && data.hasNext(); ) {
                PropertyDescriptor descriptor = iterator.next();
                IConverter converter = getConverterLocator().getConverter(descriptor.getPropertyType());
                String string = data.next();
                Object value = converter.convertToObject(string, locale);
                try {
                    descriptor.getWriteMethod().invoke(pk, value);
                } catch (Exception e) {
                    throw new ConversionException(e);
                }
            }

        }
        return pk;
    }

    @Override
    public String convertToString(ID pk, Locale locale) {
        String result = null;
        if (pk != null) {
            List data = descriptors.stream().map(descriptor -> {
                String string = null;
                try {
                    IConverter converter = getConverterLocator().getConverter(descriptor.getPropertyType());
                    Object value = descriptor.getReadMethod().invoke(pk);
                    string = converter.convertToString(value, locale);
                    return string;
                } catch (Exception ignored) {

                }
                return null;
            }).collect(Collectors.toList());
            result = Joiner.on('~').join(data);
        }
        return null;
    }

    IConverterLocator getConverterLocator() {
        return Application.get().getConverterLocator();
    }
}
