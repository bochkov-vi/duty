package com.bochkov.duty.wicket.component.select2;

import org.apache.wicket.Component;
import org.apache.wicket.StyleAttributeModifier;
import org.apache.wicket.behavior.Behavior;
import org.wicketstuff.select2.AbstractSelect2Choice;

import java.util.Map;

public class BootstrapSelect2Behavior extends Behavior {

    @Override
    public void bind(Component component) {
        super.bind(component);
        AbstractSelect2Choice cmp = (AbstractSelect2Choice) component;
        cmp.getSettings().setTheme("bootstrap4");
        cmp.add(new StyleAttributeModifier() {
            @Override
            protected Map<String, String> update(Map<String, String> oldStyles) {
                oldStyles.put("width", "100%");
                return oldStyles;
            }
        });
    }
}
