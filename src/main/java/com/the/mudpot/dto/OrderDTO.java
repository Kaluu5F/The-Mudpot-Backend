package com.the.mudpot.dto;

import com.the.mudpot.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class OrderDTO {
    private String id;

    private String customerName;
    private String phone;
    private String address;
    private List<OrderItemDTO> items = new ArrayList<>();

    private String currency;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal deliveryFee;
    private BigDecimal tax;
    private BigDecimal total;
    private Order.OrderStatus status;
    private Order.PaymentStatus paymentStatus;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;


    public static OrderDTO from(Order e) {
        OrderDTO d = new OrderDTO();
        d.id = e.getId();
        d.customerName = e.getCustomerName();
        d.phone = e.getPhone();
        d.address = e.getAddress();
        d.currency = e.getCurrency();
        d.subtotal = e.getSubtotal();
        d.discount = e.getDiscount();
        d.deliveryFee = e.getDeliveryFee();
        d.tax = e.getTax();
        d.total = e.getTotal();
        d.status = e.getStatus();
        d.paymentStatus = e.getPaymentStatus();
        d.createdAt = e.getCreatedAt();
        d.updatedAt = e.getUpdatedAt();

        List<OrderItemDTO> itemDtos = new ArrayList<>();
        e.getItems().forEach(oi -> {
            OrderItemDTO i = new OrderItemDTO();
            i.setCurryItemId(oi.getCurryItem() != null ? oi.getCurryItem().getId() : null);
            i.setQuantity(oi.getQuantity());
            i.setCurryName(oi.getCurryName());
            i.setUnitPrice(oi.getUnitPrice());
            i.setLineTotal(oi.getLineTotal());
            itemDtos.add(i);
        });
        d.items = itemDtos;
        return d;
    }

}

