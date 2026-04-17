package com.example.apiserver.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ServiceItem {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal commissionRate;
    private String status;
    private LocalDateTime createdAt;
}
