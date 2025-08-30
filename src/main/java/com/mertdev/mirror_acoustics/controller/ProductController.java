package com.mertdev.mirror_acoustics.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.mertdev.mirror_acoustics.domain.Product;
import com.mertdev.mirror_acoustics.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping({ "/urun", "/en/product" })
public class ProductController {
    private final ProductService products;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String q,
            Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("q", q);
        if (q != null && !q.isBlank()) {
            model.addAttribute("page", products.search(q, page, 12));
            model.addAttribute("title", (lang.equals("en") ? "Search" : "Arama") + ": " + q);
        } else {
            model.addAttribute("page", products.listActive(page, 12));
            model.addAttribute("title", lang.equals("en") ? "Products" : "Ürünler");
        }
        return "products";
    }

    @GetMapping("/kategori/{slug}")
    public String byCategory(@PathVariable String slug,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("page", products.listByCategorySlug(slug, page, 12));
        model.addAttribute("title", (lang.equals("en") ? "Category" : "Kategori") + ": " + slug);
        model.addAttribute("categorySlug", slug);
        return "products";
    }

    @GetMapping("/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        Product p = products.getBySlug(slug);
        model.addAttribute("p", p);
        model.addAttribute("lang", lang);
        model.addAttribute("title", (lang.equals("en") ? p.getTitleEn() : p.getTitleTr()) + " — Mirror Acoustics");
        model.addAttribute("description", lang.equals("en") ? p.getDescriptionTr() : p.getDescriptionEn());
        return "product-detail";
    }
}