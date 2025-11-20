package com.mertdev.mirror_acoustics.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mertdev.mirror_acoustics.domain.Product;
import com.mertdev.mirror_acoustics.service.ProductService;
import com.mertdev.mirror_acoustics.repository.ShowcaseImageRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping({ "/urun", "/en/product" })
public class ProductController {
    private final ProductService products;
    private final ShowcaseImageRepository showcaseRepo;

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
            model.addAttribute("description", lang.equals("en")
                    ? ("Search results for: " + q)
                    : ("Arama sonuçları: " + q));
        } else {
            model.addAttribute("page", products.listActive(page, 12));
            model.addAttribute("title", lang.equals("en") ? "Products" : "Ürünler");
            model.addAttribute("description", lang.equals("en")
                    ? "Browse handcrafted, custom hi-end speaker systems."
                    : "El yapımı, kişiye özel hi-end hoparlör sistemlerini keşfedin.");
        }
        model.addAttribute("showcase", showcaseRepo.findAllByActiveTrueOrderBySortOrderAsc());
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
        model.addAttribute("description", (lang.equals("en") ? "Products in category: " : "Kategorideki ürünler: ") + slug);
        model.addAttribute("categorySlug", slug);
        model.addAttribute("showcase", showcaseRepo.findAllByActiveTrueOrderBySortOrderAsc());
        return "products";
    }

    @GetMapping("/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        Product p = products.getBySlug(slug);
        model.addAttribute("p", p);
        model.addAttribute("lang", lang);
        if (p != null) {
            model.addAttribute("title", (lang.equals("en") ? p.getTitleEn() : p.getTitleTr()) + " - Mirror Acoustics");
            model.addAttribute("description", lang.equals("en") ? p.getDescriptionEn() : p.getDescriptionTr());
        } else {
            model.addAttribute("title", lang.equals("en") ? "Product" : "Ürün");
            model.addAttribute("description", lang.equals("en")
                    ? "Custom hi-end speakers and enclosures."
                    : "Kişiye özel hi-end hoparlör ve kabinleri.");
        }
        return "product-detail";
    }
}
