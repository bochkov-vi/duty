package com.bochkov.duty.wicket.page.day;

import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.duty.wicket.page.day.calendar.CalendarPanel;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDate;
import java.time.Month;

@MountPath("calendar")
@Slf4j
public class CalendarPage extends BootstrapPage<Month> {

    public CalendarPage() {
        setModel(Model.of(LocalDate.now().getMonth()));
    }

    public CalendarPage(IModel<Month> model) {
        super(model);
    }

    public CalendarPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CalendarPanel("calendar", getModel()));
    }
}
