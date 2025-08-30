package com.example.shop.web;

import com.example.shop.dto.CartDTO;
import com.example.shop.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class CartController {
  private final CartService cartService;

  @GetMapping("/cart")
  public String view(HttpSession session, Model model) {
    CartDTO cart = cartService.getCart(session);
    model.addAttribute("cart", cart);
    model.addAttribute("pageTitle", "Sepet");
    return "cart";
  }

  @PostMapping("/cart/add")
  public String add(HttpSession session,
                    @RequestParam Long productId,
                    @RequestParam String title,
                    @RequestParam(defaultValue = "0") Long variantId,
                    @RequestParam(required = false) String variantLabel,
                    @RequestParam BigDecimal price,
                    @RequestParam(defaultValue = "1") int qty) {
    cartService.addItem(session, productId, title, variantId == 0 ? null : variantId, variantLabel, price, qty);
    return "redirect:/cart";
  }

  @PostMapping("/cart/remove")
  public String remove(HttpSession session, @RequestParam int index) {
    cartService.removeItem(session, index);
    return "redirect:/cart";
  }

  @PostMapping("/cart/apply-coupon")
  public String applyCoupon(HttpSession session, @RequestParam String code) {
    cartService.applyCoupon(session, code, BigDecimal.ZERO);
    return "redirect:/cart";
  }
}


