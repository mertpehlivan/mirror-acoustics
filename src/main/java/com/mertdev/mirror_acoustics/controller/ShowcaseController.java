package com.mertdev.mirror_acoustics.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mertdev.mirror_acoustics.repository.ShowcaseImageRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ShowcaseController {
    private final ShowcaseImageRepository showcaseRepo;

    @GetMapping({"/vitrin", "/en/showcase"})
    public String page(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size,
                       Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Showcase" : "Vitrin");
        model.addAttribute("description", lang.equals("en")
                ? "Featured gallery of our handcrafted speakers."
                : "El yapımı hoparlörlerimizden seçkiler.");

        // Request a page of active showcase images, ordered by sortOrder.
        Pageable p = PageRequest.of(Math.max(0, page), Math.max(1, size));
        Page<com.mertdev.mirror_acoustics.domain.ShowcaseImage> pageResult = showcaseRepo.findByActiveTrue(p);

        model.addAttribute("showcasePage", pageResult);
        // For backward compatibility with templates that expect 'showcase' as a list
        model.addAttribute("showcase", pageResult.getContent());

        model.addAttribute("currentPage", pageResult.getNumber());
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("pageSize", pageResult.getSize());

        return "showcase";
    }
}
