package com.bochkov.duty.wicket.page.report;

import com.bochkov.bootstrap.ActiveLinkBehavior;
import com.bochkov.duty.jpa.entity.Report;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class TabsNavidgationPanel extends GenericPanel<Report> {

    public TabsNavidgationPanel(String id, IModel<Report> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new BookmarkablePageLink<Report>("main-data", ReportPage.class,
                ReportPage.pageParameters(getModel()))
                .add(ActiveLinkBehavior.of()));
        add(new BookmarkablePageLink<Report>("calendar", CalendarPage.class,
                ReportPage.pageParameters(getModel()))
                .add(ActiveLinkBehavior.of()));
        add(new BookmarkablePageLink<Report>("shiftTypes", ShiftGridPage.class,
                ReportPage.pageParameters(getModel()))
                .add(ActiveLinkBehavior.of()));

    }

    @Override
    public boolean isVisible() {
        return getModel().map(Report::getId).isPresent().getObject();
    }
}
