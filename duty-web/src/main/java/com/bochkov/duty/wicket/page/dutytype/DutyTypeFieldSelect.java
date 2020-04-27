package com.bochkov.duty.wicket.page.dutytype;

import com.bochkov.duty.jpa.entity.ShiftType;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2Choice;

@Getter
public class DutyTypeFieldSelect extends Select2Choice<ShiftType> {

    public DutyTypeFieldSelect(String id) {
        super(id);
    }

    public DutyTypeFieldSelect(String id, IModel<ShiftType> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setProvider(new DutyTypeChoiceProvider());
        super.onInitialize();
    }
}
