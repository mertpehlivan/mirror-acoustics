package com.mertdev.mirror_acoustics.config;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired(required = false)
    private AppProperties appProperties;

    @Autowired(required = false)
    private com.mertdev.mirror_acoustics.service.SettingService settingService;

    @ModelAttribute("lang")
    public String currentLanguage() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

    @ModelAttribute("whatsappPhone")
    public String whatsappPhone() {
        try {
            // Prefer settingService stored value if present
            if (settingService != null) {
                String s = settingService.get("contact.whatsapp.phone", LocaleContextHolder.getLocale().getLanguage());
                if (s != null && !s.isEmpty()) return s;
            }
            if (appProperties != null && appProperties.getWhatsapp() != null) {
                String p = appProperties.getWhatsapp().getPhone();
                return (p != null) ? p : "";
            }
        } catch (Exception ignored) {}
        return "";
    }
}
