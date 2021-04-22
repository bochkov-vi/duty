package com.bochkov.duty.wicket.page.test;

import com.bochkov.bootstrap.tempusdominus.LocalTimeField;
import com.bochkov.component.durationpicker.DurationField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.Duration;
import java.time.LocalTime;

@MountPath("test-localtime")
public class TestLocalTimeField extends WebPage {

    LocalTimeField localTimeField = new LocalTimeField("input", Model.of(LocalTime.of(9, 0)), "HH:mm");

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Form form = new Form("form");
        add(form);
        form.add(localTimeField);
        form.add(new DurationField("duration", Model.of(Duration.ZERO.plusHours(25).plusMinutes(3))));
        form.add(new DurationField("duration2", Model.of(Duration.ZERO.plusHours(9).plusMinutes(0))));

    }
}
