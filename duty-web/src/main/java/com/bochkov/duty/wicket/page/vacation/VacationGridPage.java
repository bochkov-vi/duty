package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.VacationPart;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;

@MountPath("vacation-grid")
@Slf4j
public class VacationGridPage extends BootstrapPage<Integer> {

    Form<VacationPart> form = new Form<VacationPart>("modal-form");

    WebMarkupContainer modalWindow = new WebMarkupContainer("modal-dialog");


    @Inject
    EmployeeRepository employeeRepository;

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
        modalWindow.add(form);
        modalWindow.setOutputMarkupId(true);
        form.setOutputMarkupId(true);
        modalWindow.add(new AjaxSubmitLink("btn-save", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                VacationPart vacation = form.getModelObject();
                log.debug("{}",vacation);
            }
        });
        form.setModel(new CompoundPropertyModel<>(Model.of()));
        add(modalWindow);
        if (getModel() == null) {
            setModel(Model.of());
        }
        if (!getModel().isPresent().getObject()) {
            setModelObject(LocalDate.now().plusYears(1).getYear());
        }
        List<Month> months = Lists.newArrayList(Month.values());
        ListView head1 = new ListView<Month>("head1", months) {
            @Override
            protected void populateItem(ListItem<Month> item) {
                item.add(new Label("cell", item.getModel().map(month -> LocalDate.now().withMonth(month.getValue())).map(d -> DateTimeFormatter.ofPattern("MMM").format(d))));
                item.add(new AttributeAppender("colspan", 2));
            }
        };
        List<Pair<LocalDate, LocalDate>> halfs = Lists.newArrayList();
        for (Month m : months) {
            LocalDate date1 = LocalDate.now().withMonth(m.getValue()).with(TemporalAdjusters.firstDayOfMonth());
            LocalDate date2 = date1.with(TemporalAdjusters.lastDayOfMonth());
            LocalDate mDate = date1.withDayOfMonth(15);
            halfs.add(Pair.of(date1, mDate));
            halfs.add(Pair.of(mDate, date2));
        }
        ListView head2 = new ListView<Pair<LocalDate, LocalDate>>("head2", halfs) {

            @Override
            protected void populateItem(ListItem<Pair<LocalDate, LocalDate>> item) {
                item.add(new Label("cell", item.getModel().map(Pair::getKey).map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM")))));
            }
        };

        ListView<Employee> rows = new ListView<Employee>("employee", employeeRepository.findAll()) {
            @Override
            protected void populateItem(ListItem<Employee> employeeListItem) {
                employeeListItem.add(new Label("employee", employeeListItem.getModel().map(Employee::toString)));

                ListView cells = new ListView<Pair<LocalDate, LocalDate>>("cells", halfs) {

                    @Override
                    protected void populateItem(ListItem<Pair<LocalDate, LocalDate>> item) {
                        item.setOutputMarkupId(true);
                        Employee employee = employeeListItem.getModelObject();
                        LocalDate date1 = item.getModel().map(Pair::getKey).getObject();
                        LocalDate date2 = item.getModel().map(Pair::getValue).getObject();
                        Boolean exists = vacationRepository.percentOverlap(date1, date2, employee) > 0.5;
                        VacationPart part = vacationRepository.findParts(date1, date2, employee).stream().findFirst().orElse(null);
                        Triple<Employee, Pair<LocalDate, LocalDate>, Boolean> value = Triple.of(employee, item.getModelObject(), exists);
                        IModel<Boolean> booleanIModel = Model.of(value.getRight());
                        item.add(new HiddenField<Boolean>("input", booleanIModel));
                    }
                };
                employeeListItem.add(cells);
            }
        };
        add(head1, head2, rows);
        form.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        //response.render(OnDomReadyHeaderItem.forScript(String.format("$('#%s').modal()", modalWindow.getMarkupId())));
    }
}
