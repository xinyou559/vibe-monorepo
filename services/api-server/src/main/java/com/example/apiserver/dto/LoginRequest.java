package com.example.apiserver.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "openid is required")
    private String openid;
    
    private String phone;
    
    private Integer referrerId;
}