package com.example.shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
  private Whatsapp whatsapp = new Whatsapp();

  @Data
  public static class Whatsapp {
    private String phone;
  }
}


