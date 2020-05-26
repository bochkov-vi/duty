package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
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
public class VacationGridPage extends BootstrapPage<Integer> {
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
            LocalDate mDate = date1.plusDays(ChronoUnit.DAYS.between(date1, date2) / 2-1);
            halfs.add(Pair.of(date1, mDate));
            halfs.add(Pair.of(mDate.plusDays(1), date2));
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
                        Employee employee = employeeListItem.getModelObject();
                        LocalDate date1 = item.getModel().map(Pair::getKey).getObject();
                        LocalDate date2 = item.getModel().map(Pair::getValue).getObject();
                        Boolean value = vacationRepository.percentOverlap(date1, date2, employee)>0.5;
                        item.add(new ClassAttributeModifier() {
                            @Override
                            protected Set<String> update(Set<String> oldClasses) {
                                if(value){
                                    oldClasses.add("alert-danger");
                                }
                                return oldClasses;
                            }
                        });
                        item.add(new HiddenField<Boolean>("input", Model.of(value)));
                    }
                };
                employeeListItem.add(cells);
            }
        };
        add(head1, head2, rows);
    }

   /* ISortableDataProvider<Vacation, String> provider() {
        ISortableDataProvider<Vacation, String> provider = PersistableDataProvider.of(() -> vacationRepository, this::specification);
        return provider;
    }*/

  /*  Specification<Vacation> specification() {
        return (r, q, b) -> b.equal(r.get("id").get("year"), getModelObject());
    }*/

   /* List<IColumn<Vacation, String>> columns() {
        List<IColumn<Vacation, String>> list = Lists.newArrayList();
        for (int month = Month.JANUARY.getValue(); month <= Month.DECEMBER.getValue(); month++) {
            list.add(new HalfMonthColumn(1, ))
        }
        return list;
    }*/

    /*static class HalfMonthColumn extends AbstractColumn<Vacation, String> {

        Integer halfNumber;

        Month month;


        public HalfMonthColumn(IModel<String> displayModel, Integer halfNumber, Month month) {
            super(displayModel);
            this.halfNumber = halfNumber;
            this.month = month;
        }

        @Override
        public void populateItem(Item<ICellPopulator<Vacation>> cellItem, String componentId, IModel<Vacation> rowModel) {

        }
    }*/
}
