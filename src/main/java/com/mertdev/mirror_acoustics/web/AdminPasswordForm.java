package com.mertdev.mirror_acoustics.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminPasswordForm {

    @NotBlank(message = "Mevcut şifre gereklidir.")
    private String currentPassword;

    @NotBlank(message = "Yeni şifre gereklidir.")
    @Size(min = 8, max = 128, message = "Yeni şifre 8-128 karakter arası olmalıdır.")
    private String newPassword;

    @NotBlank(message = "Yeni şifre tekrarını giriniz.")
    private String confirmPassword;
}
