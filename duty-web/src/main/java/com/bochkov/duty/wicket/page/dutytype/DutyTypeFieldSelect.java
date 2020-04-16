package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.DutyType;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2Choice;

@Getter
public class DutyTypeFieldSelect extends Select2Choice<DutyType> {

    public DutyTypeFieldSelect(String id) {
        super(id);
    }

    public DutyTypeFieldSelect(String id, IModel<DutyType> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setProvider(new DutyTypeChoiceProvider());
        super.onInitialize();
    }
}
