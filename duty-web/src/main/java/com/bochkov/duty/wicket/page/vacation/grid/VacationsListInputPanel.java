package com.bochkov.duty.wicket.page.vacation.grid;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.fontawesome.FontAwesomeBehavior;
import com.bochkov.wicket.jpa.model.PersistableModel;
import com.google.common.collect.Lists;
import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class VacationsListInputPanel extends GenericPanel<List<Vacation>> {

    Integer year = LocalDate.now().getYear();

    @SpringBean
    EmployeeRepository employeeRepository;

    IModel<Employee> employeeModel = PersistableModel.of(id -> employeeRepository.findById(id));

    @SpringBean
    DayRepository dayRepository;

    @SpringBean
    VacationRepository repository;


    SerializableSupplier<Vacation> newVacationSupplier = new SerializableSupplier<Vacation>() {
        @Override
        public Vacation get() {

            Vacation vacation = null;

            vacation = new Vacation().setStart(LocalDate.now()).setEnd(LocalDate.now().plusDays(30)).setEmployee(employeeModel.getObject());

            return vacation;
        }
    };

    IModel<Vacation> newVacationModel = PersistableModel.of(id -> repository.findById(id), newVacationSupplier);

    public VacationsListInputPanel(String id, IModel<List<Vacation>> model, int year, IModel<Employee> employeeModel) {
        super(id, model);
        this.year = year;
        this.employeeModel = employeeModel;
    }

    @Override
    protected void onInitialize() {
        RepeatingView repeatingView = new RepeatingView("vacations");
        super.onInitialize();
        setOutputMarkupId(true);
        add(new AjaxLink<Void>("add-new") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                repeatingView.add(new VacationInputPanel(repeatingView.newChildId(), PersistableModel.of(id -> repository.findById(id), newVacationSupplier)));
                target.add(VacationsListInputPanel.this);
            }
        });
        for (Vacation vacation : getModelObject()) {
            repeatingView.add(new VacationInputPanel(repeatingView.newChildId(), PersistableModel.of(vacation, (SerializableFunction<Long, Optional<Vacation>>) id -> repository.findById(id))));
        }
        //listView.setReuseItems(true);
        add(repeatingView);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("jquery-ui/current/jquery-ui.min.js"){
            @Override
            public List<HeaderItem> getDependencies() {
                return Lists.newArrayList(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
            }
        })));
        response.render(CssHeaderItem.forReference(new WebjarsCssResourceReference("jquery-ui/current/jquery-ui.min.css")));
        response.render(CssHeaderItem.forReference(FontAwesomeBehavior.CSS));
    }
}
