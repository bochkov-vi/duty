package com.bochkov.duty.wicket.component;

import com.google.common.primitives.Ints;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.io.Serializable;
import java.time.Duration;
import java.util.Locale;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class DurationTextField extends TextField<Duration> {

    private static final long serialVersionUID = 1L;

    private final DurationConverter converter = new DurationConverter();

    public DurationTextField(String id) {
        super(id, Duration.class);
    }

    public DurationTextField(String id, IModel<Duration> model) {
        super(id, model, Duration.class);
    }


    @Override
    protected IConverter<?> createConverter(Class<?> clazz) {
        if (Duration.class.isAssignableFrom(clazz)) {
            return converter;
        }
        return null;
    }


    private class DurationConverter implements IConverter<Duration>, Serializable {

        private static final long serialVersionUID = 1L;


        @Override
        public Duration convertToObject(String value, Locale locale) throws ConversionException {
            return Optional.ofNullable(value)
                    .map(v -> v.split(":"))
                    .map(a -> new Integer[]{Ints.tryParse(a[0]), Ints.tryParse(a[1])})
                    .map(a -> Duration.ZERO.plusHours(a[0]).plusMinutes(a[1]))
                    .orElse(null);
        }

        @Override
        public String convertToString(Duration value, Locale locale) {
            return DurationFormatUtils.formatDuration(value.toMillis(), "HH:mm");
        }
    }
}
