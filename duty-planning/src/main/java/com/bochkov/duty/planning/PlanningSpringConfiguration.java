package com.bochkov.duty.planning;

import com.bochkov.duty.jpa.DutyJpaConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DutyJpaConfig.class)
@ComponentScan
public class PlanningSpringConfiguration {
}
