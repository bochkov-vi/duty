package com.bochkov.duty.wicket.page.persongroup;

import com.bochkov.duty.jpa.entity.PersonGroup;
import com.bochkov.duty.jpa.repository.PersonGroupRepository;
import com.bochkov.duty.wicket.component.select2.BootstrapSelect2Field;
import com.google.common.primitives.Ints;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Optional;

@Getter
public class PersonGroupFieldSelect extends BootstrapSelect2Field<PersonGroupRepository, PersonGroup, Integer> {
    @SpringBean
    PersonGroupRepository repository;

    public PersonGroupFieldSelect(String id) {
        super(id, "name", "id");
    }

    public PersonGroupFieldSelect(String id, IModel<PersonGroup> model) {
        super(id, model, "name", "id");
    }
    @Override
    protected Integer convertToId(String str) {
        return Optional.ofNullable(str).map(Ints::tryParse).orElse(null);
    }
}
