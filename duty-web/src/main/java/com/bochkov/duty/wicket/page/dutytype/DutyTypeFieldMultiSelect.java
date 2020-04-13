package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
import com.bochkov.duty.wicket.component.select2.BootstrapSelect2MultiField;
import com.google.common.primitives.Ints;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collection;
import java.util.Optional;

@Getter
public class DutyTypeFieldMultiSelect extends BootstrapSelect2MultiField<DutyTypeRepository, DutyType, Integer> {
    @SpringBean
    DutyTypeRepository repository;

    public DutyTypeFieldMultiSelect(String id) {
        super(id, "name", "id");
    }

    public DutyTypeFieldMultiSelect(String id, IModel<Collection<DutyType>> model) {
        super(id, model, "name", "id");
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (getModel() == null) {

        }
    }

    @Override
    protected Integer convertToId(String str) {
        return Optional.ofNullable(str).map(Ints::tryParse).orElse(null);
    }

}
