package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.duty.wicket.component.InputSpinner;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.provider.SortedListModelDataProvider;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDate;
import java.util.List;

@MountPath("report-shift-types")
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
        ISortableDataProvider<ShiftType, String> provider = provider();
        DataTable<ShiftType, String> table = new DataTable<ShiftType, String>("table", columns(), provider, 100);
        table.addTopToolbar(new HeadersToolbar<>(table, provider));
        table.addTopToolbar(new DayToolbar(table));

        add(table);
    }

    private List<? extends IColumn<ShiftType, String>> columns() {
        List<IColumn<ShiftType, String>> columns = Lists.newArrayList();
        IColumn<ShiftType, String> shiftTypeColumn = new PropertyColumn<ShiftType, String>(new ResourceModel("shiftType"), "name") {
            @Override
            public int getHeaderRowspan() {
                return 2;
            }
        };

        columns.add(shiftTypeColumn);
        columns.addAll(columns(getModel().map(Report::getDateFrom).getObject(), getModel().map(Report::getDateTo).getObject()));
        return columns;
    }

    private List<IColumn<ShiftType, String>> columns(LocalDate d1, LocalDate d2) {
        List<IColumn<ShiftType, String>> columns = Lists.newArrayList();
        dayRepository.findOrCreate(d1, d2).stream().map(day -> new DayColumn<ShiftType>(PersistableModel.of(day, dayRepository::findById), "dd.MM") {
            @Override
            public void populateItem(Item<ICellPopulator<ShiftType>> cellItem, String componentId, IModel<ShiftType> rowModel) {
                super.populateItem(cellItem,componentId,rowModel);
                //ShiftGridPage.this.populateCellItem(cellItem, componentId, rowModel);
            }
        }).forEach(col -> columns.add(col));

        return columns;
    }

    private void populateCellItem(Item<ICellPopulator<ShiftType>> cellItem, String componentId, IModel<ShiftType> rowModel) {
        cellItem.add(new InputSpinner(componentId));
    }

    ISortableDataProvider<ShiftType, String> provider() {
        ISortableDataProvider<ShiftType, String> provider = new SortedListModelDataProvider<ShiftType>(LoadableDetachableModel.of(shiftTypeRepository::findAll)) {
            @Override
            public IModel<ShiftType> model(ShiftType object) {
                return PersistableModel.of(object, shiftTypeRepository::findById);
            }
        };

        return provider;
    }


}
