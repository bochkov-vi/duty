package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
import com.bochkov.duty.wicket.component.select2.BootstrapSelect2Field;
import com.bochkov.duty.wicket.component.select2.EntityChoiceProvider;
import com.google.common.primitives.Ints;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Optional;

@Getter
public class DutyTypeFieldSelect extends BootstrapSelect2Field<DutyTypeRepository, DutyType, Integer> {
    @SpringBean
    DutyTypeRepository repository;

    public DutyTypeFieldSelect(String id) {
        super(id, "name", "id");
    }

    public DutyTypeFieldSelect(String id, IModel<DutyType> model) {
        super(id, model, "name", "id");
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if(getModel()==null){

        }
    }

    @Override
    protected Integer convertToId(String str) {
        return Optional.ofNullable(str).map(Ints::tryParse).orElse(null);
    }

    @Override
    protected EntityChoiceProvider<DutyTypeRepository, DutyType, Integer> newDataProvider() {
        EntityChoiceProvider<DutyTypeRepository, DutyType, Integer> provider = super.newDataProvider();
        provider.setChoiceRenderer(r -> Optional.ofNullable(r).map(DutyType::getName).orElse(""));
        return provider;
    }
}
