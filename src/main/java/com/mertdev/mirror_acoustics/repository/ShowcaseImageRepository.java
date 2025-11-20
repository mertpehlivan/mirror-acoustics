package com.mertdev.mirror_acoustics.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mertdev.mirror_acoustics.domain.ShowcaseImage;

public interface ShowcaseImageRepository extends JpaRepository<ShowcaseImage, Long> {
    List<ShowcaseImage> findAllByOrderBySortOrderAsc();
    List<ShowcaseImage> findAllByActiveTrueOrderBySortOrderAsc();
    // Pageable query for server-side pagination
    Page<ShowcaseImage> findByActiveTrue(Pageable pageable);
}

