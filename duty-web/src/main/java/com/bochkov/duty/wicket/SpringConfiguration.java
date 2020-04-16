package com.bochkov.duty.wicket;

import com.bochkov.duty.jpa.DutyJpaConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DutyJpaConfig.class)
@ComponentScan("com.bochkov.duty.wicket")
public class SpringConfiguration {

}
