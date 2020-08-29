package com.bochkov.duty.datasource;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:com/bochkov/duty/datasource/h2/application.properties")
@Slf4j
public class DataSourceConfig {
    @Value("${h2.server.dir}")
    String baseDir;


    @Bean(initMethod = "start", destroyMethod = "stop", name = "h2Server")
    @Primary
    Server server() throws SQLException {
        log.debug("Стартую h2 сервер с baseDir={}",baseDir);
        Server server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-baseDir", baseDir);
        return server;
    }


    @Bean
    @DependsOn("h2Server")
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
        DataSourceBuilder dataSourceBuilder = dataSourceProperties.initializeDataSourceBuilder();

        DataSource dataSource = dataSourceBuilder.build();
        return dataSource;
    }

}
