package com.mertdev.mirror_acoustics.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/cart", "/en/cart"})
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final OrderDraftService orderDraftService;

    @GetMapping
    public String view(@RequestParam(defaultValue = "tr") String lang, Model model) {
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("lang", lang);
        model.addAttribute("title", lang.equals("en") ? "Cart" : "Sepet");
        return "cart";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long productId,
                      @RequestParam(defaultValue = "1") int qty,
                      @RequestParam(defaultValue = "tr") String lang,
                      @RequestParam(required = false) String redirect) {
        Product product = productService.getById(productId);
        cartService.addProduct(product, qty);
        if ("whatsapp".equals(redirect)) {
            return "redirect:/cart?lang=" + lang + "&whatsapp=1";
        }
        return "redirect:/cart?lang=" + lang;
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long productId,
                         @RequestParam(defaultValue = "tr") String lang) {
        cartService.removeProduct(productId);
        return "redirect:/cart?lang=" + lang;
    }

    @PostMapping("/whatsapp")
    public String whatsapp(@RequestParam String name,
                           @RequestParam String phone,
                           @RequestParam(required = false) String email,
                           @RequestParam String address,
                           @RequestParam(required = false) String shipping,
                           @RequestParam(required = false) String note,
                           @RequestParam(defaultValue = "tr") String lang) {
        Cart cart = cartService.getCart();
        orderDraftService.saveDraft(cart, name, phone, email, address, shipping, note, "utm_source=site&utm_medium=whatsapp&utm_campaign=order");

        StringBuilder sb = new StringBuilder("Merhaba, sipariş vermek istiyorum:\n");
        for (CartItem item : cart.getItems()) {
            sb.append("- Ürün: ")
              .append(lang.equals("en") ? item.getProduct().getTitleEn() : item.getProduct().getTitleTr())
              .append(" | Adet: ").append(item.getQuantity())
              .append(" | Fiyat: ").append(item.getProduct().getPrice())
              .append("\n");
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
        return "redirect:https://wa.me/905551112233?text=" + message + "&utm_source=site&utm_medium=whatsapp&utm_campaign=order";
    }
}
