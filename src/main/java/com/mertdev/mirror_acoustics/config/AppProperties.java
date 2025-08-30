package com.mertdev.mirror_acoustics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Whatsapp whatsapp = new Whatsapp();

    @Getter
    @Setter
    public static class Whatsapp {
        private String phone;
    }
}


