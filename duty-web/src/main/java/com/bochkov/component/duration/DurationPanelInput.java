package com.bochkov.component.duration;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.time.Duration;
import java.util.Optional;

public class DurationPanelInput extends FormComponentPanel<Duration> {
    TextField<Integer> days = new NumberTextField<>("days", Model.of(), Integer.class).setMinimum(0).setMaximum(1);
    TextField<Integer> hours = new NumberTextField<>("hours", Model.of(), Integer.class).setMinimum(0).setMaximum(23);
    TextField<Integer> minutes = new NumberTextField<>("minutes", Model.of(), Integer.class).setMinimum(0).setMaximum(59).setStep(15);

    public DurationPanelInput(String id) {
        super(id);
    }

    public DurationPanelInput(String id, IModel<Duration> model) {
        super(id, model);
    }

    @Override
    public void convertInput() {
        Duration result = Duration.ZERO;
        result = result.plusDays(Optional.ofNullable(days.getConvertedInput()).orElse(0));
        result = result.plusHours(Optional.ofNullable(hours.getConvertedInput()).orElse(0));
        result = result.plusMinutes(Optional.ofNullable(minutes.getConvertedInput()).orElse(0));
        setConvertedInput(result);

    }

    @Override
    protected void onBeforeRender() {
        days.setModelObject(getModel().map(Duration::toDays).map(Long::intValue).orElse(null).getObject());
        hours.setModelObject(getModel().map(Duration::toHours).map(Long::intValue).orElse(null).getObject());
        minutes.setModelObject(getModel().map(Duration::getSeconds).map(Long::intValue).orElse(null).getObject());
        super.onBeforeRender();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        queue(days, hours, minutes);
    }
}
