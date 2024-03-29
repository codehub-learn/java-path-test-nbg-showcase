package com.acme.eshop.service;

import com.acme.eshop.domain.Category;
import com.acme.eshop.domain.Item;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    boolean addItem(Item item);
    boolean removeItem(Item item);
    List<Item> getCartItems();
    BigDecimal getTotalPrice();
    boolean checkout();
    List<Item> getTopTwoItemsWithFiveQuantityAndAboveByCategory(Category category);
}
