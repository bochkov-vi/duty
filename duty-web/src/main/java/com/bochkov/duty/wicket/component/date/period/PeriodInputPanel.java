package com.bochkov.duty.wicket.component.date.period;

import com.bochkov.bootstrap.tempusdominus.LocalTimeField;
import com.bochkov.component.duration.DurationPanelInput;
import com.bochkov.duty.jpa.entity.Period;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.time.Duration;
import java.time.LocalTime;

public class PeriodInputPanel extends FormComponentPanel<Period> {

    TextField<LocalTime> startField = new LocalTimeField("start", Model.of(), getString("timePattern")).setStepping(15);
    FormComponent<Duration> durationField = new DurationPanelInput("duration", Model.of());

    public PeriodInputPanel(String id) {
        super(id);
    }

    public PeriodInputPanel(String id, IModel<Period> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(startField, durationField);
    }

    @Override
    protected void onBeforeRender() {
        startField.setModelObject(getModel().map(Period::getStart).orElse(null).getObject());
        durationField.setModelObject(getModel().map(Period::getDuration).orElse(null).getObject());
        super.onBeforeRender();
    }

    @Override
    public void convertInput() {
        LocalTime start = startField.getConvertedInput();
        Duration duration = durationField.getConvertedInput();
        Period period = new Period(start, duration);
        setConvertedInput(period);
    }
}
