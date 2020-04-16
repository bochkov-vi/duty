package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
import com.bochkov.wicket.component.select2.data.MaskableChoiceProvider;
import org.apache.wicket.injection.Injector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.inject.Inject;

public class DutyTypeChoiceProvider extends MaskableChoiceProvider<DutyType> {

    @Inject
    DutyTypeRepository repository;

    public DutyTypeChoiceProvider() {
        super("name", "id");
    }

    @Override
    protected Page<DutyType> findAll(Specification<DutyType> specification, Pageable pageRequest) {
        if (repository == null) {
            Injector.get().inject(this);
        }
        return repository.findAll(specification, pageRequest);
    }
}
