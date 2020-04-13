package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.AbstractEntity;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.UiOptions;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IModelComparator;
import org.apache.wicket.model.Model;

import java.time.LocalDateTime;

public class DutyTypeInputPanel extends GenericPanel<DutyType> {
    TextField<Integer> idTextField = new TextField<>("dutyType.id", Model.of());

    TextField<String> nameTextField = new RequiredTextField<>("dutyType.name", Model.of());

    TextField<LocalDateTime> createdDateTextField = new TextField<>("dutyType.createdDate", Model.of());

    TextField<String> plainTextTextField = new TextField<>("dutyType.uiOptions.plainText", Model.of());

    TextField<String> htmlClassTextField = new TextField<>("dutyType.uiOptions.htmlClass", Model.of());

    TextField<String> faIconTextField = new TextField<>("dutyType.uiOptions.faIcon", Model.of());


    Form form = new Form<Void>("form") {
        @Override
        protected void onSubmit() {
            DutyTypeInputPanel.this.onSubmit();
        }
    };

    public DutyTypeInputPanel(String id, IModel<DutyType> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(form);
        //((CompoundPropertyModel<DutyType>)getModel()).bind();
        form.add(idTextField.setEnabled(false));
        form.add(nameTextField);
        form.add(createdDateTextField.setEnabled(false));
        form.add(faIconTextField);
        form.add(htmlClassTextField);
        form.add(plainTextTextField);

    }

    //Call when object set
    @Override
    protected void onConfigure() {
        super.onConfigure();
        idTextField.setModelObject(getModel().map(DutyType::getId).getObject());
        nameTextField.setModelObject(getModel().map(DutyType::getName).getObject());
        IModel<UiOptions> optionsModel = getModel().map(DutyType::getUiOptions);
        htmlClassTextField.setModelObject(optionsModel.map(UiOptions::getHtmlClass).getObject());
        faIconTextField.setModelObject(optionsModel.map(UiOptions::getFaIcon).getObject());
        plainTextTextField.setModelObject(optionsModel.map(UiOptions::getPlainText).getObject());
        createdDateTextField.setModelObject(getModel().map(AbstractEntity::getCreatedDate).getObject());
    }

    //Call when object submited
    public void onSubmit() {
        DutyType dutyType = getModelObject();
        dutyType.setId(idTextField.getModelObject());
        dutyType.setName(nameTextField.getModelObject());
        UiOptions options = dutyType.getUiOptions();
        if (options == null) {
            options = new UiOptions();
        }
        dutyType.setCreatedDate(createdDateTextField.getModelObject());
        options.setPlainText(plainTextTextField.getModelObject())
                .setFaIcon(faIconTextField.getModelObject())
                .setHtmlClass(htmlClassTextField.getModelObject());
        dutyType.setUiOptions(options);
        setModelObject(dutyType);

    }

    @Override
    public IModelComparator getModelComparator() {
        return IModelComparator.ALWAYS_FALSE;
    }
}
