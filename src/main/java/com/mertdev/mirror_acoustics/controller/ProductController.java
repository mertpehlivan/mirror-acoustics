package com.mertdev.mirror_acoustics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public String list(@RequestHeader(name = "Accept-Language", required = false) String al,
            @RequestParam(defaultValue = "0") int page, Model model) {
        String lang = al != null && al.toLowerCase().startsWith("en") ? "en" : "tr";
        model.addAttribute("page", products.listActive(page, 12));
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Products" : "Ürünler");
        return "products";
    }

    @GetMapping("/{slug}")
    public String detail(@PathVariable String slug,
            @RequestParam(defaultValue = "tr") String lang,
            Model model) {
        Product p = products.getBySlug(slug);
        model.addAttribute("p", p);
        model.addAttribute("lang", lang);
        model.addAttribute("title", (lang.equals("en") ? p.getTitleEn() : p.getTitleTr()) + " — Mirror Acoustics");
        model.addAttribute("description", lang.equals("en") ? p.getDescriptionEn() : p.getDescriptionTr());
        String whatsappMessage = URLEncoder.encode(
                "Merhaba, " + (lang.equals("en") ? p.getTitleEn() : p.getTitleTr()) +
                        " hakkında bilgi almak istiyorum",
                StandardCharsets.UTF_8);
        model.addAttribute("whatsappMessage", whatsappMessage);
        return "product-detail";
    }
}