package com.bochkov.duty.wicket.component.date;

import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTextField;
import org.apache.wicket.model.IModel;

import java.time.LocalDate;
import java.time.format.FormatStyle;

public class LocaDateTextField extends LocalDateTextField {
    BootstrapDatePickerBehavior behavior = new BootstrapDatePickerBehavior();

    public LocaDateTextField(String id, String pattern) {
        super(id, pattern);
    }

    public LocaDateTextField(String id, IModel<LocalDate> model, String pattern) {
        super(id, model, pattern);
    }

    public LocaDateTextField(String id, IModel<LocalDate> model, String formatPattern, String parsePattern) {
        super(id, model, formatPattern, parsePattern);
    }

    public LocaDateTextField(String id, FormatStyle dateStyle) {
        super(id, dateStyle);
    }

    public LocaDateTextField(String id, IModel<LocalDate> model, FormatStyle dateStyle) {
        super(id, model, dateStyle);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(behavior);
        setLanguage("ru");
    }


    public LocalDateTextField setLanguage(String language) {
        behavior.setLanguage(language);
        return this;
    }
}
