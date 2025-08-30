package com.mertdev.mirror_acoustics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mertdev.mirror_acoustics.domain.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);
}


