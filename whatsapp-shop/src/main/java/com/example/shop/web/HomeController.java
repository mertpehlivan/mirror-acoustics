package com.example.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("pageTitle", "Mağaza");
    model.addAttribute("pageDescription", "Kaliteli ürünler");
    return "index";
  }
}


