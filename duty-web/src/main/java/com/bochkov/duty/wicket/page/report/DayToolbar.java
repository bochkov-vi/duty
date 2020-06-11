package com.bochkov.duty.wicket.page.report;

import com.google.common.collect.Lists;
import org.apache.poi.ss.formula.functions.T;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Iterator;
import java.util.List;

public class DayToolbar extends AbstractToolbar {

    String pattern;

    public <T> DayToolbar(DataTable<T, String> table) {
        super(table);
    }

    public void populateItem(Item<IColumn<T, String>> item) {
        WebMarkupContainer header = new WebMarkupContainer("header");
        item.add(header);
        if (item.getModelObject() instanceof DayColumn) {
            populateHeaderDayColumn((DayColumn<T>) item.getModelObject(), header, "label");
        } else {
            populateHeaderColumn(item.getModelObject(), header, "label");
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        RefreshingView<IColumn<T, String>> headers = new RefreshingView<IColumn<T, String>>("headers") {
            @Override
            protected Iterator<IModel<IColumn<T, String>>> getItemModels() {
                List<IModel<IColumn<T, String>>> list = Lists.newArrayList();

                getTable().getColumns().stream().forEach(iColumn -> list.add((IModel<IColumn<T, String>>) Model.of((IColumn<T, String>)iColumn)));
                return list.iterator();
            }

            @Override
            protected void populateItem(Item<IColumn<T, String>> item) {
                DayToolbar.this.populateItem(item);
            }
        };
        add(headers);
    }

    public void populateHeaderColumn(IColumn<T, String> column, WebMarkupContainer item, String componentId) {
        item.add(column.getHeader(componentId));
    }

    public void populateHeaderDayColumn(DayColumn<T> column, WebMarkupContainer item, String componentId) {
        item.add(new Label(componentId, column.getDisplayModel()));
    }
}
