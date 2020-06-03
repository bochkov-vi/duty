package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Report;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class ReportInputPanel extends GenericPanel<Report> {

    public ReportInputPanel(String id) {
        super(id);
    }

    public ReportInputPanel(String id, IModel<Report> model) {
        super(id, model);
    }

}
