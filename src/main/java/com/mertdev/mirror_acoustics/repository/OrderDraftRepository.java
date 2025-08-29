package com.mertdev.mirror_acoustics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mertdev.mirror_acoustics.domain.OrderDraft;

public interface OrderDraftRepository extends JpaRepository<OrderDraft, Long> {
}
