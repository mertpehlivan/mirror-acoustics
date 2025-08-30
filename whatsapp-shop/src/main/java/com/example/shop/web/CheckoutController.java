package com.example.shop.web;

import com.example.shop.config.AppProperties;
import com.example.shop.dto.CartDTO;
import com.example.shop.dto.CheckoutForm;
import com.example.shop.service.CartService;
import com.example.shop.service.WhatsAppService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CheckoutController {
  private final CartService cartService;
  private final WhatsAppService whatsAppService;
  private final AppProperties app;

  @GetMapping("/checkout")
  public String checkout(Model model, HttpSession session) {
    model.addAttribute("cart", cartService.getCart(session));
    if (!model.containsAttribute("form")) {
      model.addAttribute("form", new CheckoutForm());
    }
    model.addAttribute("pageTitle", "Sipariş Özeti");
    return "checkout";
  }

  @PostMapping("/checkout/whatsapp")
  public String submit(@Valid CheckoutForm form, BindingResult br,
                       HttpSession session, RedirectAttributes ra) {
    if (br.hasErrors()) {
      ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
      ra.addFlashAttribute("form", form);
      return "redirect:/checkout";
    }
    CartDTO cart = cartService.getCart(session);
    if (cart.getItems().isEmpty()) {
      ra.addFlashAttribute("err", "Sepet boş");
      return "redirect:/cart";
    }
    String text = whatsAppService.buildOrderMessage(cart, form.getFullName(), form.getPhone(), form.getEmail(),
            form.getAddress(), form.getShippingPref(), form.getNote(), cart.getCouponCode());
    String encoded = whatsAppService.urlEncode(text);
    return "redirect:" + whatsAppService.buildWaLink(app.getWhatsapp().getPhone(), encoded);
  }
}


