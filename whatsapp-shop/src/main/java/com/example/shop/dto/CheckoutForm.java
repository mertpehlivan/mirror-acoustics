package com.example.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckoutForm {
    @NotBlank @Size(min=3, max=160)
    private String fullName;

    @NotBlank @Size(min=7, max=32)
    private String phone;

    @Email
    private String email;

    @NotBlank
    private String address;

    private String shippingPref;
    private String note;
}


