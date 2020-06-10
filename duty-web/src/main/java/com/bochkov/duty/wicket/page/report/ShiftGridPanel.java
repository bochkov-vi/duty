package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.provider.ListModelDataProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.compress.utils.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Accessors(chain = true)
public class ShiftGridPanel extends GenericPanel<Collection<Employee>> {

    @SpringBean
    EmployeeRepository employeeRepository;

    @Getter
    IModel<LocalDate> dateFromModel;

    @Getter
    IModel<LocalDate> dateToModel;


    public ShiftGridPanel(String id, IModel<Collection<Employee>> model) {
        super(id, model);
    }

    public ShiftGridPanel(String id, IModel<Collection<Employee>> model, IModel<LocalDate> dateFromModel, IModel<LocalDate> dateToModel) {
        super(id, model);
        this.dateFromModel = dateFromModel;
        this.dateToModel = dateToModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        DataGridView<Employee> gridView = new DataGridView<Employee>("rows", cellPopulators(), dataProvider());
        add(gridView);
    }

    protected IDataProvider<Employee> dataProvider() {
        return new ListModelDataProvider<Employee>(getModel()) {
            @Override
            public IModel<Employee> model(Employee object) {
                return PersistableModel.of(object, employeeRepository::findById);
            }
        };
    }

    protected List<ICellPopulator<Employee>> cellPopulators() {
        return Lists.newArrayList();
    }

    protected ICellPopulator<Employee> cellPopulator(Day day) {
        return new ICellPopulator<Employee>() {
            @Override
            public void populateItem(Item<ICellPopulator<Employee>> cellItem, String cellId, IModel<Employee> rowModel) {

            }

            @Override
            public void detach() {

            }
        };
    }
}
