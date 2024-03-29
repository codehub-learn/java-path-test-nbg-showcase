package com.acme.eshop.service.example;

import io.github.artsok.RepeatedIfExceptionsTest;
import org.junit.jupiter.api.RepetitionInfo;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepeatedTest {
    // flaky test
    // test -> 10 times -> 4 times success
    // test -> 10 times <- max failure 1 test case

    @org.junit.jupiter.api.RepeatedTest(10)
    void repeatableTest(){
        System.out.println("repeating test");
        assertTrue(ThreadLocalRandom.current().nextBoolean());
    }

    @org.junit.jupiter.api.RepeatedTest(value = 10, name = "{currentRepetition}/{totalRepetitions}")
    void repeatedTestCustomName() {
        assertTrue(ThreadLocalRandom.current().nextBoolean());
    }

    @RepeatedIfExceptionsTest(repeats = 10)
    public void repeatedTestRerunnerSimple() {
        // Runs the test until it succeeds
        assertTrue(ThreadLocalRandom.current().nextBoolean());
    }

    @RepeatedIfExceptionsTest(repeats = 10, minSuccess = 4)
    void repeatedTestRerunnerMinimumSuccess() {
        // Runs the test until it succeeds 4 times
        assertTrue(ThreadLocalRandom.current().nextBoolean());
    }

    @RepeatedIfExceptionsTest(repeats = 10, exceptions = IOException.class, name = "{currentRepetition}/{totalRepetitions}")
    void repeatedTestRerunnerIOException() throws IOException {
        // Repeats until an IOException is not thrown
        // Also, a custom name was added
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new IOException("Exception in I/O operation");
        }
    }
}
