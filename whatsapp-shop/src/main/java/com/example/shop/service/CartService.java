package com.example.shop.service;

import com.example.shop.dto.CartDTO;
import com.example.shop.dto.CartItemDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CartService {
    private static final String CART_KEY = "CART_DTO";

    public CartDTO getCart(HttpSession session) {
        CartDTO cart = (CartDTO) session.getAttribute(CART_KEY);
        if (cart == null) {
            cart = new CartDTO();
            session.setAttribute(CART_KEY, cart);
        }
        return cart;
    }

    public void addItem(HttpSession session, Long productId, String title, Long variantId, String variantLabel,
                        BigDecimal unitPrice, int qty) {
        CartDTO cart = getCart(session);
        CartItemDTO item = new CartItemDTO(productId, title, variantId, variantLabel, unitPrice, qty,
                unitPrice.multiply(BigDecimal.valueOf(qty)));
        cart.getItems().add(item);
        computeTotals(cart);
    }

    public void removeItem(HttpSession session, int index) {
        CartDTO cart = getCart(session);
        if (index >= 0 && index < cart.getItems().size()) {
            cart.getItems().remove(index);
            computeTotals(cart);
        }
    }

    public void applyCoupon(HttpSession session, String code, BigDecimal discountAmount) {
        CartDTO cart = getCart(session);
        cart.setCouponCode(code);
        cart.setDiscount(discountAmount);
        computeTotals(cart);
    }

    public void computeTotals(CartDTO cart) {
        BigDecimal subtotal = cart.getItems().stream()
                .map(CartItemDTO::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubtotal(subtotal);
        BigDecimal total = subtotal.subtract(cart.getDiscount() == null ? BigDecimal.ZERO : cart.getDiscount());
        cart.setTotal(total.max(BigDecimal.ZERO));
    }
}


