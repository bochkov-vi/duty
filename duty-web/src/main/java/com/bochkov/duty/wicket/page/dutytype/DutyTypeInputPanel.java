package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.DutyType;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import java.time.LocalDateTime;

public class DutyTypeInputPanel extends GenericPanel<DutyType> {

    TextField<Integer> idTextField = new TextField<>("id");

    TextField<String> nameTextField = new RequiredTextField<>("name");

    TextField<LocalDateTime> createdDateTextField = new TextField<>("createdDate");

    TextField<String> plainTextTextField = new TextField<>("uiOptions.plainText");

    TextField<String> htmlClassTextField = new TextField<>("uiOptions.htmlClass");

    TextField<String> faIconTextField = new TextField<>("uiOptions.faIcon");


    public DutyTypeInputPanel(String id, IModel<DutyType> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<DutyType>)getModel()).bind();
        add(idTextField.setEnabled(false));
        add(nameTextField);
        add(createdDateTextField.setEnabled(false));
        add(faIconTextField);
        add(htmlClassTextField);
        add(plainTextTextField);

    }
}
