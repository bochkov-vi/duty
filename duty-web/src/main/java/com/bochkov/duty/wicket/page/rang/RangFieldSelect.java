package com.bochkov.duty.wicket.page.rang;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.wicket.component.select2.data.MaskableChoiceProvider;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Select2Choice;

import javax.inject.Inject;
import java.util.Optional;

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
        ChoiceProvider<Rang> provider = new MaskableChoiceProvider<Rang>("fullName", "name", "id") {
            @Override
            protected Page<Rang> findAll(Specification<Rang> specification, Pageable pageable) {
                return rangRepository.findAll(specification, pageable);
            }

            @Override
            public String getDisplayValue(Rang object) {
                return Optional.ofNullable(object).map(Rang::getFullName).orElse(null);
            }
        };
        setProvider(provider);
        getSettings().setCloseOnSelect(true);

        super.onInitialize();
    }
}
