package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.entity.ShiftAssignment;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.jpa.repository.ShiftAssignmentRepository;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.duty.wicket.page.report.grid.GridPanel;
import com.bochkov.duty.wicket.page.report.grid.ShiftTypeEditor;
import com.bochkov.wicket.data.model.PersistableModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDate;
import java.util.Optional;

@MountPath("shift-grid")
public class ShiftGridPage extends BootstrapPage<Report> {

    @SpringBean
    ReportRepository reportRepository;

    @SpringBean
    DayRepository dayRepository;

    @SpringBean
    ShiftTypeRepository shiftTypeRepository;

    @SpringBean
    ShiftAssignmentRepository shiftAssignmentRepository;

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

        GridPanel grid = new GridPanel<Void>("grid", getModel().map(Report::getDateFrom).getObject(), getModel().map(Report::getEmployees)) {
            @Override
            public void pupulateCellItem(ListItem<Employee> cellItem, String compId, IModel<LocalDate> dateModel, IModel<Employee> employeeModel) {
                IModel<ShiftType> shiftTypeModel = LoadableDetachableModel.of(() -> shiftAssignmentRepository.findAll(dateModel.getObject(), employeeModel.getObject()).stream().findFirst().map(ShiftAssignment::getShiftType).orElse(null));
                // ShiftTypeLabel label = new ShiftTypeLabel(compId, shiftTypeModel);
                // cellItem.add(label);
                ShiftTypeEditor editor = new ShiftTypeEditor(compId, PersistableModel.of(shiftTypeModel.getObject(), shiftTypeRepository::findById));
                cellItem.add(editor);
            }
        };

        form.add(grid);
    }

}
