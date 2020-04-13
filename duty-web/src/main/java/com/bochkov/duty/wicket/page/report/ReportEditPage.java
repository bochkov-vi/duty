package com.bochkov.duty.wicket.page.report;

import com.bochkov.bootstrap.tab.TabbedPanel;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.model.PersistableModel;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class ReportEditPage extends BootstrapPage<Report> {
    @Inject
    ReportRepository reportRepository;

    public ReportEditPage(IModel<Report> model) {
        super(model);
    }

    public ReportEditPage(PageParameters parameters) {
        super(parameters);
        IModel<Report> model = PersistableModel.of(reportRepository::findById);
        setModel(model);
        Optional.ofNullable(parameters.get("id"))
                .filter(sv -> !sv.isEmpty())
                .map(sv -> sv.toOptional(Integer.class))
                .flatMap(id -> reportRepository.findById(id))
                .ifPresent(this::setModelObject);

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        List<ITab> tabs = Lists.newArrayList();

        TabbedPanel tabbedPanel = new TabbedPanel("tabs", tabs);
        add(tabbedPanel);
    }
}
