package com.bochkov.duty.wicket.page.vacation.grid;

import com.bochkov.duty.jpa.entity.Day;
import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.wicket.data.model.PersistableModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;
import java.util.List;

public class VacationInputPanel extends GenericPanel<Vacation> {

    Integer year = LocalDate.now().getYear();

    @SpringBean
    DayRepository dayRepository;

    @SpringBean
    VacationRepository repository;

    IModel<List<Day>> daysModel;

    public VacationInputPanel(String id, Vacation vacation, IModel<List<Day>> daysModel) {
        super(id);
        IModel<Vacation> model = PersistableModel.of(pk -> repository.findById(pk));
        model.setObject(vacation);
        setModel(model);
        this.daysModel = daysModel;
    }

    public VacationInputPanel(String id, IModel<Vacation> model) {
        super(id, model);

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();


        ListView<Day> listView = new ListView<Day>("days", daysModel) {
            @Override
            protected void populateItem(ListItem<Day> item) {
                item.add(new DayCell("cell", item.getModel()));
            }
        };
        listView.setReuseItems(true);
        add(listView);
    }
}
