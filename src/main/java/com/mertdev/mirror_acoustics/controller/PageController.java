package com.mertdev.mirror_acoustics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping({"/hakkimizda", "/en/about"})
    public String about(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "About Us" : "Hakkımızda");
        return "pages/about";
    }

    @GetMapping("/iletisim")
    public String contact(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        return "pages/contact";
    }

    @GetMapping("/kvkk")
    public String privacy(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        return "pages/privacy";
    }

    @GetMapping("/teslimat")
    public String delivery(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        return "pages/delivery";
    }

    @GetMapping({"/iade-degisim", "/en/returns"})
    public String returns(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Returns" : "İade & Değişim");
        return "pages/returns";
    }

    @GetMapping({"/sss", "/en/faq"})
    public String faq(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", "SSS");
        return "pages/faq";
    }
}
