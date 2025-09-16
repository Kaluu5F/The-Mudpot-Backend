package com.the.mudpot.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartDTO {
    private String id;
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal subtotal;
}

