package com.the.mudpot.service;


import com.the.mudpot.dto.CartDTO;

public interface CartService {
    CartDTO getMyCart(String userId);
    CartDTO addItem(String userId, String curryItemId, int qty);
    CartDTO setQuantity(String userId, String curryItemId, int qty);
    CartDTO removeItem(String userId, String curryItemId);
    void clear(String userId);
}

