package com.example.apiserver.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class OrderRequest {
    @NotNull(message = "userId is required")
    private Integer userId;
    
    @NotNull(message = "serviceId is required")
    private Integer serviceId;
}