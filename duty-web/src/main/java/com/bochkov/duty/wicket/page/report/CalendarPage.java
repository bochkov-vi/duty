package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Roster;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.duty.wicket.page.day.calendar.CalendarPanel;
import com.bochkov.wicket.data.model.PersistableModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

@MountPath("report-calendar")
public class CalendarPage extends BootstrapPage<Month> {

    IModel<Roster> reportModel;

    @Inject
    ReportRepository reportRepository;


    public CalendarPage() {
        super();
    }

    public CalendarPage(PageParameters parameters) {
        this(ReportPage.extractFromParameters(parameters, Integer.class));
    }

    public CalendarPage(Integer id) {
        this();
        reportModel = PersistableModel.of(id, reportRepository::findById);
    }

    public CalendarPage(IModel<Roster> model) {
        this();
        reportModel = model;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (reportModel == null || !reportModel.isPresent().getObject()) {
            RequestCycle.get().setResponsePage(ReportPage.class);
            return;
        }
        IModel<Month> monthModel = LambdaModel.of(reportModel, report -> {
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
        });
        setModel(monthModel);
        add(new TabsNavidgationPanel("tabs", reportModel));
        add(new CalendarPanel("calendar", getModel()));
    }
}
