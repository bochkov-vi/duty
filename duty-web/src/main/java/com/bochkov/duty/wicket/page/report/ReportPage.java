package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.base.DetailsPanel;
import com.bochkov.duty.wicket.base.EntityPage;
import com.bochkov.duty.wicket.page.planing.PlaningPage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeaderlessColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@MountPath("report")
public class ReportPage extends EntityPage<Report, Integer> {
    @SpringBean
    private ReportRepository reportRepository;

    public ReportPage() {
    }

    public ReportPage(Report entity) {
        super(entity);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setModalSize(MODAL_LARGE);
    }

    @Override
    protected List<IColumn<Report, String>> columns() {
        List<IColumn<Report, String>> list = Lists.newArrayList();
        list.add(new LambdaColumn<Report, String>(new ResourceModel("report.id"), "id", Report::getId));
        list.add(new LambdaColumn<Report, String>(new ResourceModel("report.title"), "title", Report::getTitle));
        list.add(new HeaderlessColumn<Report, String>() {
            @Override
            public void populateItem(Item<ICellPopulator<Report>> cellItem, String componentId, IModel<Report> rowModel) {
                cellItem.add(new Link<Report>(componentId, rowModel) {
                    @Override
                    public void onClick() {
                        setResponsePage(new PlaningPage(getModel()));
                    }
                }.setBody(Model.of("<div class='btn btn-light border'><span class='fa fa-table'></span></div>")).setEscapeModelStrings(false));
            }
        });
        return list;
    }


    @Override
    protected ReportRepository getRepository() {
        return reportRepository;
    }

    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Report> model) {
        return new ReportInputPanel(id, model);
    }

    @Override
    protected GenericPanel<Report> createDetailsPanel(String id, IModel<Report> model) {
        return new DetailsPanel<Report>(id, getModel(), ImmutableList.of("id", "title", "createdDate"), "report.");
    }

    @Override
    protected Report newInstance() {
        return new Report();
    }

    @Override
    public void onEditLinkClick(AjaxRequestTarget target, IModel<Report> model) {
        RequestCycle.get().setResponsePage(ReportEditPage.class, new PageParameters().set("id", model.map(Report::getId).getObject()));
    }
}
