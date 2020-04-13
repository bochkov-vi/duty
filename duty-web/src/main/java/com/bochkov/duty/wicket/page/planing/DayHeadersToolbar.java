package com.bochkov.duty.wicket.page.planing;

import com.bochkov.duty.jpa.entity.Day;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class DayHeadersToolbar extends AbstractToolbar {
    public DayHeadersToolbar(DataTable<?, ?> table) {
        super(table);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        List<? extends IColumn<?, ?>> columns = getTable().getColumns();
        ListView listView = new ListView("header-cells", columns) {
            @Override
            protected void populateItem(ListItem item) {
                if (item.getModelObject() instanceof DutyGrid2.DayColumn) {
                    DutyGrid2.DayColumn dayColumn = (DutyGrid2.DayColumn) item.getModelObject();
                    item.add(new Label("cell", dayColumn.dayModel.map(Day::getId).map(d -> d.format(DateTimeFormatter.ofPattern("dd")))));
                } else {
                    item.add(new EmptyPanel("cell"));
                }
            }
        };
        add(listView);
    }
}
