package com.bochkov.duty.wicket.page.shifttype;

import com.bochkov.duty.jpa.entity.ShiftType;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2Choice;

@Getter
public class ShiftTypeFieldSelect extends Select2Choice<ShiftType> {

    public ShiftTypeFieldSelect(String id) {
        super(id);
    }

    public ShiftTypeFieldSelect(String id, IModel<ShiftType> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        getSettings().setCloseOnSelect(true);
        getSettings().setTheme("bootstrap4");
        setProvider(new ShiftTypeChoiceProvider());
        super.onInitialize();
    }
}
