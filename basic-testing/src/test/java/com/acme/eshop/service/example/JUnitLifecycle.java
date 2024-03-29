package com.acme.eshop.service.example;

import com.acme.eshop.domain.Category;
import com.acme.eshop.domain.Item;
import com.acme.eshop.service.CartServiceImpl;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JUnitLifecycle {
    @BeforeAll
    static void beforeAll(){
        System.out.println("hello from before all");
    }

    @BeforeEach
    void beforeEach(){
        System.out.println("hello from before each");
    }

    @AfterEach
    void afterEach(){
        System.out.println("hello from after each");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("hello from after all");
    }


    @Test
    void simpleTest() {

    }
}
