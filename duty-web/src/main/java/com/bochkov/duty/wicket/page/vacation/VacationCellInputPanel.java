package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.duty.jpa.entity.Vacation;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.time.LocalDate;

public class VacationCellInputPanel extends FormComponentPanel<Vacation> {


    FormComponent<LocalDate> startDateInput = new HiddenField<>("start-date", Model.of());

    public VacationCellInputPanel(String id) {
        super(id);
    }

    public VacationCellInputPanel(String id, IModel<Vacation> model) {
        super(id, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
    }

    @Override
    public void convertInput() {

    }
}
