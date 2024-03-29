package com.acme.eshop.service;

import com.acme.eshop.domain.Category;
import com.acme.eshop.domain.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartServiceImpl implements CartService{
    List<Item> cart = new ArrayList<>();

    @Override
    public boolean addItem(Item item) {
        if(item != null){
            if(validate(item)){
                final int indexOfItem = cart.indexOf(item);
                if(indexOfItem == -1){
                    return cart.add(item);
                } else {
                    Item retrievedItem = cart.get(indexOfItem);
                    retrievedItem.setQuantity(retrievedItem.getQuantity() + item.getQuantity());
                }
            }
        } else {
            throw new IllegalArgumentException("Item is null");
        }
        return false;
    }

    private boolean validate(Item item){
        return item.getQuantity() > 0 && item.getCategory() != null & item.getName() != null && item.getPrice() != null;
    }

    @Override
    public boolean removeItem(Item item) {
        return false;
    }

    @Override
    public List<Item> getCartItems() {
        return cart;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return null;
    }

    @Override
    public boolean checkout() {
        return false;
    }

    @Override
    public List<Item> getTopTwoItemsWithFiveQuantityAndAboveByCategory(Category category) {
        return null;
    }
}
