package com.bochkov.durationpicker;

import com.bochkov.bootstrap.BootstrapBehavior;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class DurationField extends TextField<Duration> {
    IConverter<Duration> converter = new IConverter<Duration>() {
        @Override
        public Duration convertToObject(String value, Locale locale) throws ConversionException {
            return Optional.ofNullable(value).map(str -> str.split(":")).map(a -> new Integer[]{Ints.tryParse(a[0]), Ints.tryParse(a[1])})
                    .map(a -> Duration.ZERO.plusHours(a[0]).plusMinutes(a[1])).orElse(null);
        }

        @Override
        public String convertToString(Duration value, Locale locale) {
            return Optional.ofNullable(value).map(d -> DurationFormatUtils.formatDuration(d.toMillis(), "HH:mm")).orElse(null);
        }
    };

    public DurationField(String id) {
        super(id, Duration.class);
    }

    public DurationField(String id, IModel<Duration> model) {
        super(id, model, Duration.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new ClassAttributeModifier() {
            @Override
            protected Set<String> update(Set<String> oldClasses) {
                oldClasses.add("html-duration-picker");
                return oldClasses;
            }
        });
        add(new AttributeAppender("data-duration", () -> getDefaultModelObjectAsString()));
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return (IConverter<C>) converter;
    }

    @Override
    public void convertInput() {
        super.convertInput();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(DurationField.class, "DurationField.js") {
            @Override
            public List<HeaderItem> getDependencies() {
                return Lists.newArrayList(JavaScriptHeaderItem.forReference(BootstrapBehavior.JS),
                        CssHeaderItem.forReference(BootstrapBehavior.CSS));
            }
        }));
        response.render(OnDomReadyHeaderItem.forScript(String.format("DurationPicker($('#%s'))", getMarkupId())));
    }
}
