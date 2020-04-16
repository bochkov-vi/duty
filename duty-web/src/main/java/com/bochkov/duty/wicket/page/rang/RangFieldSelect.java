package com.bochkov.duty.wicket.page.rang;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.wicket.component.select2.data.MaskableChoiceProvider;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2Choice;

import javax.inject.Inject;

@Getter
public class RangFieldSelect extends Select2Choice<Rang> {

    @Inject
    RangRepository rangRepository;

    public RangFieldSelect(String id) {
        super(id);
    }

    public RangFieldSelect(String id, IModel<Rang> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setProvider(MaskableChoiceProvider.of(Rang.class, (s, p) -> rangRepository.findAll(s, p), "fullName", "name", "id"));
        getSettings().setCloseOnSelect(true);
        super.onInitialize();
    }
}
