package com.the.mudpot.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDTO {
    // REQUEST: need curryItemId + quantity
    private String curryItemId;
    private Integer quantity;

    // RESPONSE: snapshot
    private String curryName;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

}
