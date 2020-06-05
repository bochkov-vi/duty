package com.bochkov.duty.wicket.page.day;

import com.bochkov.bootstrap.tempusdominus.LocalDateField;
import com.bochkov.duty.jpa.entity.AbstractEntity;
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

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DayInputPanel extends GenericPanel<Day> {
    @SpringBean
    ShiftTypeRepository shiftTypeRepository;

    TextField<LocalDate> idField = new LocalDateField("id", getString("datePattern")) {
        @Override
        public boolean isEnabled() {
            return DayInputPanel.this.getModel().map(AbstractEntity::isNew).getObject();
        }
    };
    TextField<LocalDate> nextField = new LocalDateField("next", getString("datePattern")) {
        @Override
        public boolean isEnabled() {
            return DayInputPanel.this.getModel().map(AbstractEntity::isNew).getObject();
        }
    };
    TextField<LocalDate> prevField = new LocalDateField("prev", getString("datePattern")) {
        @Override
        public boolean isEnabled() {
            return DayInputPanel.this.getModel().map(AbstractEntity::isNew).getObject();
        }
    };

    TextField<Integer> daysToWeekendField = new TextField<>("daysToWeekend", Integer.class);
    TextField<Integer> daysFromWeekendField = new TextField<>("daysFromWeekend", Integer.class);
    TextField<Integer> weekIndexField = new TextField<>("weekIndex", Integer.class);
    TextField<Integer> dayIndexField = new TextField<>("dayIndex", Integer.class);
    TextField<DayOfWeek> dayOfWeekField = new TextField<>("dayOfWeek", DayOfWeek.class);

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
        idField.setRequired(true);
        shiftTypeField.setRequired(true);
        nextField.setEnabled(false);
        prevField.setEnabled(false);
        daysToWeekendField.setEnabled(false);
        daysFromWeekendField.setEnabled(false);
        weekIndexField.setEnabled(false);
        weekendField.setEnabled(false);
        dayIndexField.setEnabled(false);
        dayOfWeekField.setEnabled(false);
        add(idField, shiftTypeField,
                weekendField,
                nextField,
                prevField,
                daysToWeekendField,
                daysFromWeekendField,
                weekIndexField,
                weekendField,
                dayIndexField,
                dayOfWeekField);
    }
}
