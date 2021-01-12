package com.bochkov.component.durationpicker;

import com.bochkov.bootstrap.BootstrapBehavior;
import com.google.common.collect.Lists;
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

public class DurationField extends TextField<Duration> {

    IConverter<Duration> converter = new IConverter<Duration>() {
        @Override
        public Duration convertToObject(String value, Locale locale) throws ConversionException {
            return Optional.ofNullable(getConverter(Long.class)).map(cnv -> cnv.convertToObject(value, locale)).map(lng -> Duration.ofSeconds(lng)).orElse(null);
        }

        @Override
        public String convertToString(Duration value, Locale locale) {
            return Optional.ofNullable(value).map(dr -> dr.toMillis() / 1000).map(sec -> getConverter(Long.class).convertToString(sec, locale)).orElse(null);
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
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(DurationField.class, "bootstrap-duration-picker.js") {
            @Override
            public List<HeaderItem> getDependencies() {
                return Lists.newArrayList(JavaScriptHeaderItem.forReference(BootstrapBehavior.JS),
                        CssHeaderItem.forReference(BootstrapBehavior.CSS));
            }
        }));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(DurationField.class, "bootstrap-duration-picker.css")));
        response.render(OnDomReadyHeaderItem.forScript(String.format("$('#%s').durationPicker()",getMarkupId())));


    }
}
