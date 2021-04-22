package com.bochkov.duty.wicket.page.rang;

import com.bochkov.duty.jpa.entity.Rang;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class RangInputPanel extends GenericPanel<Rang> {

    public RangInputPanel(String id, IModel<Rang> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<Rang>)getModel()).bind();
        add(new TextField<>("id").setEnabled(false));
        add(new RequiredTextField<>("name"));
        add(new RequiredTextField<>("fullName"));
        add(new TextField<>("createdDate").setEnabled(false));
    }
}
