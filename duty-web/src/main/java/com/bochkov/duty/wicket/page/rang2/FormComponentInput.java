package com.bochkov.duty.wicket.page.rang2;

import com.bochkov.bootstrap.FormComponentErrorBehavior;
import com.bochkov.duty.jpa.entity.AbstractEntity;
import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.wicket.data.model.PersistableModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IModelComparator;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;

import javax.inject.Inject;

@Accessors(chain = true)
public class FormComponentInput extends FormComponentPanel<Rang> {

    @Inject
    RangRepository repository;

    @Getter
    @Setter
    boolean canSelect = false;

    IModel<Rang> selectedEntity = PersistableModel.of(repository::findById);

    FormComponent<String> name = new TextField<>("name", Model.of(), String.class).setRequired(true);

    FormComponent<String> fullName = new TextField<>("fullName", Model.of(), String.class).setRequired(true);


    FormComponent<Rang> select = new SelectRang("select", selectedEntity);

    FormComponent<Rang> id = new TextField<Rang>("id", selectedEntity, Rang.class) {
        @Override
        protected void onConfigure() {
            super.onConfigure();
            setEnabled(!getModel().isPresent().getObject());
        }
    };


    public FormComponentInput(String id, IModel<Rang> model) {
        super(id, model);
    }

    public FormComponentInput(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
        select.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(FormComponentInput.this);
                setModelObject(select.getModelObject());
            }
        });
        add(name);
        add(select, id, fullName);
        add(new Label("createdDate", LambdaModel.of(getModel(), AbstractEntity::getCreatedDate)));

        FormComponentErrorBehavior.append(this);
    }

    @Override
    protected void onBeforeRender() {
        Rang rang = getModelObject();
        select.setModelObject(rang);
        name.setModelObject(getModel().map(Rang::getName).getObject());
        fullName.setModelObject(getModel().map(Rang::getFullName).getObject());
        super.onBeforeRender();
    }

    @Override
    public void convertInput() {
        Rang rang = select.getModelObject();
        if (rang == null) {
            rang = new Rang();
        }
        rang.setName(name.getConvertedInput());
        rang.setFullName(fullName.getConvertedInput());
        setConvertedInput(rang);
    }

    @Override
    public IModelComparator getModelComparator() {
        return IModelComparator.ALWAYS_FALSE;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (canSelect) {
            select.setVisible(true);
            id.setVisible(false);
            select.setEnabled(true);
            id.setEnabled(false);

        } else {
            select.setVisible(false);
            id.setVisible(true);
            select.setEnabled(false);
            id.setEnabled(true);
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

    }
}
