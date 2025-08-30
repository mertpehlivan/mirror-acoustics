package com.example.shop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long productId;
    private String title;
    private Long variantId;
    private String variantLabel;
    private BigDecimal unitPrice;
    private int qty;
    private BigDecimal lineTotal;
}


