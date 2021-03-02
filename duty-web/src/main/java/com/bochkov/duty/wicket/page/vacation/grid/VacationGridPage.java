package com.bochkov.duty.wicket.page.vacation.grid;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.entity.VacationPK;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@MountPath("vacation-grid")
@Slf4j
public class VacationGridPage extends BootstrapPage<Integer> {


    IModel<Integer> yearModel = LoadableDetachableModel.of(() -> LocalDate.now().getYear());

    @Inject
    EmployeeRepository employeeRepository;


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
        IModel<List<Day>> days = CollectionModel.asList((SerializableFunction<LocalDate, Optional<Day>>) date -> dayRepository.findById(date));
        days.setObject(dayRepository.findAllByIdBetween(
                LocalDate.now().with(TemporalAdjusters.firstDayOfYear()),
                LocalDate.now().with(TemporalAdjusters.lastDayOfYear())));
        ListView<Employee> rows = new ListView<Employee>("employees", employeeRepository.findAll()) {
            @Override
            protected void populateItem(ListItem<Employee> employeeListItem) {
                employeeListItem.add(new Label("employee", employeeListItem.getModel().map(Employee::toString)));
                Vacation vacation = vacationRepository.findById(new VacationPK(yearModel.getObject(), employeeListItem.getModelObject())).orElse(null);
                employeeListItem.add(new VacationInputPanel("vacation", vacation, days));
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
