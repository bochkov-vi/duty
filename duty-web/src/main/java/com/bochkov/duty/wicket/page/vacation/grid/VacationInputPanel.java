package com.bochkov.duty.wicket.page.vacation.grid;

import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.fontawesome.FontAwesomeBehavior;
import com.bochkov.wicket.jpa.model.PersistableModel;
import com.google.common.collect.Lists;
import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.Application;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class VacationInputPanel extends FormComponentPanel<Vacation> {

    LocalDate start;

    LocalDate end;


    @SpringBean
    VacationRepository repository;

    IModel<Vacation> vacationModel = PersistableModel.of((id) -> repository.findById(id));

    FormComponent<Vacation> idField = new HiddenField<>("pk", vacationModel);

    FormComponent<LocalDate> startField = new HiddenField<>("start", LocalDate.class);

    FormComponent<LocalDate> endField = new HiddenField<>("end", LocalDate.class);

    public VacationInputPanel(String id, IModel<Vacation> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setDefaultModel(new CompoundPropertyModel<>(this));
        container.add(idField, startField, endField);
        add(container);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        idField.setModelObject(getModelObject());
        startField.setModelObject(getModel().map(Vacation::getStart).getObject());
        endField.setModelObject(getModel().map(Vacation::getEnd).getObject());
    }

    @Override
    public void convertInput() {
        Vacation e = idField.getModelObject();

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(VacationInputPanel.class, "VacationInputPanel.js")));
        response.render(OnDomReadyHeaderItem.forScript(String.format("vacationInputPanel('%s')", getMarkupId())));
    }
}
