package com.bochkov.duty.wicket.page.vacation;

import com.bochkov.duty.jpa.entity.Vacation;
import com.bochkov.duty.wicket.page.employee.EmployeeFieldSelect;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.springframework.data.domain.Persistable;

public class VacationInputPanel extends GenericPanel<Vacation> {

    public VacationInputPanel(String id, IModel<Vacation> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //((CompoundPropertyModel<Vacatiob>)getModel()).bind();
        add(new TextField<Integer>("id.year") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (VacationInputPanel.this.getModel().map(Persistable::isNew).getObject()) {
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
            }
        }.setRequired(true));
        add(new EmployeeFieldSelect("employee").setRequired(true));
        add(new TextField<>("createdDate").setEnabled(false));

        add(new VacationPartCollectionInputPanel("parts"));


    }
}
