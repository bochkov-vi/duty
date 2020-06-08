package com.bochkov.duty.wicket.page.rostering;

import com.bochkov.bootstrap.tab.TabbedPanel;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.duty.wicket.page.day.calendar.CalendarPanel;
import com.bochkov.wicket.component.toast.ToastFeedbackPanel;
import com.bochkov.wicket.data.model.PersistableModel;
import com.google.common.collect.Lists;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@MountPath("rostering")
public class ShiftRosteringPage extends BootstrapPage<Report> {

    @SpringBean
    ReportRepository repository;
    ToastFeedbackPanel feedbackPanel = new ToastFeedbackPanel("feedback");
    IModel<Integer> selectedTab = Model.of(0);
    TabbedPanel tabbedPanel;

    public ShiftRosteringPage(PageParameters parameters) {
        super(parameters);
    }

    public ShiftRosteringPage(Report report) {
        setModel(PersistableModel.of(report, repository::findById));
    }

    public ShiftRosteringPage(Integer idReport) {
        setModel(PersistableModel.of(idReport, repository::findById));
    }

    public ShiftRosteringPage() {
        setModel(PersistableModel.of(repository::findById, Report::new));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(feedbackPanel);

        tabbedPanel = new TabbedPanel<ITab>("tabs-panel", createTabs(), selectedTab);
        tabbedPanel.setOutputMarkupId(true);
        add(tabbedPanel);
    }

    public List<ITab> createTabs() {
        List<ITab> tabs = Lists.newArrayList();
        tabs.add(createMainReportTab());
        tabs.add(createCalendarTab());
        return tabs;
    }

    public ITab createMainReportTab() {
        ITab tab = new AbstractTab(new ResourceModel("report.main-data")) {
            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new MainReportDataPanel(panelId, getModel()) {
                    @Override
                    public void onUpdate(AjaxRequestTarget target) {
                        super.onUpdate(target);
                        target.add(tabbedPanel);
                        target.add(feedbackPanel);

                    }
                };
            }
        };
        return tab;
    }

    public ITab createCalendarTab() {
        ITab tab = new AbstractTab(new ResourceModel("report.calendar")) {
            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new CalendarPanel(panelId, LambdaModel.of(getModel(), report -> {
                    LocalDate date = report.getDateFrom();
                    if (date == null) {
                        date = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                        report.setDateFrom(date);
                        report.setDateTo(date.with(TemporalAdjusters.lastDayOfMonth()));
                    }
                    Month month = date.getMonth();
                    return month;
                }, (report, month) -> {
                    if (month != null) {
                        LocalDate date = LocalDate.now().with(month).with(TemporalAdjusters.firstDayOfMonth());
                        report.setDateFrom(date);
                        report.setDateTo(date.with(TemporalAdjusters.lastDayOfMonth()));
                    }
                }));
            }
        };
        return tab;
    }
}
