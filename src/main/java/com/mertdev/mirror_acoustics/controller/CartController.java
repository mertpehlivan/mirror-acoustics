package com.mertdev.mirror_acoustics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mertdev.mirror_acoustics.domain.Product;
import com.mertdev.mirror_acoustics.service.CartService;
import com.mertdev.mirror_acoustics.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/cart", "/en/cart"})
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

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
                      @RequestParam(defaultValue = "tr") String lang) {
        Product product = productService.getById(productId);
        cartService.addProduct(product, qty);
        return "redirect:/cart?lang=" + lang;
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long productId,
                         @RequestParam(defaultValue = "tr") String lang) {
        cartService.removeProduct(productId);
        return "redirect:/cart?lang=" + lang;
    }
}
