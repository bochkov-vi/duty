package com.bochkov.duty.h2;

import lombok.Getter;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootConfiguration
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
    @DependsOn("h2Server")
    DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        DataSource dataSource = dataSourceBuilder.build();
        return dataSource;
    }

}
