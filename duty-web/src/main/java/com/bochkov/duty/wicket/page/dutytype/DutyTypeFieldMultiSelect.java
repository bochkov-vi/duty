package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.DutyType;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2MultiChoice;

import java.util.Collection;

@Getter
public class DutyTypeFieldMultiSelect extends Select2MultiChoice<DutyType> {

    public DutyTypeFieldMultiSelect(String id) {
        super(id);
    }

    public DutyTypeFieldMultiSelect(String id, IModel<Collection<DutyType>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setProvider(new DutyTypeChoiceProvider());
        super.onInitialize();
    }
}
