package com.mertdev.mirror_acoustics.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mertdev.mirror_acoustics.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlugAndActiveTrue(String slug);

    @Query("select p from Product p where p.active=true order by p.createdAt desc")
    Page<Product> findActive(Pageable pageable);

    @Query("select p from Product p where p.active=true and p.featured=true order by p.featuredOrder asc, p.createdAt desc")
    Page<Product> findFeatured(Pageable pageable);

    @Query("select p from Product p where p.active=true and (lower(p.titleTr) like lower(concat('%', :q, '%')) or lower(p.titleEn) like lower(concat('%', :q, '%'))) order by p.createdAt desc")
    Page<Product> searchActive(@Param("q") String q, Pageable pageable);

    @Query("select p from Product p where p.active=true and p.category.slug = :slug order by p.createdAt desc")
    Page<Product> findActiveByCategorySlug(@Param("slug") String slug, Pageable pageable);
}
