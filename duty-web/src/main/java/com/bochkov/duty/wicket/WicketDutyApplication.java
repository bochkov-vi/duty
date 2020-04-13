package com.bochkov.duty.wicket;

import com.bochkov.duty.wicket.page.home.HomePage;
import com.google.common.primitives.Ints;
import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

@Component
public class WicketDutyApplication extends WebApplication {
    @Autowired
    DutyTypeRepository dutyTypeRepository;

    @Autowired
    RangRepository rangRepository;

    @Autowired
    PersonGroupRepository personGroupRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    DayRepository dayRepository;

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
        //getRequestCycleListeners().add(new OpenSessionInViewRequestCycleListener(this));
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator converterLocator = (ConverterLocator) super.newConverterLocator();
        converterLocator.set(DutyType.class, new IConverter<DutyType>() {
            @Override
            public DutyType convertToObject(String value, Locale locale) throws ConversionException {
                return Optional.ofNullable(value).map(Ints::tryParse)
                        .flatMap(id -> dutyTypeRepository.findById(id)).orElse(null);
            }

            @Override
            public String convertToString(DutyType value, Locale locale) {
                return Optional.ofNullable(value).map(Persistable::getId).map(String::valueOf).orElse(null);
            }
        });
        converterLocator.set(Rang.class, new IConverter<Rang>() {
            @Override
            public Rang convertToObject(String value, Locale locale) throws ConversionException {
                return Optional.ofNullable(value).map(Ints::tryParse).map(n -> n.shortValue())
                        .flatMap(id -> rangRepository.findById(id)).orElse(null);
            }

            @Override
            public String convertToString(Rang value, Locale locale) {
                return Optional.ofNullable(value).map(Persistable::getId).map(String::valueOf).orElse(null);
            }
        });

        converterLocator.set(PersonGroup.class, new IConverter<PersonGroup>() {
            @Override
            public PersonGroup convertToObject(String value, Locale locale) throws ConversionException {
                return Optional.ofNullable(value).map(Ints::tryParse).map(n -> n.intValue())
                        .flatMap(id -> personGroupRepository.findById(id)).orElse(null);
            }

            @Override
            public String convertToString(PersonGroup value, Locale locale) {
                return Optional.ofNullable(value).map(Persistable::getId).map(String::valueOf).orElse(null);
            }
        });
        converterLocator.set(Person.class, new IConverter<Person>() {
            @Override
            public Person convertToObject(String value, Locale locale) throws ConversionException {
                return Optional.ofNullable(value).flatMap(id -> personRepository.findById(id)).orElse(null);
            }

            @Override
            public String convertToString(Person value, Locale locale) {
                return Optional.ofNullable(value).map(Persistable::getId).map(id -> converterLocator.getConverter(String.class).convertToString(id, locale)).orElse(null);
            }
        });
        converterLocator.set(Day.class, new IConverter<Day>() {
            @Override
            public Day convertToObject(String value, Locale locale) throws ConversionException {
                LocalDate date = converterLocator.get(LocalDate.class).convertToObject(value, locale);
                return Optional.ofNullable(date).flatMap(id -> dayRepository.findById(id)).orElse(null);
            }

            @Override
            public String convertToString(Day value, Locale locale) {
                LocalDate date = Optional.ofNullable(value).map(Day::getId).orElse(null);
                return Optional.ofNullable(date).map(id -> converterLocator.get(LocalDate.class).convertToString(id, locale)).orElse(null);
            }
        });
        return converterLocator;
    }
}
