package com.bochkov.duty.wicket.page;

import com.bochkov.bootstrap.BootstrapBehavior;
import com.bochkov.fontawesome.FontAwesomeBehavior;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BootstrapPage<T> extends GenericWebPage<T> implements IAjaxIndicatorAware {
    public BootstrapPage() {
    }

    public BootstrapPage(IModel<T> model) {
        super(model);
    }

    public BootstrapPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new BootstrapBehavior());
        add(new FontAwesomeBehavior());
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
        return "indicator";
    }
}
