package com.bochkov.duty.wicket.page.day;

import com.bochkov.bootstrap.tempusdominus.LocalDateField;
import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.wicket.component.select2.data.PersistableChoiceProvider;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.select2.Select2Choice;

import java.time.LocalDate;

public class DayInputPanel extends GenericPanel<Day> {
    @SpringBean
    ShiftTypeRepository shiftTypeRepository;

    TextField<LocalDate> idField = new LocalDateField("id", getString("datePattern"));
    CheckBox weekendField = new CheckBox("weekend");
    Select2Choice<ShiftType> shiftTypeField = new Select2Choice<ShiftType>("shiftType",
            PersistableChoiceProvider.ofIntId(() -> shiftTypeRepository, "name", "id")
    );

    public DayInputPanel(String id) {
        super(id);
    }

    public DayInputPanel(String id, IModel<Day> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(idField, shiftTypeField,weekendField);
    }
}
