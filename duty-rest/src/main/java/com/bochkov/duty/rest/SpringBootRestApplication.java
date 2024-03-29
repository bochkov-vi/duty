package com.bochkov.duty.rest;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.List;

@Configuration
@Import(DutyJpaConfig.class)

@SpringBootApplication
@ComponentScan("com.bochkov.duty.jpa.entity.projection")
public class SpringBootRestApplication extends SpringBootServletInitializer implements RepositoryRestConfigurer {

    @Autowired
    ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestApplication.class, args);
    }

    @Override
    public void configureJacksonObjectMapper(ObjectMapper mapper) {

    }

    @Override
    public void configureHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {

    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        new Repositories(context).forEach(config::exposeIdsFor);
        //config.getCorsRegistry().addMapping("*");
        config.getCorsRegistry().addMapping("/**").allowedOrigins("*").allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name());
       /* config.setRelProvider(new DefaultLinkRelationProvider() {
            @Override
            public LinkRelation getCollectionResourceRelFor(Class<?> type) {
                return LinkRelation.of("items");
            }

            @Override
            public LinkRelation getItemResourceRelFor(Class<?> type) {
                return LinkRelation.of("item");
            }
        });*/

    }

}
