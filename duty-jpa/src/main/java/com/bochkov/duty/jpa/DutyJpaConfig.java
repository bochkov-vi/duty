package com.bochkov.duty.jpa;

import com.bochkov.duty.h2.SpringH2ServerConfig;
import com.google.common.collect.Maps;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableJpaRepositories("com.bochkov.duty.jpa.repository")
@EnableJpaAuditing()
@EnableTransactionManagement
@Import(SpringH2ServerConfig.class)
public class DutyJpaConfig {


    @Bean
    AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable("test");
    }

    @Bean
    public SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dutyDataSource, AbstractJpaVendorAdapter jpaVendorAdapter, ResourceLoader resourceLoader) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
     /*   for (String sqlScript : ImmutableList.of("classpath:init.sql")) {
            Resource sqlScriptResource = resourceLoader.getResource(sqlScript);

            populator.addScript(sqlScriptResource);
        }
        populator.setContinueOnError(true);
        DatabasePopulatorUtils.execute(populator, dutyDataSource);*/

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactory.setJpaDialect(new HibernateJpaDialect());
        entityManagerFactory.setPersistenceUnitName("duty-pu");
        //entityManagerFactory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPackagesToScan("com.bochkov.duty.jpa.entity");
        entityManagerFactory.setDataSource(dutyDataSource);

        //for eclipselink
        Map<String, Object> jpaProperties = Maps.newHashMap();
        jpaProperties.put("hibernate.enable_lazy_load_no_trans", "true");
        jpaProperties.put("hibernate.format_sql", "false");
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");
        jpaVendorAdapter.setShowSql(true);
        entityManagerFactory.setJpaPropertyMap(jpaProperties);
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
