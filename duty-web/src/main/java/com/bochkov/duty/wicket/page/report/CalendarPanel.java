package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Report;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class CalendarPanel extends GenericPanel<Report> {

    public CalendarPanel(String id) {
        super(id);
    }

    public CalendarPanel(String id, IModel<Report> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        LocalDate start = getModel().map(Report::getDateFrom).map(ld -> ld.with(DayOfWeek.MONDAY)).getObject();
        LocalDate end = getModel().map(Report::getDateTo).map(ld -> ld.with(DayOfWeek.SUNDAY)).getObject();


        List<TreeMap<Integer, LocalDate>> rows = Lists.newArrayList();
        int w = start.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        TreeMap<Integer, LocalDate> row = Maps.newTreeMap();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            int i = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
            if (i != w) {
                row = Maps.newTreeMap();
                rows.add(row);
            }
            row.put(date.getDayOfWeek().getValue(), date);
            w = i;
        }

        ListDataProvider<TreeMap<Integer, LocalDate>> provider = new ListDataProvider<TreeMap<Integer, LocalDate>>(rows);
        List<IColumn<TreeMap<Integer, LocalDate>, String>> columns = Lists.newArrayList(

        );
        DataTable table = new DataTable<TreeMap<Integer, LocalDate>,String>("table", columns, provider, 10);
        add(table);
    }

}
