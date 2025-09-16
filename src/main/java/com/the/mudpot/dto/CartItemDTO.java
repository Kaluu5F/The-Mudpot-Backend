package com.the.mudpot.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDTO {
    private String curryItemId;
    private Integer quantity;

    // response enrichments
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String availability;
    private BigDecimal lineTotal;

}

