package com.bochkov.duty.wicket.page.rostering;

import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.page.report.ReportInputPanel;
import lombok.experimental.Accessors;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@Accessors(chain = true)
public class MainReportDataPanel extends GenericPanel<Report> {
    @SpringBean
    ReportRepository reportRepository;

    FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");

    Form<Report> form = new Form<Report>("form") {
        @Override
        protected void onSubmit() {
            super.onSubmit();
            try {
                Report report = reportRepository.save(getModelObject());
                setModelObject(null);
                setModelObject(report);
                info("График успешно сохранен");
            } catch (Exception e) {
                error(e.getLocalizedMessage());
            }
        }
    };


    public MainReportDataPanel(String id, IModel<Report> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        form.add(feedbackPanel);
        feedbackPanel.setOutputMarkupId(true);
        add(form);
        form.setModel(new CompoundPropertyModel<>(getModel()));
        form.setOutputMarkupId(true);
        form.add(new AjaxButton("btn-save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                target.add(feedbackPanel);
            }
        });
        form.add(new ReportInputPanel("input-panel", getModel()));
    }

    public void onUpdate(AjaxRequestTarget target) {

    }
}
