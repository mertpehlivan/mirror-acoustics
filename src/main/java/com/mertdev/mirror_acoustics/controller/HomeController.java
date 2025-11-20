package com.mertdev.mirror_acoustics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mertdev.mirror_acoustics.service.ProductService;
import com.mertdev.mirror_acoustics.repository.ShowcaseImageRepository;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService products;
    private final ShowcaseImageRepository showcaseRepo;
    // SettingService removed: Home page will use message bundles and defaults

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        String lang = LocaleContextHolder.getLocale().getLanguage();

        model.addAttribute("page", products.listActive(page, 8));
        model.addAttribute("featured", products.listFeatured(0, 6));
        model.addAttribute("lang", lang);

        var showcaseAll = showcaseRepo.findAllByActiveTrueOrderBySortOrderAsc();
        model.addAttribute("showcase",
                (showcaseAll != null && showcaseAll.size() > 8)
                        ? showcaseAll.subList(0, 8)
                        : showcaseAll);

    // Use message bundles / template defaults for hero and about sections
    model.addAttribute("heroTitle", "Mirror Acoustics Custom Audio Products");
    model.addAttribute("heroSubtitle", "El yapımı sanatın en üst seviyesi");
    model.addAttribute("aboutTitle", lang.equals("en") ? "About Us" : "Hakkımızda");
    model.addAttribute("aboutP1", lang.equals("en")
        ? "Mirror Acoustics is a special audio systems brand founded by Ismet Cataloğlu in a workshop in central Istanbul and grown through his meticulous work. Each product reflects the elegant combination of high engineering knowledge and craftsmanship."
        : "Mirror Acoustics, İstanbul merkezli bir atölyede İsmet Çataloğlu tarafından kurulmuş ve onun titiz çalışmalarıyla büyüyen özel bir ses sistemleri markasıdır. Her bir ürün, yüksek mühendislik bilgisi ile el işçiliğinin zarif birleşimini yansıtır.");
    model.addAttribute("aboutP2", lang.equals("en")
        ? "For us, speakers are not just electronic devices, but also works of art and designs that add value to your living spaces. We produce completely custom speaker cabinets and systems according to your chosen wood type and special design requests."
        : "Bizim için hoparlörler sadece birer elektronik cihaz değil, aynı zamanda sanat eseri ve yaşam alanlarınıza değer katan tasarımlardır. Seçtiğiniz ahşap türü ve özel tasarım talepleriniz doğrultusunda, tamamen kişiye özel hoparlör kabinleri ve sistemler üretiyoruz.");
    model.addAttribute("aboutBadge", lang.equals("en") ? "About" : "Hakkımızda");

        // İletişim bilgileri
        model.addAttribute("contactAddress", "İstanbul, Türkiye");
        model.addAttribute("contactEmail", "info@mirroracoustics.com");
        model.addAttribute("contactPhone", "905327040949");

        // Sabit HTML blokları (UTF-8)
        String partnersHtml = """
                <div class="grid grid-cols-2 md:grid-cols-4 gap-8 opacity-60">
                    <div class="text-center">
                        <div class="w-16 h-16 bg-amber-200 rounded-lg mx-auto mb-2"></div>
                        <p class="text-sm text-amber-700">Partner 1</p>
                    </div>
                    <div class="text-center">
                        <div class="w-16 h-16 bg-amber-200 rounded-lg mx-auto mb-2"></div>
                        <p class="text-sm text-amber-700">Partner 2</p>
                    </div>
                    <div class="text-center">
                        <div class="w-16 h-16 bg-amber-200 rounded-lg mx-auto mb-2"></div>
                        <p class="text-sm text-amber-700">Partner 3</p>
                    </div>
                    <div class="text-center">
                        <div class="w-16 h-16 bg-amber-200 rounded-lg mx-auto mb-2"></div>
                        <p class="text-sm text-amber-700">Partner 4</p>
                    </div>
                </div>
                """;

        String testimonialsHtml = """
                <div class="space-y-6">
                    <div class="bg-white p-6 rounded-lg shadow-sm">
                        <p class="text-amber-800 mb-4">"Mükemmel ses kalitesi ve el işçiliği. Gerçekten beklediğimden çok daha iyi!"</p>
                        <div class="flex items-center space-x-3">
                            <div class="w-10 h-10 bg-amber-200 rounded-full"></div>
                            <div>
                                <p class="font-medium text-amber-900">Ahmet Yılmaz</p>
                                <p class="text-sm text-amber-600">Müzik Tutkunu</p>
                            </div>
                        </div>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-sm">
                        <p class="text-amber-800 mb-4">"Kişiye özel tasarım ve üstün kalite. Mirror Acoustics'i herkese tavsiye ederim."</p>
                        <div class="flex items-center space-x-3">
                            <div class="w-10 h-10 bg-amber-200 rounded-full"></div>
                            <div>
                                <p class="font-medium text-amber-900">Mehmet Demir</p>
                                <p class="text-sm text-amber-600">Ses Mühendisi</p>
                            </div>
                        </div>
                    </div>
                </div>
                """;

        String blogHtml = """
                <div class="grid md:grid-cols-3 gap-6">
                    <article class="bg-white rounded-lg shadow-sm overflow-hidden">
                        <div class="h-48 bg-amber-200"></div>
                        <div class="p-6">
                            <h3 class="font-semibold text-amber-900 mb-2">Hi-End Ses Sistemleri</h3>
                            <p class="text-amber-700 text-sm mb-4">El yapımı hoparlörlerin avantajları ve neden tercih edilmeli...</p>
                            <a href="#" class="text-amber-600 hover:text-amber-700 text-sm font-medium">Devamını Oku →</a>
                        </div>
                    </article>
                    <article class="bg-white rounded-lg shadow-sm overflow-hidden">
                        <div class="h-48 bg-amber-200"></div>
                        <div class="p-6">
                            <h3 class="font-semibold text-amber-900 mb-2">Ahşap Seçimi</h3>
                            <p class="text-amber-700 text-sm mb-4">Hoparlör kabinlerinde doğru ahşap türünün seçimi...</p>
                            <a href="#" class="text-amber-600 hover:text-amber-700 text-sm font-medium">Devamını Oku →</a>
                        </div>
                    </article>
                    <article class="bg-white rounded-lg shadow-sm overflow-hidden">
                        <div class="h-48 bg-amber-200"></div>
                        <div class="p-6">
                            <h3 class="font-semibold text-amber-900 mb-2">Ses Odası Tasarımı</h3>
                            <p class="text-amber-700 text-sm mb-4">Mükemmel dinleme deneyimi için odanızı nasıl düzenlemelisiniz...</p>
                            <a href="#" class="text-amber-600 hover:text-amber-700 text-sm font-medium">Devamını Oku →</a>
                        </div>
                    </article>
                </div>
                """;

        model.addAttribute("partnersHtml", partnersHtml);
        model.addAttribute("testimonialsHtml", testimonialsHtml);
        model.addAttribute("blogHtml", blogHtml);

        // Başlık ve CTA metinleri
        model.addAttribute("featuredTitle", lang.equals("en") ? "Featured Products" : "Öne Çıkan Ürünler");
        model.addAttribute("ctaProducts", lang.equals("en") ? "Browse Products" : "Ürünlere Göz At");
        model.addAttribute("ctaContact", lang.equals("en") ? "Contact Us" : "Bize Ulaşın");

        model.addAttribute("title", "Mirror Acoustics — Custom Audio Products");
        model.addAttribute("description", "El yapımı, kişiye özel hi-end hoparlör kabinleri ve sistemler.");

        return "index";
    }
}
