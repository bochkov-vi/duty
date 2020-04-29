package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.provider.PersistableDataProvider;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.data.jpa.domain.Specification;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

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
        ISortableDataProvider<Vacation, String> provider = provider();
        DataTable table = new DataTable("table", columns(), provider, 1000);
        table.addTopToolbar(new HeadersToolbar<String>(table, provider));
        table.addBottomToolbar(new NoRecordsToolbar(table));
        add(table);

    }

    ISortableDataProvider<Vacation, String> provider() {
        ISortableDataProvider<Vacation, String> provider = PersistableDataProvider.of(() -> vacationRepository, this::specification);
        return provider;
    }

    Specification<Vacation> specification() {
        return (r, q, b) -> b.equal(r.get("id").get("year"), getModelObject());
    }

    List<IColumn<Vacation, String>> columns() {
        List<IColumn<Vacation, String>> list = Lists.newArrayList();
        for (int month = Month.JANUARY.getValue(); month <= Month.DECEMBER.getValue(); month++) {
            list.add(new HalfMonthColumn(1,))
        }
        return list;
    }

    static class HalfMonthColumn extends AbstractColumn<Vacation, String> {

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
    }
}
