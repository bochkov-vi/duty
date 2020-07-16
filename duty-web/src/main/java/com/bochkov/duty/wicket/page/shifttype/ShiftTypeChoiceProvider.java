package com.bochkov.duty.wicket.page.shifttype;

import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import com.bochkov.wicket.component.select2.data.MaskableChoiceProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.inject.Inject;
import java.util.Collection;

public class ShiftTypeChoiceProvider extends MaskableChoiceProvider<ShiftType> {

    @Inject
    ShiftTypeRepository repository;

    IModel<Collection<ShiftType>> excludes;

    public ShiftTypeChoiceProvider() {
        super("name", "id");
    }

    public ShiftTypeChoiceProvider(IModel<Collection<ShiftType>> excludes) {
        this();
        this.excludes = excludes;
    }

    @Override
    public Collection<ShiftType> excludes() {
        return excludes.getObject();
    }

    @Override
    protected Page<ShiftType> findAll(Specification<ShiftType> specification, Pageable pageRequest) {
        if (repository == null) {
            Injector.get().inject(this);
        }
        return repository.findAll(specification, pageRequest);
    }

    @Override
    public void detach() {
        super.detach();
        if (excludes != null) {
            excludes.detach();
        }
    }
}
