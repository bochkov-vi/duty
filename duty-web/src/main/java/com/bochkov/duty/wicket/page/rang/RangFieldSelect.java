package com.bochkov.duty.wicket.page.rang;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.duty.wicket.component.select2.BootstrapSelect2Field;
import com.bochkov.duty.wicket.component.select2.EntityChoiceProvider;
import com.google.common.primitives.Ints;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Optional;

@Getter
public class RangFieldSelect extends BootstrapSelect2Field<RangRepository, Rang, Short> {
    @SpringBean
    RangRepository repository;

    public RangFieldSelect(String id) {
        super(id, "fullName", "name", "id");
    }

    public RangFieldSelect(String id, IModel<Rang> model) {
        super(id, model, "fullName", "name", "id");
    }

    @Override
    protected Short convertToId(String str) {
        return Optional.ofNullable(str).map(Ints::tryParse).map(Number::shortValue).orElse(null);
    }

    @Override
    protected EntityChoiceProvider<RangRepository, Rang, Short> newDataProvider() {
        EntityChoiceProvider<RangRepository, Rang, Short> provider = super.newDataProvider();
        provider.setChoiceRenderer(r -> Optional.ofNullable(r).map(Rang::getFullName).orElse(""));
        return provider;
    }
}
