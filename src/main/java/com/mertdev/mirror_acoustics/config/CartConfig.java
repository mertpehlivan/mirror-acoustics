package com.mertdev.mirror_acoustics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

import com.mertdev.mirror_acoustics.domain.Cart;

@Configuration
public class CartConfig {
    @Bean
    @SessionScope
    public Cart cart() {
        return new Cart();
    }
}
