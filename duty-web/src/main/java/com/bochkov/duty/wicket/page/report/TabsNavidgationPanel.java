package com.bochkov.duty.wicket.page.report;

import com.bochkov.bootstrap.ActiveLinkBehavior;
import com.bochkov.duty.jpa.entity.Report;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;

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
                Report entity1 = TabsNavidgationPanel.this.getModelObject();
                Report entity2 = getModelObject();

                RequestCycle.get().setResponsePage(new ReportPage(getModelObject()).setEditMode(true));
            }
        }.add(ActiveLinkBehavior.of(ReportPage.class)));
        add(new Link<Report>("calendar", getModel()) {
            @Override
            public void onClick() {
                RequestCycle.get().setResponsePage(new CalendarPage(getModel()));
            }
        }.add(ActiveLinkBehavior.of(CalendarPage.class)));

    }
}
