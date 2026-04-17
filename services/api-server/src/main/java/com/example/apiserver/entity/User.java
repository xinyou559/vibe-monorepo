package com.example.apiserver.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String openid;
    private String phone;
    private BigDecimal balance;
    private Integer referrerId;
    private LocalDateTime createdAt;
}
