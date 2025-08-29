package com.mertdev.mirror_acoustics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mertdev.mirror_acoustics.service.ProductService;
import com.mertdev.mirror_acoustics.service.SettingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService products;
    private final SettingService settings;

    @GetMapping({ "/", "/{lang:en|tr}" })
    public String index(@PathVariable(required = false) String lang,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("page", products.listActive(page, 8));
        model.addAttribute("featured", products.listFeatured(0, 6));
        model.addAttribute("lang", lang == null ? "tr" : lang);
        String language = lang == null ? "tr" : lang;
        model.addAttribute("heroTitle", settings.get("hero.title", language));
        model.addAttribute("heroSubtitle", settings.get("hero.subtitle", language));
        model.addAttribute("aboutTitle", settings.get("about.title", language));
        model.addAttribute("aboutP1", settings.get("about.p1", language));
        model.addAttribute("aboutP2", settings.get("about.p2", language));
        model.addAttribute("contactAddress", settings.get("contact.address", language));
        model.addAttribute("contactEmail", settings.get("contact.email", language));
        model.addAttribute("contactPhone", settings.get("contact.phone", language));
        model.addAttribute("title", "Mirror Acoustics — Custom Audio Products");
        model.addAttribute("description", "El yapımı, kişiye özel hi‑end hoparlör kabinleri ve sistemler.");
        return "index";
    }
}