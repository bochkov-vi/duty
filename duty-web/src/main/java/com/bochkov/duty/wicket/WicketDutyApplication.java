package com.bochkov.duty.wicket;

import com.bochkov.duty.wicket.page.home.HomePage;
import com.bochkov.wicket.select2.Select2ApplicationExtension;
import com.giffing.wicket.spring.boot.starter.app.WicketBootStandardWebApplication;
import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import lombok.Getter;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.HTML5Attributes;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

@Component
public class WicketDutyApplication extends WicketBootStandardWebApplication {

    @Autowired
    IConverterLocator springConverterLocator;

    @Getter
    @Autowired
    ApplicationContext applicationContext;

    public static WicketDutyApplication get() {
        return (WicketDutyApplication) WicketBootStandardWebApplication.get();
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected void init() {
        super.init();
        WicketWebjars.install(this);
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        new AnnotatedMountScanner().scanPackage("com.bochkov.duty.wicket.page").mount(this);
        getJavaScriptLibrarySettings().setJQueryReference(new WebjarsJavaScriptResourceReference("webjars/jquery/current/jquery.min.js"));
        getComponentInstantiationListeners().add(new SpringComponentInjector(this,
                WebApplicationContextUtils.getRequiredWebApplicationContext(
                        getServletContext())));
        getMarkupSettings().setStripWicketTags(true);
        Select2ApplicationExtension.install(this);
        getComponentInstantiationListeners().add(new HTML5Attributes.InstantiationListener());
        //getRequestCycleListeners().add(new OpenSessionInViewRequestCycleListener(this));
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        return springConverterLocator;
    }
}
