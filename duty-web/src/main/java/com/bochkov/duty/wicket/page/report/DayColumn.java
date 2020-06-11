package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Day;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import java.time.format.DateTimeFormatter;

@Accessors(chain = true)
public class DayColumn<T> extends AbstractColumn<T, String> {

    @Getter
    IModel<Day> dayModel;

    public DayColumn(IModel<Day> dayModel, String headerDateFormat) {
        super(dayModel.map(Day::getId).map(ld -> ld.format(DateTimeFormatter.ofPattern(headerDateFormat))));
        this.dayModel=dayModel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new Label(componentId, dayModel.map(Day::getId).map(ld -> ld.format(DateTimeFormatter.ofPattern("dd")))));
    }
}
