package com.mertdev.mirror_acoustics.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@ToString(exclude = {"products"})
@EqualsAndHashCode(exclude = {"products"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nameTr;

    @Column(nullable = false)
    private String nameEn;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
}
