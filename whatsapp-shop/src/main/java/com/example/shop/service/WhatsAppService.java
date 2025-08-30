package com.example.shop.service;

import com.example.shop.dto.CartDTO;
import com.example.shop.dto.CartItemDTO;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class WhatsAppService {
  public String buildOrderMessage(CartDTO cart, String fullName, String phone, String email,
                                  String address, String shipping, String note, String coupon) {
    StringBuilder sb = new StringBuilder();
    sb.append("Merhaba, sipariş vermek istiyorum:%0A");
    for (CartItemDTO it : cart.getItems()) {
      String variant = it.getVariantLabel() != null ? " | Varyant: " + it.getVariantLabel() : "";
      sb.append(String.format("- Ürün: %s%s | Adet: %d | Fiyat: %.2f%%0A",
              it.getTitle(), variant, it.getQty(), it.getUnitPrice()));
    }
    sb.append(String.format("Ara Toplam: %.2f%%0A", cart.getSubtotal()));
    if (coupon != null) sb.append("Kupon: ").append(coupon).append("%0A");
    sb.append("Kargo: ").append(shipping == null ? "-" : shipping).append("%0A");
    sb.append("İsim: ").append(fullName).append("%0A");
    sb.append("Telefon: ").append(phone).append("%0A");
    if (email != null && !email.isBlank()) sb.append("E-posta: ").append(email).append("%0A");
    sb.append("Adres: ").append(address == null ? "-" : address).append("%0A");
    if (note != null && !note.isBlank()) sb.append("Not: ").append(note).append("%0A");
    sb.append("%0A(utm_source=site&utm_medium=whatsapp&utm_campaign=order)");
    return sb.toString();
  }

  public String urlEncode(String s) {
    try { return URLEncoder.encode(s, StandardCharsets.UTF_8); }
    catch (Exception e) { return s; }
  }

  public String buildWaLink(String phoneDigitsOnly, String encodedText) {
    return "https://wa.me/" + phoneDigitsOnly + "?text=" + encodedText;
  }
}


