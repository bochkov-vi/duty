package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.provider.ListModelDataProvider;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true)
public class ShiftGridPanel extends GenericPanel<Collection<ShiftType>> {

    @SpringBean
    ShiftTypeRepository shiftTypeRepository;

    @SpringBean
    DayRepository dayRepository;

    @Getter
    @Setter
    LocalDate dateFrom;

    @Getter
    @Setter
    LocalDate dateTo;

    List<ICellPopulator<ShiftType>> cellPopulators;

    public ShiftGridPanel(String id, IModel<Collection<ShiftType>> model) {
        super(id, model);
    }

    public ShiftGridPanel(String id, IModel<Collection<ShiftType>> model, LocalDate dateFrom, LocalDate dateTo) {
        super(id, model);
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        cellPopulators = cellPopulators();
        DataGridView<ShiftType> gridView = new DataGridView<ShiftType>("rows", cellPopulators, dataProvider());
        add(gridView);
    }

    protected IDataProvider<ShiftType> dataProvider() {
        return new ListModelDataProvider<ShiftType>(getModel()) {
            @Override
            public IModel<ShiftType> model(ShiftType object) {
                return PersistableModel.of(object, shiftTypeRepository::findById);
            }
        };
    }

    protected List<ICellPopulator<ShiftType>> cellPopulators() {
        List<ICellPopulator<ShiftType>> result = Lists.newArrayList();
        result.addAll(
                dayRepository.findOrCreate(dateFrom, dateTo).stream().map(this::cellPopulator).collect(Collectors.toList())
        );
        return result;
    }

    protected ICellPopulator<ShiftType> cellPopulator(IModel<Day> day) {
        return new ShiftTypeCellPolulator(day);
    }

    protected ICellPopulator<ShiftType> cellPopulator(LocalDate date) {
        return new ShiftTypeCellPolulator(PersistableModel.of(date, dayRepository::findById));
    }

    protected ICellPopulator<ShiftType> cellPopulator(Day day) {
        return new ShiftTypeCellPolulator(PersistableModel.of(day, dayRepository::findById));
    }

    static class ShiftTypeCellPolulator implements ICellPopulator<ShiftType> {

        IModel<Day> dayModel;

        public ShiftTypeCellPolulator(IModel<Day> dayModel) {
            this.dayModel = dayModel;
        }

        @Override
        public void populateItem(Item<ICellPopulator<ShiftType>> cellItem, String cellId, IModel<ShiftType> rowModel) {
            WebMarkupContainer cell = new WebMarkupContainer(cellId);
            cellItem.add(cell);
        }

        @Override
        public void detach() {

        }
    }
}
