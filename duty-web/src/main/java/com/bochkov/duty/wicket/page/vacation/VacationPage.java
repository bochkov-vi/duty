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
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    protected void onInitialize() {
        super.onInitialize();
        setModalMode(false);
    }

    @Override
    protected List<IColumn<Vacation, String>> columns() {
        List<IColumn<Vacation, String>> list = Lists.newArrayList();
        list.add(new PropertyColumn<Vacation, String>(new ResourceModel("vacation.employee"), "employee.lastName", "employee"));
        list.add(new PropertyColumn<Vacation, String>(new ResourceModel("vacation.id.year"), "id.year", "id.year"));
        list.add(new LambdaColumn<Vacation, String>(new ResourceModel("vacation.parts"), null, v -> Optional.ofNullable(v.getParts()).map(Collection::stream).map(stream -> stream.map(p -> p.toString(DateTimeFormatter.ofPattern(getString("datePattern")))).filter(Objects::nonNull).collect(Collectors.joining("; "))).orElse(null)));
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

    @Override
    public void save(Vacation entity) {
        assert entity.getId() != null;
        if (!Objects.equals(entity.getId().getIdEmployeer(), entity.getEmployee().getId())) {
            entity.getId().setIdEmployeer(entity.getEmployee().getId());
        }
        super.save(entity);
    }
}
