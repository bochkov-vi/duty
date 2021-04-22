package com.bochkov.duty.wicket.component;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

@Getter
@Setter
@Accessors(chain = true)
public class LabelFaIcon extends GenericPanel<String> {

    boolean hideOnModelEmpty = false;

    IModel<String> faIconModel;

    Label text = new Label("text");

    FaIcon icon = new FaIcon("icon");

    public LabelFaIcon(String id, IModel<String> faIconModel) {
        super(id);
        this.faIconModel = faIconModel;
    }

    public LabelFaIcon(String id, IModel<String> model, IModel<String> faIconModel) {
        super(id, model);
        this.faIconModel = faIconModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        text.setDefaultModel(getModel());
        icon.setDefaultModel(faIconModel);
        add(text, icon);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        faIconModel.detach();
    }

    public FaIcon setFaIconHideOnModelEmpty(boolean hideOnModelEmpty) {
        return icon.setHideOnModelEmpty(hideOnModelEmpty);
    }

    public boolean isFaIconHideOnModelEmpty() {
        return icon.isHideOnModelEmpty();
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (hideOnModelEmpty) {
            setVisible(!Strings.isNullOrEmpty(getDefaultModelObjectAsString()));
        }
    }
}
