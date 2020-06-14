package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.model.PersistableModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

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
        ShiftGridPanel<ShiftType> grid = new ShiftGridPanel<ShiftType>("grid",
                LoadableDetachableModel.of(shiftTypeRepository::findAll),
                getModel().map(Report::getDateFrom).getObject(),
                getModel().map(Report::getDateTo).getObject()) {
            @Override
            public IModel<ShiftType> model(ShiftType object) {
                return PersistableModel.of(object, shiftTypeRepository::findById);
            }
        };
        add(grid);
    }

}
