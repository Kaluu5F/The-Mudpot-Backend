package com.the.mudpot.controller;

import com.the.mudpot.dto.CartDTO;
import com.the.mudpot.model.Session;
import com.the.mudpot.service.CartService;
import com.the.mudpot.util.SecurityUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public CartDTO get() {
        String userId = SecurityUtils.currentUserId().orElseThrow();
        return cartService.getMyCart(userId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartDTO addItem(@RequestBody AddItemRequest req) {
        String userId = Session.getUser().getId();
        return cartService.addItem(userId, req.getCurryItemId(), req.getQuantity());
    }

    @PutMapping("/items/{curryItemId}")
    public CartDTO setQty(@PathVariable String curryItemId, @RequestBody SetQtyRequest req) {
        String userId = Session.getUser().getId();
        return cartService.setQuantity(userId, curryItemId, req.getQuantity());
    }

    @DeleteMapping("/items/{curryItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable String curryItemId) {
        String userId = Session.getUser().getId();
        cartService.removeItem(userId, curryItemId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear() {
        String userId = Session.getUser().getId();
        cartService.clear(userId);
    }

    @Data static class AddItemRequest { private String curryItemId; private int quantity = 1; }
    @Data static class SetQtyRequest  { private int quantity; }
}

