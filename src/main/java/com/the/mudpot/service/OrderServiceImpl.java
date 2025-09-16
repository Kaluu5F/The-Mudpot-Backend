package com.the.mudpot.service;

import com.the.mudpot.dto.OrderDTO;
import com.the.mudpot.dto.OrderItemDTO;
import com.the.mudpot.model.CurryItem;
import com.the.mudpot.model.Order;
import com.the.mudpot.model.OrderItem;
import com.the.mudpot.repository.CurryItemRepository;
import com.the.mudpot.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CurryItemRepository curryRepo;

    private static final String DEFAULT_CURRENCY = "LKR";
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    @Transactional
    @Override
    public OrderDTO createOrder(String userId, OrderDTO req) {
        if (req == null || req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        if (req.getCustomerName() == null || req.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (req.getPhone() == null || req.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone is required.");
        }

        Order order = Order.builder()
                .userId(userId)
                .customerName(req.getCustomerName().trim())
                .phone(req.getPhone().trim())
                .address(req.getAddress())
                .currency(DEFAULT_CURRENCY)
                .discount(ZERO)
                .deliveryFee(ZERO)
                .tax(ZERO)
                .status(Order.OrderStatus.NEW)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .build();

        List<OrderItem> items = new ArrayList<>();
        BigDecimal subtotal = ZERO;

        for (OrderItemDTO it : req.getItems()) {
            if (it.getQuantity() == null || it.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0.");
            }
            CurryItem product = curryRepo.findById(it.getCurryItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Curry item not found: " + it.getCurryItemId()));

            // availability guard
            var status = product.getAvailability();
            if (status == CurryItem.Availability.DISCONTINUED || status == CurryItem.Availability.OUT_OF_STOCK) {
                throw new IllegalStateException("Item unavailable: " + product.getName());
            }

            BigDecimal unit = product.getPrice().setScale(2, RoundingMode.HALF_UP);
            BigDecimal line = unit.multiply(BigDecimal.valueOf(it.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .curryItem(product)
                    .curryName(product.getName())  // snapshot
                    .quantity(it.getQuantity())
                    .unitPrice(unit)
                    .lineTotal(line)
                    .build();

            items.add(oi);
            subtotal = subtotal.add(line);
        }

        order.setItems(items);
        order.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));

        // simple total composition (extend as needed)
        BigDecimal total = order.getSubtotal()
                .subtract(order.getDiscount())
                .add(order.getDeliveryFee())
                .add(order.getTax())
                .setScale(2, RoundingMode.HALF_UP);
        order.setTotal(total);

        Order saved = orderRepo.save(order);
        return OrderDTO.from(saved);
    }


    @Transactional(readOnly = true)
    @Override
    public OrderDTO getMyOrder(String userId, String orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            throw new SecurityException("Not allowed to access this order.");
        }
        return OrderDTO.from(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDTO> listMyOrders(String userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderDTO::from)
                .toList();
    }

    @Override
    public OrderDTO createOrderFromCart(String userId, String customerName, String phone, String address) {
        return null;
    }
}

