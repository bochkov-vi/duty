package com.bochkov.duty.wicket.page.planing;

import com.bochkov.bootstrap.ActiveLinkBehavior;
import com.bochkov.duty.wicket.page.BootstrapPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BasePlaningPage<T> extends BootstrapPage<T> {
    public BasePlaningPage() {
    }

    public BasePlaningPage(IModel<T> model) {
        super(model);
    }

    public BasePlaningPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        BookmarkablePageLink link = new BookmarkablePageLink<Void>("period-link", DatePeriodPage.class);
        link.add(ActiveLinkBehavior.of(link));
        add(link);

        link = new BookmarkablePageLink<Void>("employee-link", PlaningEmployeePage.class);
        link.add(ActiveLinkBehavior.of(link));
        add(link);
    }
}
