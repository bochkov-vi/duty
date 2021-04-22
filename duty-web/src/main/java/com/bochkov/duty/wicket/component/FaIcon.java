package com.bochkov.duty.wicket.component;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class FaIcon extends Label {

    boolean hideOnModelEmpty = false;

    public FaIcon(String id) {
        super(id);
    }

    public FaIcon(String id, Serializable label) {
        super(id, label);
    }

    public FaIcon(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new ClassAttributeModifier() {
            @Override
            protected Set<String> update(Set<String> oldClasses) {
                oldClasses.add("fa");
                String icon = getDefaultModelObjectAsString();
                if (!Strings.isNullOrEmpty(icon)) {
                    oldClasses.add(icon);
                }
                return oldClasses;
            }
        });
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (hideOnModelEmpty) {
            setVisible(!Strings.isNullOrEmpty(getDefaultModelObjectAsString()));
        }
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        replaceComponentTagBody(markupStream, openTag, null);
    }
}
