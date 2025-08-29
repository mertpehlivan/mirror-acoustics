package com.mertdev.mirror_acoustics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/hakkimizda")
    public String about() {
        return "pages/about";
    }

    @GetMapping("/iletisim")
    public String contact() {
        return "pages/contact";
    }

    @GetMapping("/kvkk")
    public String privacy() {
        return "pages/privacy";
    }

    @GetMapping("/teslimat")
    public String delivery() {
        return "pages/delivery";
    }

    @GetMapping("/iade-degisim")
    public String returns() {
        return "pages/returns";
    }

    @GetMapping("/sss")
    public String faq() {
        return "pages/faq";
    }
}
