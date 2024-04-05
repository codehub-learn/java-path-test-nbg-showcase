package com.acme.eshop.service;

import com.acme.eshop.domain.Category;
import com.acme.eshop.domain.Item;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A cart")
class CartServiceImplTest {

    private CartService cartService;

    @BeforeEach
    void beforeEach(){
        this.cartService = new CartServiceImpl();
    }

    @Test
    @DisplayName("successfully adds a correct item")
    @Tag("development")
    void addCorrectlyItemToCart() {
        Item item = Item.builder().name("Mouse AA1").quantity(1).price(BigDecimal.TEN).category(Category.MOUSE).build();
        boolean result = cartService.addItem(item);
        assertTrue(result);
    }

    @Nested
    @DisplayName(" (when item is incorrect) ")
    class WhenIncorrect {
        @Test
        @DisplayName("does not add with zero quantity")
        void doesNotAddItemWithZeroQuantityToCart() {
            Item item = Item.builder().name("Mouse AA1").quantity(0).price(BigDecimal.TEN).category(Category.MOUSE).build();
            boolean result = cartService.addItem(item);
            assertFalse(result);
        }

        @Test
        @DisplayName("does not add with null fields")
        void doesNotAddItemWithNullFieldsToCart() {
            Item item = Item.builder().name(null).quantity(5).price(null).category(null).build();
            boolean result = cartService.addItem(item);
            assertFalse(result);
        }
    }

    @Test
    @DisplayName("correctly updates duplicate items")
    void cartIsUpdatedWhenCorrectItemIsAddedTwice(){
        Item item = Item.builder().name("Mouse AA1").quantity(1).price(BigDecimal.TEN).category(Category.MOUSE).build();
        Item item2 = Item.builder().name("Mouse AA1").quantity(3).price(BigDecimal.TEN).category(Category.MOUSE).build();
        cartService.addItem(item);
        cartService.addItem(item2);
        List<Item> cartItems = cartService.getCartItems();
        assertAll(
                () -> assertEquals(1, cartItems.size()),
                () -> assertEquals(4, cartItems.getFirst().getQuantity())
        );
    }

    @Test
    @DisplayName("does not add when item is null")
    void doesNotAddNullToCart() {
        assertThrows(IllegalArgumentException.class, () -> cartService.addItem(null));
    }

    @Test
    @DisplayName("adds and retrieves items in insertion order")
    void addItemsToCartAndReturnedItemsAreSortedAccordingToInsertionOrder(){
        Item item1 = Item.builder().name("CPU 101").price(BigDecimal.valueOf(200)).quantity(1).category(Category.HARDWARE).build();
        Item item2 = Item.builder().name("Mouse AA1").quantity(1).price(BigDecimal.TEN).category(Category.MOUSE).build();
        cartService.addItem(item1);
        cartService.addItem(item2);
        assertIterableEquals(List.of(item1, item2), cartService.getCartItems(), "Items are not in correct order (insertion) or not same items");
    }
}