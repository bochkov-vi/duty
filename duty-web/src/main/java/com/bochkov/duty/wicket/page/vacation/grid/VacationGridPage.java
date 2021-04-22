package com.bochkov.duty.wicket.page.vacation.grid;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.jpa.model.CollectionModel;
import com.bochkov.wicket.jpa.model.PersistableModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@MountPath("vacation-grid")
@Slf4j
public class VacationGridPage extends BootstrapPage<Integer> {


    IModel<Integer> yearModel = LoadableDetachableModel.of(() -> LocalDate.now().getYear());

    @Inject
    EmployeeRepository employeeRepository;

    Integer year = LocalDate.now().getYear();

    @Inject
    DayRepository dayRepository;

    @Inject
    VacationRepository vacationRepository;

    public VacationGridPage() {
    }

    public VacationGridPage(IModel<Integer> model) {
        super(model);
    }

    public VacationGridPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (getModel() == null) {
            setModel(Model.of());
        }
        if (!getModel().isPresent().getObject()) {
            setModelObject(LocalDate.now().plusYears(1).getYear());
        }

        ListView<Employee> rows = new ListView<Employee>("employees", employeeRepository.findAll()) {
            @Override
            protected void populateItem(ListItem<Employee> employeeListItem) {
                employeeListItem.add(new Label("employee", employeeListItem.getModel().map(Employee::toString)));
                IModel<List<Vacation>> collectionModel = CollectionModel.asList(pk -> vacationRepository.findById(pk));
                collectionModel.setObject(vacationRepository.findAllByPeriod(LocalDate.now().withYear(year).with(TemporalAdjusters.firstDayOfYear()),
                        LocalDate.now().withYear(year).with(TemporalAdjusters.lastDayOfYear()), employeeListItem.getModelObject()));
                employeeListItem.add(new VacationsListInputPanel("vacation", collectionModel, year, PersistableModel.of(id -> employeeRepository.findById(id))));
            }
        };
        add(rows);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        //response.render(OnDomReadyHeaderItem.forScript(String.format("$('#%s').modal()", modalWindow.getMarkupId())));
    }
}
