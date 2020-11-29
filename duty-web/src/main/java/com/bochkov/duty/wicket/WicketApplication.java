package com.bochkov.duty.wicket;

import com.bochkov.duty.jpa.DutyJpaConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DutyJpaConfig.class)
public class WicketApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder()
                .sources(WicketApplication.class)
                .run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }
}
