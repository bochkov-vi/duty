package com.bochkov.duty.wicket.page;

import com.google.common.collect.ImmutableList;
import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.Application;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

public class BootstrapPage<T> extends GenericWebPage<T> {
    public BootstrapPage() {
    }

    public BootstrapPage(IModel<T> model) {
        super(model);
    }

    public BootstrapPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("webjars/bootstrap/current/js/bootstrap.js") {
            @Override
            public List<HeaderItem> getDependencies() {
                return ImmutableList.of(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()),
                        JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("webjars/popper.js/current/umd/popper.min.js")));

            }
        }));
        response.render(CssHeaderItem.forReference(new WebjarsCssResourceReference("webjars/bootstrap/current/css/bootstrap.css")));
        response.render(CssHeaderItem.forReference(new WebjarsCssResourceReference("resources/webjars/font-awesome/current/css/font-awesome.css")));
       // response.render(CssHeaderItem.forReference(new WebjarsCssResourceReference("resources/webjars/font-awesome/current/css/all.css")));
    }
}
