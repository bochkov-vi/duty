package com.bochkov.duty.wicket.page.shifttype;

import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.wicket.component.select2.data.MaskableChoiceProvider;
import org.apache.wicket.injection.Injector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.inject.Inject;

public class ShiftTypeChoiceProvider extends MaskableChoiceProvider<ShiftType> {

    @Inject
    ShiftTypeRepository repository;

    public ShiftTypeChoiceProvider() {
        super("name", "id");
    }

    @Override
    protected Page<ShiftType> findAll(Specification<ShiftType> specification, Pageable pageRequest) {
        if (repository == null) {
            Injector.get().inject(this);
        }
        return repository.findAll(specification, pageRequest);
    }
}
