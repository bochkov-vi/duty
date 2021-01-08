package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.entity.VacationPK;
import com.bochkov.duty.jpa.entity.VacationPart;
import com.bochkov.wicket.data.provider.SortedListModelDataProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeaderlessColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class VacationPartCollectionInputPanel extends FormComponentPanel<Set<VacationPart>> {

    ListModel<VacationPart> vacationPartsModel = new ListModel<>(Lists.newArrayList());

    Form<Void> form = new Form<>("form");

    IModel<Vacation> vacationModel;

    public VacationPartCollectionInputPanel(String id, IModel<Vacation> vacationModel) {
        super(id);
        this.vacationModel = vacationModel;
    }

    public VacationPartCollectionInputPanel(String id, IModel<Set<VacationPart>> model, IModel<Vacation> vacationModel) {
        super(id, model);
        this.vacationModel = vacationModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(form);
        form.setOutputMarkupId(true);
        DataTable<VacationPart, String> table = new DefaultDataTable<VacationPart, String>("parts-table"
                , Lists.newArrayList(
                new PropertyColumn<>(new ResourceModel("vacation.part.partNumber"), "partNumber", "partNumber"),
                new PropertyColumn<>(new ResourceModel("vacation.part.start"), "start", "start"),
                new PropertyColumn<>(new ResourceModel("vacation.part.end"), "end", "end"),
                new HeaderlessColumn<VacationPart, String>() {
                    @Override
                    public void populateItem(Item cellItem, String componentId, IModel rowModel) {
                        cellItem.add(new AjaxLink<VacationPart>(componentId, rowModel) {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                VacationPart part = getModelObject();
                                Set<VacationPart> set = Sets.newTreeSet(vacationPartsModel.getObject());
                                set.remove(part);
                                AtomicInteger i = new AtomicInteger(1);
                                set.forEach(p -> p.setPartNumber(i.getAndIncrement()));
                                vacationPartsModel.setObject(Lists.newArrayList(set));
                                target.add(form);
                            }
                        }.setBody(Model.of("<i class='fa fa-close'></i>")).setEscapeModelStrings(false));

                    }
                })
                ,
                new SortedListModelDataProvider<VacationPart>(vacationPartsModel) {
                    @Override
                    public IModel<VacationPart> model(VacationPart object) {
                        return Model.of(object);
                    }
                },
                50);
        table.setOutputMarkupId(true);
        form.add(table);
        VacationPartInputPanel input = new VacationPartInputPanel("part-input", Model.of());
        form.add(input);
        form.add(new AjaxButton("btn-plus") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                VacationPart part = input.getModelObject();
                part.setVacation(vacationModel.getObject());
                part.getId().setIdEmployeer(vacationModel.map(Vacation::getId).map(VacationPK::getIdEmployeer).getObject());
                part.getId().setYear(vacationModel.map(Vacation::getId).map(VacationPK::getYear).getObject());
                Set<VacationPart> set = Sets.newTreeSet(vacationPartsModel.getObject());
                set.add(part);
                AtomicInteger i = new AtomicInteger(1);
                set.forEach(p -> p.setPartNumber(i.getAndIncrement()));
                vacationPartsModel.setObject(Lists.newArrayList(set));
                target.add(form);
            }
        });


    }

    @Override
    protected void onBeforeRender() {
        vacationPartsModel.setObject(Lists.newArrayList(getModel().getObject()));
        super.onBeforeRender();
    }

    @Override
    public void convertInput() {
        setConvertedInput(Sets.newTreeSet(vacationPartsModel.getObject()));
    }

    @Override
    protected void onDetach() {
        vacationPartsModel.detach();
        super.onDetach();
    }
}
