package com.bochkov.duty.wicket.page.planing;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.model.nonser.CollectionModel;
import com.bochkov.wicket.data.provider.SortedListModelDataProvider;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.util.List;

@MountPath("planing/emploee")
public class PlaningEmployeePage extends BasePlaningPage<List<Employee>> {
    @Inject
    EmployeeRepository employeeRepository;

    public PlaningEmployeePage() {
        super();
        setModel(CollectionModel.asList(employeeRepository::findById));
    }

    public PlaningEmployeePage(IModel<List<Employee>> model) {
        super(model);
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        ISortableDataProvider<Employee, ?> provider = provider();
        DataTable<Employee, ?> table = new DataTable("table", columns(), provider, 100);
        table.addTopToolbar(new HeadersToolbar(table, provider));
        table.addBottomToolbar(new NoRecordsToolbar(table, new ResourceModel("datatable.no-records-found")));
        add(table);
    }

    List<IColumn<Employee, ?>> columns() {
        List<IColumn<Employee, ?>> columns = Lists.newArrayList();
        return columns;
    }

    ISortableDataProvider<Employee, ?> provider() {
        ISortableDataProvider<Employee, ?> provider = new SortedListModelDataProvider<Employee>(getModel()) {
            @Override
            public IModel<Employee> model(Employee object) {
                return PersistableModel.of(object, employeeRepository::findById);
            }
        };
        return provider;
    }

}
