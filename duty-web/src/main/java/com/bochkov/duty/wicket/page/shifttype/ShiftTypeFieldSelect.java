package com.bochkov.duty.wicket.page.shifttype;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.entity.ShiftType;
import lombok.Getter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2Choice;

import java.util.Collection;

@Getter
public class ShiftTypeFieldSelect extends Select2Choice<ShiftType> {

    IModel<Collection<Employee>> employeeCollectionModel;

    public ShiftTypeFieldSelect(String id) {
        super(id);
    }

    public ShiftTypeFieldSelect(String id, IModel<ShiftType> model) {
        super(id, model);
    }

    public ShiftTypeFieldSelect(String id, IModel<ShiftType> model, IModel<Collection<Employee>> employeeCollectionModel) {
        super(id, model);
        this.employeeCollectionModel = employeeCollectionModel;
    }

    @Override
    protected void onInitialize() {
        getSettings().setCloseOnSelect(true);
        getSettings().setTheme("bootstrap4");
        getSettings().setDropdownAutoWidth(true);
        setProvider(new ShiftTypeChoiceProvider());
        super.onInitialize();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }
}
