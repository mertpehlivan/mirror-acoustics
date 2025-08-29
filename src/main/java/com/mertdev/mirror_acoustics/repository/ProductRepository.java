package com.mertdev.mirror_acoustics.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mertdev.mirror_acoustics.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlugAndActiveTrue(String slug);

    @Query("select p from Product p where p.active=true order by p.createdAt desc")
    Page<Product> findActive(Pageable pageable);

    @Query("select p from Product p where p.active=true and p.featured=true order by p.featuredOrder asc, p.createdAt desc")
    Page<Product> findFeatured(Pageable pageable);
}
