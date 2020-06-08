package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.duty.wicket.page.day.calendar.CalendarPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class CalendarPage extends BootstrapPage<Month> {

    IModel<Report> reportModel;

    public CalendarPage(IModel<Report> model) {
        super(LambdaModel.of(model, report -> {
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

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new TabsNavidgationPanel("tabs", reportModel));
        add(new CalendarPanel("calendar", getModel()));
    }
}
