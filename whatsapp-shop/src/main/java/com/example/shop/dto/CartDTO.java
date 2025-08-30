package com.example.shop.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CartDTO {
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal subtotal = BigDecimal.ZERO;
    private String couponCode;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
}


