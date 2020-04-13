package com.bochkov.duty;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;

public class SimpleTest {

    @Test
    public void testDuration(){
        Duration d1 = Duration.between(LocalTime.of(9,0),LocalTime.of(13,0));
        System.out.println(d1);
    }

    @Test
    public void testDuration2(){
    }
}
