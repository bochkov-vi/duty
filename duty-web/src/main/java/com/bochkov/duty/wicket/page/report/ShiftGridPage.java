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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

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
        Integer pk = report.getId();
        setModel(LoadableDetachableModel.of(() -> reportRepository.findById(pk).get()));
    }

    public ShiftGridPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Integer pk = getPageParameters().get(0).toOptionalInteger();
        if (pk == null && pk <= 0) {
            pk = getPageParameters().get("id").toOptionalInteger();
        }
        if (pk != null && pk > 0) {
            setModel(PersistableModel.of(pk, reportRepository::findById));
        }


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
                fragment.add(new TextField<Integer>("input", Model.of()));
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
        add(grid);
    }

}
