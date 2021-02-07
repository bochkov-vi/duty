package com.bochkov.duty.wicket;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.wicket.service.converter.WicketJpaConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DutyJpaConfig.class)
public class WicketApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder()
                .sources(WicketApplication.class)
                .run(args);
    }

    @Bean
    IConverterLocator converterLocator() {
        return new WicketJpaConverterLocator();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }
}
