package com.mertdev.mirror_acoustics.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final com.mertdev.mirror_acoustics.service.SettingService settingService;

    @GetMapping({"/hakkimizda", "/en/about"})
    public String about(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "About Us" : "Hakkımızda");
        model.addAttribute("description", lang.equals("en") ? "About Mirror Acoustics and our handcrafted audio philosophy." : "Mirror Acoustics ve el yapımı ses felsefemiz hakkında.");
        model.addAttribute("aboutTitle", lang.equals("en") ? "About Us" : "Hakkımızda");
        model.addAttribute("aboutP1", lang.equals("en") ? "Mirror Acoustics is a special audio systems brand founded by Ismet Cataloğlu in a workshop in central Istanbul and grown through his meticulous work. Each product reflects the elegant combination of high engineering knowledge and craftsmanship." : "Mirror Acoustics, İstanbul merkezli bir atölyede İsmet Çataloğlu tarafından kurulmuş ve onun titiz çalışmalarıyla büyüyen özel bir ses sistemleri markasıdır. Her bir ürün, yüksek mühendislik bilgisi ile el işçiliğinin zarif birleşimini yansıtır.");
        model.addAttribute("aboutP2", lang.equals("en") ? "For us, speakers are not just electronic devices, but also works of art and designs that add value to your living spaces. We produce completely custom speaker cabinets and systems according to your chosen wood type and special design requests." : "Bizim için hoparlörler sadece birer elektronik cihaz değil, aynı zamanda sanat eseri ve yaşam alanlarınıza değer katan tasarımlardır. Seçtiğiniz ahşap türü ve özel tasarım talepleriniz doğrultusunda, tamamen kişiye özel hoparlör kabinleri ve sistemler üretiyoruz.");
        return "pages/about";
    }

    @GetMapping("/iletisim")
    public String contact(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Contact" : "İletişim");
        model.addAttribute("description", lang.equals("en")
                ? "Get in touch with Mirror Acoustics for custom speakers."
                : "Özel hoparlörler için Mirror Acoustics ile iletişime geçin.");

        // contact fields: prefer dynamic settings, otherwise fall back to defaults
        String defaultPhone = "+90 532 704 0949";
        String defaultEmail = "info@mirroracoustics.com";

        String settingPhone = settingService != null ? settingService.get("contact.phone.number", lang) : null;
        String settingEmail = settingService != null ? settingService.get("contact.email.address", lang) : null;

        String phone = (settingPhone != null && !settingPhone.isBlank()) ? settingPhone : defaultPhone;
        String email = (settingEmail != null && !settingEmail.isBlank()) ? settingEmail : defaultEmail;

        model.addAttribute("contactPhone", phone);
        model.addAttribute("contactEmail", email);
        return "pages/contact";
    }

    @GetMapping("/kvkk")
    public String privacy(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Privacy Policy" : "KVKK");
        model.addAttribute("description", lang.equals("en")
                ? "Privacy policy and data processing information."
                : "KVKK ve kişisel verilerin işlenmesine ilişkin bilgiler.");
        return "pages/privacy";
    }

    @GetMapping("/teslimat")
    public String delivery(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Delivery" : "Teslimat");
        model.addAttribute("description", lang.equals("en")
                ? "Delivery and shipping information for orders."
                : "Siparişler için teslimat ve kargo bilgileri.");
        return "pages/delivery";
    }

    @GetMapping({"/iade-degisim", "/en/returns"})
    public String returns(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Returns" : "İade & Değişim");
        model.addAttribute("description", lang.equals("en")
                ? "Returns and exchanges policy."
                : "İade ve değişim politikası.");
        return "pages/returns";
    }

    @GetMapping({"/sss", "/en/faq"})
    public String faq(Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "FAQ" : "SSS");
        model.addAttribute("description", lang.equals("en")
                ? "Frequently asked questions about our products and process."
                : "Ürünlerimiz ve süreç hakkında sıkça sorulan sorular.");
        return "pages/faq";
    }
}
