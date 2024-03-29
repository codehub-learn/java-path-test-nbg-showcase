package com.acme.eshop.service.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TimeoutTest {

    @Test
    void testShouldSucceedInOneSecond(){
        assertTimeout(Duration.of(1, ChronoUnit.SECONDS), () -> Thread.sleep(5000));
    }

    @Test
    void testShouldSucceedInOneSecondPreemptively(){
        assertTimeoutPreemptively(Duration.of(1, ChronoUnit.SECONDS), () -> Thread.sleep(5000));
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void testShouldSucceedInTwoSeconds() throws InterruptedException {
        // execution of code...
        Thread.sleep(3000);
        assertTrue(true);
    }
}
