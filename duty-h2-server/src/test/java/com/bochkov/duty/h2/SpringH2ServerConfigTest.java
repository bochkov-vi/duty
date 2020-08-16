package com.bochkov.duty.h2;

import org.h2.tools.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = SpringH2ServerConfig.class)
@RunWith(SpringRunner.class)
public class SpringH2ServerConfigTest {

    @Autowired()
    Server server;

    @Autowired()
    DataSource dataSource;

    @Test
    public void testServer() {
        assertNotNull(server);
    }

    @Test
    public void testDataSource() {
        assertNotNull(dataSource);
    }
}
