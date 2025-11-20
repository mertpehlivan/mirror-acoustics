package com.mertdev.mirror_acoustics.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@ToString(exclude = {"product"})
@EqualsAndHashCode(exclude = {"product"})
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String url; // /uploads/...
    private String altTextTr; // SEO alt text
    private String altTextEn;
    private Integer sortOrder = 0;
    /**
     * Type of product image: "image" or "video". Default is "image" for backward compatibility.
     */
    private String type = "image";

    /**
     * If type is "video", this is the video URL (can be local or external).
     */
    private String videoUrl;
    // getters/setters
}
