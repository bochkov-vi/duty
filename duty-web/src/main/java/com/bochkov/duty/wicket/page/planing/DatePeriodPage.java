package com.bochkov.duty.wicket.page.planing;

import com.bochkov.bootstrap.tempusdominus.LocalDateField;
import com.google.common.collect.Range;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@MountPath("planing/period")
public class DatePeriodPage extends BasePlaningPage<Void> {

    IModel<LocalDate> dateFrom = Model.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
    IModel<LocalDate> dateTo = Model.of(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));

    public DatePeriodPage() {
        super();
    }

    public DatePeriodPage(IModel<LocalDate> dateFrom, IModel<LocalDate> dateTo) {
        super();
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public DatePeriodPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        LocalDateField dateFromField = new LocalDateField("dateFrom",
                dateFrom,
                getString("datePattern"));
        LocalDateField dateToField = new LocalDateField("dateTo", dateTo, getString("datePattern"));
        Form form = new Form<Range<LocalDate>>("form") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                RequestCycle.get().setResponsePage(PlaningEmployeePage.class);
            }
        };
        add(form);
        form.add(dateFromField);
        form.add(dateToField);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        dateFrom.detach();
        dateTo.detach();
    }
}
