package com.bochkov.duty.wicket.page.shift;

import com.bochkov.duty.jpa.entity.Shift;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class ShiftInputPanel extends GenericPanel<Shift> {
    public ShiftInputPanel(String id) {
        super(id);
    }

    public ShiftInputPanel(String id, IModel<Shift> model) {
        super(id, model);
    }
}
