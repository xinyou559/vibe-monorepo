package com.example.apiserver.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction {
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private String type;
    private Integer relatedId;
    private LocalDateTime createdAt;
}
