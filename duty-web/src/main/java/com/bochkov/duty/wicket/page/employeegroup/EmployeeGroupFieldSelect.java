package com.bochkov.duty.wicket.page.employeegroup;

import com.bochkov.duty.jpa.entity.EmployeeGroup;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2Choice;

@Getter
public class EmployeeGroupFieldSelect extends Select2Choice<EmployeeGroup> {

    public EmployeeGroupFieldSelect(String id) {
        super(id);
    }

    public EmployeeGroupFieldSelect(String id, IModel<EmployeeGroup> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setProvider(new EmployeeGroupChoiceProvider());
        getSettings().setCloseOnSelect(true);
        super.onInitialize();
    }
}
