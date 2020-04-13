package com.bochkov.duty.h2;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@PropertySource("classpath:h2-jdbc.properties")
@Getter
public class SpringH2ServerConfig {
    @Value("${h2.server.basedir}")
    String baseDir;

    @Autowired
    private Environment env;

    @Bean(initMethod = "start", destroyMethod = "stop", name = "h2Server")
    Server server() throws SQLException {
        Server server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-baseDir", baseDir);
        return server;
    }


    @Bean
    AbstractJpaVendorAdapter jpaVendorAdapter() {
        AbstractJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        //jpaVendorAdapter.setShowSql(env.getProperty("hibernate.showSql", Boolean.class, false));
        jpaVendorAdapter.setDatabase(Database.H2);
        return jpaVendorAdapter;
    }

    @Bean
    @DependsOn("h2Server")
    DataSource dutyDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl(env.getProperty("h2.jdbc.url"));
        config.setUsername(env.getProperty("h2.jdbc.user"));
        config.setPassword(env.getProperty("h2.jdbc.password"));
        return new HikariDataSource(config);
    }


}
