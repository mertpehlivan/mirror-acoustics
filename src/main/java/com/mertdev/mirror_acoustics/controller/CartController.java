package com.mertdev.mirror_acoustics.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mertdev.mirror_acoustics.domain.Cart;
import com.mertdev.mirror_acoustics.domain.CartItem;
import com.mertdev.mirror_acoustics.domain.Product;
import com.mertdev.mirror_acoustics.service.CartService;
import com.mertdev.mirror_acoustics.service.OrderDraftService;
import com.mertdev.mirror_acoustics.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

import com.mertdev.mirror_acoustics.config.AppProperties;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/cart", "/en/cart"})
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final OrderDraftService orderDraftService;
    private final AppProperties appProperties;

    @GetMapping
    public String view(@RequestParam(defaultValue = "tr") String lang, HttpServletRequest request, Model model) {
        // Ensure session is initialized before rendering
        request.getSession(true);

        var cart = cartService.getCart();
        var favorites = cartService.getFavorites();

        var items = cart.getItems();
        boolean hasCartItems = items != null && !items.isEmpty();
        model.addAttribute("hasCartItems", hasCartItems);
        model.addAttribute("cartItems", items);
        model.addAttribute("cartTotal", cart.getTotal());
        model.addAttribute("cartCurrency", hasCartItems ? items.get(0).getProduct().getCurrency() : "");
        model.addAttribute("favoritesIds", favorites.getProductIds());
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Cart" : "Sepet");
        model.addAttribute("description", lang.equals("en")
                ? "View items in your cart and proceed to order."
                : "Sepetinizdeki ürünleri görüntüleyin ve sipariş verin.");
        return "cart";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long productId,
                      @RequestParam(defaultValue = "1") int qty,
                      @RequestParam(required = false) String redirect) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        Product product = productService.getById(productId);
        cartService.addProduct(product, qty);
        if ("whatsapp".equals(redirect)) {
            return "redirect:/cart?whatsapp=1";
        }
        return "redirect:/cart";
    }

    @PostMapping("/fav/add")
    public String addFav(@RequestParam Long productId) {
        cartService.addFavorite(productId);
        return "redirect:/cart";
    }

    @PostMapping("/fav/remove")
    public String removeFav(@RequestParam Long productId) {
        cartService.removeFavorite(productId);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long productId) {
        cartService.removeProduct(productId);
        return "redirect:/cart";
    }

    @PostMapping("/whatsapp/direct")
    public String whatsappDirect(@RequestParam Long productId,
                                 @RequestParam(defaultValue = "1") int qty) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        Product product = productService.getById(productId);
        Cart tempCart = new Cart();
        tempCart.addItem(product, qty);
        orderDraftService.saveDraft(tempCart, null, null, null, null, null, null,
                "utm_source=site&utm_campaign=order");

                StringBuilder sb = new StringBuilder("Merhaba, sipariş vermek istiyorum:\n");
                sb.append("- Ürün: ");
                sb.append(lang.equals("en") ? product.getTitleEn() : product.getTitleTr());
                sb.append(" | Adet: ").append(qty);
                sb.append(" | Fiyat: ").append(product.getPrice());
                sb.append("\n");
        String message = URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);
        String whatsappPhone = appProperties.getWhatsapp().getPhone();
        String wa = "https://wa.me/" + whatsappPhone + "?text=" + message + "&utm_source=site&utm_medium=whatsapp&utm_campaign=order";
        String webWa = "https://web.whatsapp.com/send?phone=" + whatsappPhone + "&text=" + message + "&utm_source=site&utm_medium=whatsapp&utm_campaign=order";
        return "redirect:" + wa + "#" + webWa;
    }

    @PostMapping("/whatsapp")
    public String whatsapp(@RequestParam String name,
                           @RequestParam String phone,
                           @RequestParam(required = false) String email,
                           @RequestParam String address,
                           @RequestParam(required = false) String shipping,
                           @RequestParam(required = false) String note) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        Cart cart = cartService.getCart();
        orderDraftService.saveDraft(cart, name, phone, email, address, shipping, note, "utm_source=site&utm_medium=whatsapp&utm_campaign=order");

        StringBuilder sb = new StringBuilder("Merhaba, sipariş vermek istiyorum:\n");
        for (CartItem item : cart.getItems()) {
            sb.append("- Ürün: ");
            sb.append(lang.equals("en") ? item.getProduct().getTitleEn() : item.getProduct().getTitleTr());
            sb.append(" | Adet: ").append(item.getQuantity());
            sb.append(" | Fiyat: ").append(item.getProduct().getPrice());
            sb.append("\n");
        }
        sb.append("Ara Toplam: ").append(cart.getTotal()).append("\n");
        if (shipping != null && !shipping.isBlank()) {
            sb.append("Kargo: ").append(shipping).append("\n");
        }
        sb.append("İsim: ").append(name).append("\n")
          .append("Telefon: ").append(phone).append("\n")
          .append("Adres: ").append(address);
        if (note != null && !note.isBlank()) {
            sb.append("\nNot: ").append(note);
        }

        String message = URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);
        String whatsappPhone = appProperties.getWhatsapp().getPhone();
        String wa = "https://wa.me/" + whatsappPhone + "?text=" + message + "&utm_source=site&utm_medium=whatsapp&utm_campaign=order";
        String webWa = "https://web.whatsapp.com/send?phone=" + whatsappPhone + "&text=" + message + "&utm_source=site&utm_medium=whatsapp&utm_campaign=order";
        return "redirect:" + wa + "#" + webWa;
    }
}
