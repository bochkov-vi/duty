package com.bochkov.duty.rest;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.jpa.entity.projection.EmployeeFullDataProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.core.DefaultLinkRelationProvider;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import static java.util.Arrays.asList;

@Configuration
@Import(DutyJpaConfig.class)

@SpringBootApplication
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
        ((MappingJackson2HttpMessageConverter) messageConverters.get(0))
                .setSupportedMediaTypes(asList(MediaTypes.HAL_JSON));
        ((MappingJackson2HttpMessageConverter) messageConverters.get(2))
                .setSupportedMediaTypes(asList(MediaType.APPLICATION_JSON));
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        new Repositories(context).forEach(config::exposeIdsFor);
        //config.getCorsRegistry().addMapping("*");
        config.getCorsRegistry().addMapping("/**").allowedOrigins("*").allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name());
        config.setRelProvider(new DefaultLinkRelationProvider() {
            @Override
            public LinkRelation getCollectionResourceRelFor(Class<?> type) {
                return LinkRelation.of("items");
            }

            @Override
            public LinkRelation getItemResourceRelFor(Class<?> type) {
                return LinkRelation.of("item");
            }
        });

    }

    @Bean
    EmployeeFullDataProcessor employeeFullDataProcessor() {
        return new EmployeeFullDataProcessor();
    }
}
