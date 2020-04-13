package com.bochkov.duty.wicket.page.rang;

import com.bochkov.duty.jpa.entity.Rang;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;

public class RangInputPanel extends GenericPanel<Rang> {
    public RangInputPanel(String id, IModel<Rang> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<Rang>)getModel()).bind();
        add(new TextField<>("rang.id", LambdaModel.of(getModel(), Rang::getId, Rang::setId)).setEnabled(false));
        add(new RequiredTextField<>("rang.name", LambdaModel.of(getModel(), Rang::getName, Rang::setName)));
        add(new RequiredTextField<>("rang.fullName", LambdaModel.of(getModel(), Rang::getFullName, Rang::setFullName)));
        add(new TextField<>("rang.createdDate", LambdaModel.of(getModel(), Rang::getCreatedDate, Rang::setCreatedDate)).setEnabled(false));
    }
}
