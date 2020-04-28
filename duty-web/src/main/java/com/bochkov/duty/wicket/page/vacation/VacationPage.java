package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.jpa.entity.VacationPK;
import com.bochkov.duty.jpa.repository.VacationRepository;
import com.bochkov.duty.wicket.base.DetailsPanel;
import com.bochkov.duty.wicket.base.EntityPage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@MountPath("vacation")
@Getter
public class VacationPage extends EntityPage<Vacation, VacationPK> {

    @SpringBean
    private VacationRepository repository;

    public VacationPage() {
    }

    public VacationPage(Vacation entity) {
        super(entity);
    }

    @Override
    protected List<IColumn<Vacation, String>> columns() {
        List<IColumn<Vacation, String>> list = Lists.newArrayList();
        list.add(new PropertyColumn<Vacation, String>(new ResourceModel("vacation.employee"), "employee.lastName", "employee"));
        list.add(new PropertyColumn<Vacation, String>(new ResourceModel("vacation.year"), "id.year", "id.year"));
        list.add(new PropertyColumn<Vacation, String>(new ResourceModel("vacation.parts"), null, "parts"));
        return list;
    }


    @Override
    protected WebMarkupContainer createInputPanel(String id, IModel<Vacation> model) {
        return new VacationInputPanel(id, model);
    }

    @Override
    protected GenericPanel<Vacation> createDetailsPanel(String id, IModel<Vacation> model) {
        return new DetailsPanel<Vacation>(id, getModel(), ImmutableList.of("employee", "id.year", "parts"), "vacation.");
    }

    @Override
    protected Vacation newInstance() {
        return new Vacation(new VacationPK()).setParts(Sets.newTreeSet());
    }
}
