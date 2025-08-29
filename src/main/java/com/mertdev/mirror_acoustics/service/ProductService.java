package com.mertdev.mirror_acoustics.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mertdev.mirror_acoustics.domain.Product;
import com.mertdev.mirror_acoustics.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;

    public Page<Product> listActive(int page, int size) {
        return repo.findActive(PageRequest.of(page, size));
    }

    public Page<Product> listFeatured(int page, int size) {
        return repo.findFeatured(PageRequest.of(page, size));
    }

    public Product getBySlug(String slug) {
        return repo.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Product save(Product p) {
        return repo.save(p);
    } // basic

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}