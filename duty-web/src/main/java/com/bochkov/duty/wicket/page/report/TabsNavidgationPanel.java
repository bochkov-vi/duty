package com.bochkov.duty.wicket.page.report;

import com.bochkov.bootstrap.ActiveLinkBehavior;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.wicket.base.EntityPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.springframework.data.domain.Persistable;

public class TabsNavidgationPanel extends GenericPanel<Report> {

    public TabsNavidgationPanel(String id, IModel<Report> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Link<Report>("main-data", getModel()) {
            @Override
            public void onClick() {
                RequestCycle.get().setResponsePage(ReportPage.class, EntityPage.pageParameters(getModel().map(Persistable::getId), Integer.class));
            }
        }.add(ActiveLinkBehavior.of(ReportPage.class)));

        add(new Link<Report>("calendar", getModel()) {
            @Override
            public void onClick() {
                RequestCycle.get().setResponsePage(CalendarPage.class, EntityPage.pageParameters(getModel().map(Persistable::getId), Integer.class));
            }
        }.add(ActiveLinkBehavior.of(CalendarPage.class)));

        add(new Link<Report>("shiftTypes", getModel()) {
            @Override
            public void onClick() {
                RequestCycle.get().setResponsePage(ShiftGridPage.class, EntityPage.pageParameters(getModel().map(Persistable::getId), Integer.class));
            }
        }.add(ActiveLinkBehavior.of(ShiftGridPage.class)));


    }

    @Override
    public boolean isVisible() {
        return getModel().map(Report::getId).isPresent().getObject();
    }
}
