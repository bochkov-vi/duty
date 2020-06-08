package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.base.DetailsPanel;
import com.bochkov.duty.wicket.base.EntityPage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@MountPath("report")
public class ReportPage extends EntityPage<Report, Integer> {
    @SpringBean
    private ReportRepository repository;

    public ReportPage() {
    }

    public ReportPage(Report entity) {
        super(entity);
    }

    @Override
    protected void onInitialize() {
        this.setModalMode(false);
        super.onInitialize();
        add(new TabsNavidgationPanel("tabs", getModel()));
    }

    @Override
    protected List<IColumn<Report, String>> columns() {
        List<IColumn<Report, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<Report, String>(new ResourceModel("report.id"), "id", Report::getId));
        List<IColumn<Report, String>> defaultColumns = this.reflectiveColumns(Report.class, "report.",
                "date",
                "dateFrom",
                "dateTo",
                "chief",
                "executor",
                "title",
                "dateTitle",
                "genitiveDepartment");
        list.addAll(defaultColumns);
        return list;
    }


    @Override
    protected ReportRepository getRepository() {
        return repository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Report> model) {
        return new ReportInputPanel(id, model);
    }

    @Override
    protected GenericPanel<Report> createDetailsPanel(String id, IModel<Report> model) {
        return new DetailsPanel<Report>(id, getModel(), ImmutableList.of("id"), "report.");
    }

    @Override
    protected Report newInstance() {
        return new Report();
    }
}
