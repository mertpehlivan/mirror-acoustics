package com.mertdev.mirror_acoustics.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "order_drafts")
@Data
public class OrderDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String cartSnapshot;

    private String name;
    private String phone;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String shippingPreference;

    @Column(columnDefinition = "TEXT")
    private String note;

    private String utm;

    @Column(nullable = false)
    private String status = "draft_whatsapp";
}
