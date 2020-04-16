package com.bochkov.duty.wicket.page.rang;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.wicket.component.select2.data.MaskableChoiceProvider;
import org.apache.wicket.injection.Injector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.inject.Inject;

public class RangChoiceProvider extends MaskableChoiceProvider<Rang> {

    @Inject
    RangRepository repository;

    public RangChoiceProvider() {
        super("fullName", "name", "id");
    }

    @Override
    protected Page<Rang> findAll(Specification<Rang> specification, Pageable pageRequest) {
        if (repository == null) {
            Injector.get().inject(this);
        }
        return repository.findAll(specification, pageRequest);
    }
}
