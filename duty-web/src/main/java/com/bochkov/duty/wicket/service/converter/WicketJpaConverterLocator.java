package com.bochkov.duty.wicket.service.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.ConverterLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@Slf4j
public class WicketJpaConverterLocator extends ConverterLocator {

    @Autowired
    ApplicationContext context;


    public WicketJpaConverterLocator() {
        super();
    }

    @PostConstruct
    public void postConstruct() {
        WicketJpaConverterExtension.init(this, context);
    }


}
