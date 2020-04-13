package com.bochkov.duty.wicket;

import com.bochkov.duty.jpa.DutyJpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DutyJpaConfig.class)
public class SpringConfiguration {
}
