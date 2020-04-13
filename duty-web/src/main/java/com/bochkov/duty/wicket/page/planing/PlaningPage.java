package com.bochkov.duty.wicket.page.planing;

import com.bochkov.duty.jpa.entity.Duty;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.DutyRepository;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.model.PersistableModel;
import lombok.Getter;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Collection;

@MountPath("planing")
public class PlaningPage extends BootstrapPage<Report> {
    @SpringBean
    DayRepository dayRepository;

    @SpringBean
    DutyRepository dutyRepository;

    @SpringBean
    @Getter
    ReportRepository reportRepository;


    public PlaningPage(IModel<Report> model) {
        super(model);
    }

    public PlaningPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Integer id = getPageParameters().get("id").toOptionalInteger();
        if (getModel() == null) {
            setModel(PersistableModel.of(reportRepository::findById));
        }
        if (id != null && reportRepository.existsById(id)) {
            setModelObject(reportRepository.findById(id).orElse(null));
        }
        Form form = new Form<Void>("form");
        add(form);
        DutyGrid2 dutyGrid = new DutyGrid2("duty-grid", getModel().map(Report::getPersons), getModel().map(Report::getDateFrom), getModel().map(Report::getDateTo));
        form.add(dutyGrid);
        dutyGrid.setOutputMarkupId(true);
        form.add(new AjaxButton("btn-save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                target.add(dutyGrid);
                Collection c = dutyGrid.getModelObject();
                form.visitChildren(DutyCellEditor.class, new IVisitor<DutyCellEditor, Duty>() {

                    @Override
                    public void component(DutyCellEditor object, IVisit<Duty> visit) {
                        Duty duty =object.getModelObject();
                        System.out.println(duty);
                    }
                });
            }

        }.setOutputMarkupId(true));
    }


}
