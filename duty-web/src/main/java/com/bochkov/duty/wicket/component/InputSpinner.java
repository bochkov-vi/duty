package com.bochkov.duty.wicket.component;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class InputSpinner extends TextField<Number> {

    public InputSpinner(String id) {
        super(id, Number.class);
    }

    public InputSpinner(String id, IModel<Number> model) {
        super(id, model, Number.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("bootstrap-input-spinner/current/src/bootstrap-input-spinner.js")));
        response.render(OnDomReadyHeaderItem.forScript(String.format("$('#%s').inputSpinner()", getMarkupId())));
    }
}
