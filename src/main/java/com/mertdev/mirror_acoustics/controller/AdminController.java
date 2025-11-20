package com.mertdev.mirror_acoustics.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.mertdev.mirror_acoustics.domain.Setting;
import java.util.UUID;
import java.util.Locale;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mertdev.mirror_acoustics.domain.Category;
import com.mertdev.mirror_acoustics.domain.Product;
import com.mertdev.mirror_acoustics.domain.ProductImage;
import com.mertdev.mirror_acoustics.repository.CategoryRepository;
import com.mertdev.mirror_acoustics.repository.ProductRepository;
import com.mertdev.mirror_acoustics.repository.ProductImageRepository;
import com.mertdev.mirror_acoustics.repository.OrderDraftRepository;
import com.mertdev.mirror_acoustics.service.StorageService;
import com.mertdev.mirror_acoustics.util.Slugger;
import com.mertdev.mirror_acoustics.web.AdminPasswordForm;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ProductRepository repo;
    private final CategoryRepository categories;
    private final StorageService storage;
    private final OrderDraftRepository orderDrafts;
    private final ProductImageRepository imagesRepo;
    private final com.mertdev.mirror_acoustics.repository.ShowcaseImageRepository showcaseRepo;
    private final com.mertdev.mirror_acoustics.service.SettingService settingService;
    private final com.mertdev.mirror_acoustics.service.AdminCredentialService adminCredentialService;

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
        m.addAttribute("isEdit", false);
        return "admin/product-form";
    }

    @GetMapping("/products/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        Product p = repo.findById(id).orElseThrow();
        m.addAttribute("item", p);
        m.addAttribute("categories", categories.findAll(Sort.by("nameTr")));
        m.addAttribute("images", imagesRepo.findByProductIdOrderBySortOrderAsc(id));
        m.addAttribute("isEdit", true);
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

    @PostMapping("/products/{id}/images/{imgId}/delete")
    public String deleteImage(@PathVariable Long id, @PathVariable Long imgId) {
        imagesRepo.deleteById(imgId);
        return "redirect:/admin/products/" + id + "/edit";
    }

    @PostMapping("/products/{id}/images/{imgId}/cover")
    public String setCover(@PathVariable Long id, @PathVariable Long imgId) {
        List<ProductImage> imgs = imagesRepo.findByProductIdOrderBySortOrderAsc(id);
        int i = 0;
        for (ProductImage pi : imgs) {
            if (pi.getId().equals(imgId)) {
                pi.setSortOrder(0);
            } else {
                pi.setSortOrder(++i);
            }
        }
        imagesRepo.saveAll(imgs);
        return "redirect:/admin/products/" + id + "/edit";
    }

    @PostMapping("/products/{id}/images/reorder")
    public String reorderImages(@PathVariable Long id, @RequestParam List<Long> orderedIds) {
        List<ProductImage> imgs = imagesRepo.findByProductIdOrderBySortOrderAsc(id);
        for (int i = 0; i < orderedIds.size(); i++) {
            final int idx = i;
            Long targetId = orderedIds.get(i);
            imgs.stream().filter(pi -> pi.getId().equals(targetId)).findFirst()
                .ifPresent(pi -> pi.setSortOrder(idx));
        }
        imagesRepo.saveAll(imgs);
        return "redirect:/admin/products/" + id + "/edit";
    }

    @PostMapping(value = "/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String update(@PathVariable Long id,
            @RequestParam String titleTr,
            @RequestParam String titleEn,
            @RequestParam BigDecimal price,
            @RequestParam(defaultValue = "TRY") String currency,
            @RequestParam(required = false) String descriptionTr,
            @RequestParam(required = false) String descriptionEn,
            @RequestParam(defaultValue = "false") boolean featured,
            @RequestParam(required = false) Integer featuredOrder,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(name = "images", required = false) List<MultipartFile> images) throws Exception {
        Product p = repo.findById(id).orElseThrow();
        p.setTitleTr(titleTr);
        p.setTitleEn(titleEn);
        p.setPrice(price);
        p.setCurrency(currency);
        p.setDescriptionTr(descriptionTr);
        p.setDescriptionEn(descriptionEn);
        p.setFeatured(featured);
        p.setFeaturedOrder(featuredOrder);
        if (categoryId != null) {
            categories.findById(categoryId).ifPresent(p::setCategory);
        } else {
            p.setCategory(null);
        }
        // Basit: Yeni yüklenen görselleri sona ekle (mevcutları koru)
        if (images != null) {
            int start = p.getImages() != null ? p.getImages().size() : 0;
            for (int i = 0; i < images.size(); i++) {
                MultipartFile f = images.get(i);
                String url = storage.save(f);
                if (url != null) {
                    ProductImage img = new ProductImage();
                    img.setProduct(p);
                    img.setUrl(url);
                    img.setSortOrder(start + i);
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

    @GetMapping("/categories/{id}/edit")
    public String editCategory(@PathVariable Long id, Model m) {
        Category c = categories.findById(id).orElseThrow();
        m.addAttribute("item", c);
        return "admin/category-form";
    }

    @PostMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id,
            @RequestParam String nameTr,
            @RequestParam String nameEn) {
        Category c = categories.findById(id).orElseThrow();
        c.setNameTr(nameTr);
        c.setNameEn(nameEn);
        c.setSlug(Slugger.slugify(nameTr));
        categories.save(c);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categories.deleteById(id);
        return "redirect:/admin/categories";
    }


    @GetMapping("/orders-drafts")
    public String listOrderDrafts(Model m) {
        m.addAttribute("items", orderDrafts.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        return "admin/orders-drafts";
    }

    // Showcase (Vitrin)
    @GetMapping("/showcase")
    public String showcase(Model m) {
        m.addAttribute("items", showcaseRepo.findAllByOrderBySortOrderAsc());
        return "admin/showcase";
    }

    @PostMapping(value = "/showcase/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String showcaseUpload(@RequestParam(name = "images", required = false) List<MultipartFile> images,
                                 @RequestParam(required = false) String titleTr,
                                 @RequestParam(required = false) String titleEn) throws Exception {
        int start = showcaseRepo.findAll().size();
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile f = images.get(i);
                String url = storage.saveShowcaseImage(f);
                if (url != null) {
                    String contentType = f.getContentType();
                    String original = f.getOriginalFilename();
                    boolean isVideo = false;
                    if (contentType != null && contentType.toLowerCase(Locale.ROOT).startsWith("video/")) {
                        isVideo = true;
                    } else if (original != null) {
                        String lower = original.toLowerCase(Locale.ROOT);
                        isVideo = lower.endsWith(".mp4") || lower.endsWith(".webm") || lower.endsWith(".ogg")
                                || lower.endsWith(".mov") || lower.endsWith(".avi");
                    }

                    com.mertdev.mirror_acoustics.domain.ShowcaseImage si = new com.mertdev.mirror_acoustics.domain.ShowcaseImage();
                    si.setUrl(url);
                    si.setTitleTr(titleTr);
                    si.setTitleEn(titleEn);
                    si.setSortOrder(start + i);
                    si.setType(isVideo ? "video" : "image");
                    if (isVideo) {
                        si.setVideoUrl(url);
                        si.setDisplayMode("contain");
                    }
                    showcaseRepo.save(si);
                }
            }
        }
        return "redirect:/admin/showcase";
    }

    @PostMapping("/showcase/{id}/delete")
    public String showcaseDelete(@PathVariable Long id) {
        showcaseRepo.deleteById(id);
        return "redirect:/admin/showcase";
    }

    @PostMapping("/showcase/{id}/toggle")
    public String showcaseToggle(@PathVariable Long id) {
        var item = showcaseRepo.findById(id).orElseThrow();
        item.setActive(!item.isActive());
        showcaseRepo.save(item);
        return "redirect:/admin/showcase";
    }

    @PostMapping("/showcase/{id}/move")
    public String showcaseMove(@PathVariable Long id, @RequestParam String dir) {
        var items = showcaseRepo.findAllByOrderBySortOrderAsc();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                int j = dir.equals("up") ? i - 1 : i + 1;
                if (j >= 0 && j < items.size()) {
                    int a = items.get(i).getSortOrder();
                    int b = items.get(j).getSortOrder();
                    items.get(i).setSortOrder(b);
                    items.get(j).setSortOrder(a);
                    showcaseRepo.save(items.get(i));
                    showcaseRepo.save(items.get(j));
                }
                break;
            }
        }
        return "redirect:/admin/showcase";
    }

    @PostMapping("/showcase/{id}/settings")
    public String showcaseSettings(@PathVariable Long id,
                                   @RequestParam(required = false) String type,
                                   @RequestParam(required = false) String videoUrl,
                                   @RequestParam(required = false) String displayMode,
                                   @RequestParam(required = false) Integer frameHeight,
                                   @RequestParam(required = false) String objectPosition) {
        var item = showcaseRepo.findById(id).orElseThrow();
        if (type != null) {
            String normalized = type.trim().toLowerCase(Locale.ROOT);
            item.setType(normalized.equals("video") ? "video" : "image");
        }
        if (videoUrl != null) {
            String trimmed = videoUrl.trim();
            item.setVideoUrl(trimmed.isEmpty() ? null : trimmed);
        }
        if (displayMode != null) item.setDisplayMode(displayMode);
        item.setFrameHeight(frameHeight);
        if (objectPosition != null) item.setObjectPosition(objectPosition);
        if ("video".equalsIgnoreCase(item.getType())) {
            if (item.getVideoUrl() == null || item.getVideoUrl().isBlank()) {
                item.setVideoUrl(item.getUrl());
            }
            if (item.getDisplayMode() == null || item.getDisplayMode().isBlank()) {
                item.setDisplayMode("contain");
            }
        } else {
            item.setType("image");
            item.setVideoUrl(null);
        }
        showcaseRepo.save(item);
        return "redirect:/admin/showcase";
    }

    @GetMapping("/profile/password")
    public String adminPasswordForm(Model model) {
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new AdminPasswordForm());
        }
        model.addAttribute("adminUsername", adminCredentialService.getAdminUsername());
        return "admin/password-form";
    }

    @PostMapping("/profile/password")
    public String updateAdminPassword(
            @Valid @ModelAttribute("passwordForm") AdminPasswordForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (!bindingResult.hasFieldErrors("newPassword") && !bindingResult.hasFieldErrors("confirmPassword")) {
            if (!form.getNewPassword().equals(form.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "password.mismatch", "Yeni şifre tekrarı eşleşmiyor.");
            }
        }

        if (!bindingResult.hasFieldErrors("currentPassword")
                && !adminCredentialService.matchesCurrentPassword(form.getCurrentPassword())) {
            bindingResult.rejectValue("currentPassword", "password.invalid", "Mevcut şifre yanlış.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("adminUsername", adminCredentialService.getAdminUsername());
            return "admin/password-form";
        }

        adminCredentialService.updatePassword(form.getNewPassword());
        redirectAttributes.addFlashAttribute("passwordChanged", true);
        return "redirect:/admin/profile/password";
    }

    // Contact settings (multilanguage)
    @GetMapping("/contact-settings")
    public String contactSettings(Model m) {
        // Group keys to render a more user-friendly admin UI
        Map<String, List<Setting>> groups = new LinkedHashMap<>();

        groups.put("Address", new ArrayList<>());
        groups.put("Phone", new ArrayList<>());
        groups.put("Email", new ArrayList<>());
        groups.put("WhatsApp", new ArrayList<>());
        groups.put("Hours", new ArrayList<>());

        // Address
        groups.get("Address").add(settingService.find("contact.address.value"));
        groups.get("Address").add(settingService.find("contact.address.note"));

        // Phone
        groups.get("Phone").add(settingService.find("contact.phone.title"));
        groups.get("Phone").add(settingService.find("contact.phone.number"));
        groups.get("Phone").add(settingService.find("contact.phone.hours"));

        // Email
        groups.get("Email").add(settingService.find("contact.email.title"));
        groups.get("Email").add(settingService.find("contact.email.address"));
        groups.get("Email").add(settingService.find("contact.email.note"));

        // WhatsApp
        groups.get("WhatsApp").add(settingService.find("contact.whatsapp.phone"));
        groups.get("WhatsApp").add(settingService.find("contact.whatsapp.prefill"));
        groups.get("WhatsApp").add(settingService.find("contact.whatsapp.button"));

        // Hours
        groups.get("Hours").add(settingService.find("contact.hours.title"));
        groups.get("Hours").add(settingService.find("contact.hours.weekdays"));
        groups.get("Hours").add(settingService.find("contact.hours.saturday"));
        groups.get("Hours").add(settingService.find("contact.hours.sunday"));
        groups.get("Hours").add(settingService.find("contact.hours.closed"));
        groups.get("Hours").add(settingService.find("contact.hours.emergency"));

        m.addAttribute("settingsGroups", groups);
        return "admin/contact-settings";
    }

    @PostMapping("/contact-settings")
    public String saveContactSettings(jakarta.servlet.http.HttpServletRequest req, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
    String[] keys = new String[]{
        "contact.address.value",
        "contact.address.note",
        "contact.phone.title",
        "contact.phone.number",
        "contact.phone.hours",
        "contact.email.title",
        "contact.email.address",
        "contact.email.note",
        "contact.whatsapp.phone",
        "contact.whatsapp.prefill",
        "contact.whatsapp.button",
        "contact.hours.title",
        "contact.hours.weekdays",
        "contact.hours.saturday",
        "contact.hours.sunday",
        "contact.hours.closed",
        "contact.hours.emergency"
    };
        for (String key : keys) {
            String base = key.replace('.', '_');
            String tr = req.getParameter(base + "_tr");
            String en = req.getParameter(base + "_en");
            if (tr == null) tr = "";
            if (en == null) en = "";
            settingService.save(key, tr, en);
        }
        // use flash attribute for success message instead of query param
        ra.addFlashAttribute("message", "Contact settings saved successfully.");
        return "redirect:/admin/contact-settings";
    }

    // About admin endpoints have been removed to restore previous behavior (use message bundles)
}


