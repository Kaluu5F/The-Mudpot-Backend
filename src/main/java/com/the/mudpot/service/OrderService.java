package com.the.mudpot.service;

import com.the.mudpot.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(String userId, OrderDTO request);
    OrderDTO getMyOrder(String userId, String orderId);
    List<OrderDTO> listMyOrders(String userId);
    OrderDTO createOrderFromCart(String userId, String customerName, String phone, String address);

}
