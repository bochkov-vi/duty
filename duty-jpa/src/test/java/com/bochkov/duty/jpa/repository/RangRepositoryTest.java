package com.bochkov.duty.jpa.repository;

import com.bochkov.duty.jpa.DutyJpaConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DutyJpaConfig.class)
public class RangRepositoryTest {
    @Autowired
    RangRepository repository;

    @Test
    public void findByMask() {
        Page result = repository.findByLike("м", Pageable.unpaged(), "name", "fullName");
        Assert.assertFalse(result.isEmpty());
    }
}
