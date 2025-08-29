package com.mertdev.mirror_acoustics.service;

import org.springframework.stereotype.Service;

import com.mertdev.mirror_acoustics.domain.Cart;
import com.mertdev.mirror_acoustics.domain.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final Cart cart;

    public void addProduct(Product product, int quantity) {
        cart.addItem(product, quantity);
    }

    public void removeProduct(Long productId) {
        cart.removeItem(productId);
    }

    public Cart getCart() {
        return cart;
    }
}
