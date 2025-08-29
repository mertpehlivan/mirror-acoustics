package com.mertdev.mirror_acoustics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/admin/login", "/admin/logout").permitAll()
                        .requestMatchers("/admin/**").authenticated()
                        .anyRequest().permitAll())
                .formLogin(f -> f
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/products", true)
                        .failureUrl("/admin/login?error")
                        .permitAll())
                .logout(l -> l
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout"))
                .csrf(csrf -> csrf.disable()); // basitlik için
        return http.build();
    }
}