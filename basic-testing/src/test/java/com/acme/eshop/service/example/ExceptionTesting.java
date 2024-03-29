package com.acme.eshop.service.example;

import com.acme.eshop.service.CartService;
import com.acme.eshop.service.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.ExpectedException;
import org.junit.Rule;

import static org.junit.jupiter.api.Assertions.*;

@EnableRuleMigrationSupport // for junit4 rule example
public class ExceptionTesting {

    private CartService cartService;

    @BeforeEach
    void beforeEach(){
        this.cartService = new CartServiceImpl();
    }
    @Test
    void doesNotAddNullToCart1() {
        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(null));
    }

    @Test
    void doesNotAddNullToCart2() {
        // not recommended
        try {
            cartService.addItem(null);
            fail();
        } catch (IllegalArgumentException e){
            assertEquals("Item is null", e.getMessage());
        }
    }

    // JUnit 4 way (requires migration support)
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    void nullItemTheJUnit4Way() {
        expectedException.expect(IllegalArgumentException.class);
        cartService.addItem(null);
    }
}
