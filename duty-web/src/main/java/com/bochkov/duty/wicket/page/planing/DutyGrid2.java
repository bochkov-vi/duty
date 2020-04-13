package com.bochkov.duty.wicket.page.planing;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Duty;
import com.bochkov.duty.jpa.entity.DutyPK;
import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.DutyRepository;
import com.bochkov.duty.jpa.repository.PersonRepository;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.provider.ListModelDataProvider;
import com.google.common.collect.Lists;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.AbstractExportableColumn;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DutyGrid2 extends GenericPanel<Collection<Duty>> {

    IModel<? extends Collection<Person>> personsModel;

    IModel<? extends Collection<Day>> daysModel;

    @Inject
    DayRepository dayRepository;

    @Inject
    PersonRepository personRepository;

    @Inject
    DutyRepository dutyRepository;

    public DutyGrid2(String id, IModel<? extends Collection<Person>> personsModel, IModel<? extends Collection<Day>> daysModel) {
        super(id);
        this.personsModel = personsModel;
        this.daysModel = daysModel;
    }

    public DutyGrid2(String id, IModel<? extends Collection<Person>> personsModel, IModel<LocalDate> dayFrom, IModel<LocalDate> dayTo) {
        super(id);
        this.personsModel = personsModel;
        this.daysModel = dayFrom.combineWith(dayTo, (f, t) -> dayRepository.findOrCreate(f, t));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(createTable("table"));
    }

    public DataTable<Person, String> createTable(String id) {
        List<IColumn<Person, String>> columns = Lists.newArrayList(
                new LambdaColumn<Person, String>(new ResourceModel("person"), p -> p.toString())
        );
        columns.addAll(dayColumns());
        IDataProvider<Person> provider = new ListModelDataProvider<Person>(personsModel) {
            @Override
            public IModel<Person> model(Person object) {
                return PersistableModel.of(object, personRepository::findById);
            }
        };
        DataTable<Person, String> table = new DataTable<Person, String>(id, columns, provider, Long.MAX_VALUE);
        table.addTopToolbar(new DayHeadersToolbar(table));
        return table;
    }

    public List<? extends IColumn<Person, String>> dayColumns() {
        return daysModel.map(c -> c.stream()
                .map(d -> new DayColumn(PersistableModel.of(d, dayRepository::findById)))
                .collect(Collectors.toList()))
                .getObject();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        daysModel.detach();
        personsModel.detach();
    }

    protected class DayColumn extends AbstractExportableColumn<Person, String> implements Serializable {
        IModel<Day> dayModel;

        public DayColumn(IModel<Day> dayModel) {
            super(dayModel.map(Day::toString));
            this.dayModel = dayModel;
        }

        @Override
        public IModel<Duty> getDataModel(IModel<Person> rowModel) {
            return rowModel.combineWith(dayModel, (p, d) -> dutyRepository.findById(new DutyPK(p.getId(), d.getId())).orElse(null));
        }

        @Override
        public void populateItem(Item<ICellPopulator<Person>> cellItem, String componentId, IModel<Person> rowModel) {
            IModel<Duty> dutyModel = getDataModel(rowModel);
            Duty duty = dutyModel.getObject();
            cellItem.add(new DutyCellEditor(componentId, rowModel.getObject(), dayModel.getObject()));
        }
    }
}
