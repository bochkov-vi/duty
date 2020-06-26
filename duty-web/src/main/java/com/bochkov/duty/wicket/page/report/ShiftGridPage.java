package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.model.PersistableModel;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Optional;

@MountPath("shift-grid")
public class ShiftGridPage extends BootstrapPage<Report> {

    @SpringBean
    ReportRepository reportRepository;

    @SpringBean
    DayRepository dayRepository;

    @SpringBean
    ShiftTypeRepository shiftTypeRepository;

    public ShiftGridPage(Report report) {
        super();
        PersistableModel model = PersistableModel.of(report, reportRepository::findById);
        setModel(model);
    }

    public ShiftGridPage(Integer pk) {
        super();
        setModel(PersistableModel.of(pk, reportRepository::findById));
    }

    public ShiftGridPage(PageParameters parameters) {
        super(parameters);
        Integer pk = Optional.ofNullable(getPageParameters().get(0).toOptionalInteger()).orElseGet(() ->
                getPageParameters().get("id").toOptionalInteger());
        setModel(PersistableModel.of(pk, reportRepository::findById));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (!getModel().isPresent().getObject()) {
            RequestCycle.get().setResponsePage(ReportPage.class);
            return;
        }
        add(new TabsNavidgationPanel("tabs", getModel()));
        Form form = new Form<Void>("shift-form") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                this.visitChildren(Fragment.class, (cmp, obj) -> {
                    Fragment fragment = (Fragment) cmp;

                });
            }
        };
        add(form);

        ShiftGridPanel<ShiftType> grid = new ShiftGridPanel<ShiftType>("grid",
                LoadableDetachableModel.of(shiftTypeRepository::findAll),
                getModel().map(Report::getDateFrom).getObject(),
                getModel().map(Report::getDateTo).getObject()) {
            @Override
            public IModel<ShiftType> model(ShiftType object) {
                return PersistableModel.of(object, shiftTypeRepository::findById);
            }

            @Override
            public void populateItem(Item<ICellPopulator<ShiftType>> cellItem, String componentId, IModel<ShiftType> rowModel, IModel<Day> date) {
                Fragment fragment = new Fragment(componentId, "cell-input", getPage());
                fragment.add(new TextField<Integer>("input", Model.of(), Integer.class));
                fragment.add(new HiddenField<Day>("day", date, Day.class));
                fragment.add(new HiddenField<ShiftType>("shiftType", rowModel, ShiftType.class));
                cellItem.add(fragment);
            }
        };

        grid.setFirstColumns(Lists.newArrayList(new LambdaColumn<ShiftType, String>(
                new ResourceModel("shiftType"),
                ShiftType::toString
        ) {

            @Override
            public String getCssClass() {
                return "text-nowrap";
            }

            @Override
            public int getHeaderRowspan() {
                return 2;
            }
        }));
        form.add(grid);
    }

}
