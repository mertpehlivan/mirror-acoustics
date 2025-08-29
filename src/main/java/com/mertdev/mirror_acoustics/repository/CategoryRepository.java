package com.mertdev.mirror_acoustics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mertdev.mirror_acoustics.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
