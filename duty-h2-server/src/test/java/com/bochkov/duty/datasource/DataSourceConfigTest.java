package com.bochkov.duty.datasource;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = DataSourceConfig.class)
@RunWith(SpringRunner.class)
@Slf4j
public class DataSourceConfigTest {

    @Autowired
    Server server;

    @Autowired
    DataSource dataSource;

    @Test
    public void testServer() {
        assertNotNull(server);
    }

    @Test
    public void testDataSource() {
        assertNotNull(dataSource);
    }

    @Test
    public void testSqlCommand() {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        int result = template.update("CREATE SCHEMA  IF NOT EXISTS test  ");
        result = template.update("DROP SCHEMA IF EXISTS test");
        log.debug(String.format("UPDATES IS SUCCESSFULL!!! %s", result));
    }
}
