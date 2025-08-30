package com.mertdev.mirror_acoustics.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Favorites {
    private Set<Long> productIds = new LinkedHashSet<>();

    public void add(Long productId) {
        productIds.add(productId);
    }

    public void remove(Long productId) {
        productIds.remove(productId);
    }
}


