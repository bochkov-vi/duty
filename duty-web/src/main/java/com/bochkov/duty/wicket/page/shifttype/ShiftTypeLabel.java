package com.bochkov.duty.wicket.page.shifttype;

import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.entity.UiOptions;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import java.util.Set;


public class ShiftTypeLabel extends GenericPanel<ShiftType> {
    Label textLabel = new Label("text");
    WebMarkupContainer iconLabel = new WebMarkupContainer("icon");

    public ShiftTypeLabel(String id) {
        super(id);
    }

    public ShiftTypeLabel(String id, IModel<ShiftType> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        iconLabel.setDefaultModel(getModel().map(ShiftType::getUiOptions).map(UiOptions::getFaIcon));
        iconLabel.add(new ClassAttributeModifier() {
            @Override
            protected Set<String> update(Set<String> oldClasses) {
                oldClasses.add("fa");
                if (iconLabel.getDefaultModel().isPresent().getObject()) {
                    oldClasses.add(iconLabel.getDefaultModelObjectAsString());
                }
                return oldClasses;
            }
        });
        textLabel.setDefaultModel(getModel().map(ShiftType::getUiOptions).map(UiOptions::getPlainText));

        add(iconLabel, textLabel);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (iconLabel.getDefaultModel().isPresent().getObject()) {
            iconLabel.setVisible(true);
        } else {
            iconLabel.setVisible(false);
        }
        textLabel.setVisible(!iconLabel.isVisible());
    }
}
