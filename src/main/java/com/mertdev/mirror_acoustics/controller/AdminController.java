package com.mertdev.mirror_acoustics.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mertdev.mirror_acoustics.domain.Category;
import com.mertdev.mirror_acoustics.domain.Product;
import com.mertdev.mirror_acoustics.domain.ProductImage;
import com.mertdev.mirror_acoustics.repository.CategoryRepository;
import com.mertdev.mirror_acoustics.repository.ProductRepository;
import com.mertdev.mirror_acoustics.service.StorageService;
import com.mertdev.mirror_acoustics.service.SettingService;
import com.mertdev.mirror_acoustics.util.Slugger;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ProductRepository repo;
    private final CategoryRepository categories;
    private final StorageService storage;
    private final SettingService settings;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping
    public String root() {
        return "redirect:/admin/products";
    }

    @GetMapping("/products")
    public String list(Model m) {
        m.addAttribute("items", repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newForm(Model m) {
        m.addAttribute("item", new Product());
        m.addAttribute("categories", categories.findAll(Sort.by("nameTr")));
        return "admin/product-form";
    }

    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String create(@RequestParam String titleTr,
            @RequestParam String titleEn,
            @RequestParam BigDecimal price,
            @RequestParam(defaultValue = "TRY") String currency,
            @RequestParam(required = false) String descriptionTr,
            @RequestParam(required = false) String descriptionEn,
            @RequestParam(defaultValue = "false") boolean featured,
            @RequestParam(required = false) Integer featuredOrder,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(name = "images", required = false) List<MultipartFile> images) throws Exception {
        Product p = new Product();
        p.setTitleTr(titleTr);
        p.setTitleEn(titleEn);
        p.setPrice(price);
        p.setCurrency(currency);
        p.setDescriptionTr(descriptionTr);
        p.setDescriptionEn(descriptionEn);
        p.setFeatured(featured);
        p.setFeaturedOrder(featuredOrder);
        p.setSlug(Slugger.slugify(titleTr) + "-" + UUID.randomUUID().toString().substring(0, 8));
        if (categoryId != null) {
            categories.findById(categoryId).ifPresent(p::setCategory);
        }

        if (images != null) {
            int i = 0;
            for (MultipartFile f : images) {
                String url = storage.save(f);
                if (url != null) {
                    ProductImage img = new ProductImage();
                    img.setProduct(p);
                    img.setUrl(url);
                    img.setSortOrder(i++);
                    img.setAltTextTr(titleTr);
                    img.setAltTextEn(titleEn);
                    p.getImages().add(img);
                }
            }
        }
        repo.save(p);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/categories")
    public String listCategories(Model m) {
        m.addAttribute("items", categories.findAll(Sort.by("nameTr")));
        return "admin/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model m) {
        m.addAttribute("item", new Category());
        return "admin/category-form";
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam String nameTr,
            @RequestParam String nameEn) {
        Category c = new Category();
        c.setNameTr(nameTr);
        c.setNameEn(nameEn);
        c.setSlug(Slugger.slugify(nameTr));
        categories.save(c);
        return "redirect:/admin/categories";
    }

    @GetMapping("/settings")
    public String settingsForm(Model m) {
        m.addAttribute("heroTitleTr", settings.get("hero.title", "tr"));
        m.addAttribute("heroTitleEn", settings.get("hero.title", "en"));
        m.addAttribute("heroSubtitleTr", settings.get("hero.subtitle", "tr"));
        m.addAttribute("heroSubtitleEn", settings.get("hero.subtitle", "en"));
        m.addAttribute("aboutTitleTr", settings.get("about.title", "tr"));
        m.addAttribute("aboutTitleEn", settings.get("about.title", "en"));
        m.addAttribute("aboutP1Tr", settings.get("about.p1", "tr"));
        m.addAttribute("aboutP1En", settings.get("about.p1", "en"));
        m.addAttribute("aboutP2Tr", settings.get("about.p2", "tr"));
        m.addAttribute("aboutP2En", settings.get("about.p2", "en"));
        m.addAttribute("contactAddressTr", settings.get("contact.address", "tr"));
        m.addAttribute("contactAddressEn", settings.get("contact.address", "en"));
        m.addAttribute("contactEmail", settings.get("contact.email", "tr"));
        m.addAttribute("contactPhone", settings.get("contact.phone", "tr"));
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String saveSettings(@RequestParam String heroTitleTr,
            @RequestParam String heroTitleEn,
            @RequestParam String heroSubtitleTr,
            @RequestParam String heroSubtitleEn,
            @RequestParam String aboutTitleTr,
            @RequestParam String aboutTitleEn,
            @RequestParam String aboutP1Tr,
            @RequestParam String aboutP1En,
            @RequestParam String aboutP2Tr,
            @RequestParam String aboutP2En,
            @RequestParam String contactAddressTr,
            @RequestParam String contactAddressEn,
            @RequestParam String contactEmail,
            @RequestParam String contactPhone) {
        settings.save("hero.title", heroTitleTr, heroTitleEn);
        settings.save("hero.subtitle", heroSubtitleTr, heroSubtitleEn);
        settings.save("about.title", aboutTitleTr, aboutTitleEn);
        settings.save("about.p1", aboutP1Tr, aboutP1En);
        settings.save("about.p2", aboutP2Tr, aboutP2En);
        settings.save("contact.address", contactAddressTr, contactAddressEn);
        settings.save("contact.email", contactEmail, contactEmail);
        settings.save("contact.phone", contactPhone, contactPhone);
        return "redirect:/admin/settings";
    }
}