package com.the.mudpot.controller;


import com.the.mudpot.dto.OrderDTO;
import com.the.mudpot.service.OrderService;
import com.the.mudpot.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private  OrderService orderService;

    // Create new order (user must be authenticated)
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO create(@RequestBody OrderDTO request) {
        String userId = SecurityUtils.currentUserId()
                .orElseThrow(() -> new SecurityException("Auth required"));
        return orderService.createOrder(userId, request);
    }

    // List my orders
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public List<OrderDTO> myOrders() {
        String userId = SecurityUtils.currentUserId()
                .orElseThrow(() -> new SecurityException("Auth required"));
        return orderService.listMyOrders(userId);
    }

    // Get one of my orders
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public OrderDTO get(@PathVariable String id) {
        String userId = SecurityUtils.currentUserId()
                .orElseThrow(() -> new SecurityException("Auth required"));
        return orderService.getMyOrder(userId, id);
    }

//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/from-cart")
//    @ResponseStatus(HttpStatus.CREATED)
//    public OrderDTO createFromCart(@RequestBody CheckoutRequest req) {
//        String userId = SecurityUtils.currentUserId().orElseThrow();
//        return orderService.createOrderFromCart(userId, req.getCustomerName(), req.getPhone(), req.getAddress());
//    }
}

