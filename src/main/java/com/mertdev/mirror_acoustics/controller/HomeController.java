package com.mertdev.mirror_acoustics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mertdev.mirror_acoustics.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService products;

    @GetMapping({ "/", "/{lang:en|tr}" })
    public String index(@PathVariable(required = false) String lang,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("page", products.listActive(page, 8));
        model.addAttribute("featured", products.listFeatured(0, 6));
        model.addAttribute("lang", lang == null ? "tr" : lang);
        model.addAttribute("title", "Mirror Acoustics — Custom Audio Products");
        model.addAttribute("description", "El yapımı, kişiye özel hi‑end hoparlör kabinleri ve sistemler.");
        return "index";
    }
}