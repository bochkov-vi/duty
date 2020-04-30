package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
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
import java.util.List;
import java.util.stream.Collectors;

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
        List<Pair<Month, Integer>> halfs = Lists.newArrayList();
        for (Month m : months) {
            halfs.add(Pair.of(m, 1));
            halfs.add(Pair.of(m, 2));
        }
        ListView head2 = new ListView<Pair<Month, Integer>>("head2", halfs) {

            @Override
            protected void populateItem(ListItem<Pair<Month, Integer>> item) {
                item.add(new Label("cell", item.getModel().map(Pair::getValue)));
            }
        };

        ListView<Employee> rows = new ListView<Employee>("employee", employeeRepository.findAll()) {
            @Override
            protected void populateItem(ListItem<Employee> item) {
                item.add(new Label("employee", item.getModel().map(Employee::toString)));
                ListView<Triple<Employee, Month, Integer>> cells = new ListView("cells", halfs.stream().map(pair -> Triple.of(item.getModelObject(), pair.getKey(), pair.getValue())).collect(Collectors.toList())) {
                    @Override
                    protected void populateItem(ListItem item) {
                        item.add(new HiddenField<Boolean>())
                    }
                };
                item.add(cells);
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
