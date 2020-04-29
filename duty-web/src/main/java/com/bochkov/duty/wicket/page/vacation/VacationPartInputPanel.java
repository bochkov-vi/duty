package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.bootstrap.tempusdominus.LocalDateField;
import com.bochkov.duty.jpa.entity.VacationPart;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class VacationPartInputPanel extends FormComponentPanel<VacationPart> {
    LocalDateField start = new LocalDateField("start", Model.of(), getString("datePattern"));
    LocalDateField end = new LocalDateField("end", Model.of(), getString("datePattern"));
    TextField<Integer> partNumber = new NumberTextField<Integer>("partNumber", Model.of(), Integer.class);

    public VacationPartInputPanel(String id) {
        super(id);
    }

    public VacationPartInputPanel(String id, IModel<VacationPart> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(start, end, partNumber);
    }

    @Override
    protected void onBeforeRender() {
        start.setModelObject(getModel().map(VacationPart::getStart).orElse(null).getObject());
        end.setModelObject(getModel().map(VacationPart::getEnd).orElse(null).getObject());
        partNumber.setModelObject(getModel().map(VacationPart::getPartNumber).orElse(null).getObject());
        super.onBeforeRender();
    }

    @Override
    public void convertInput() {
        setConvertedInput(new VacationPart()
                .setStart(start.getConvertedInput())
                .setEnd(end.getConvertedInput())
                .setPartNumber(partNumber.getConvertedInput()));
    }
}
