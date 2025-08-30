package com.mertdev.mirror_acoustics.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final AppProperties appProperties;

    @ModelAttribute("whatsappPhone")
    public String whatsappPhone() {
        return appProperties.getWhatsapp().getPhone();
    }
}


