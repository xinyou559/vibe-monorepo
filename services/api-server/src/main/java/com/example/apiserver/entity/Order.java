package com.example.apiserver.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类：对应 orders 表
 * 记录用户购买美业服务（MVP 阶段主要是使用余额购买凭证）
 */
@Data
public class Order {
    private Integer id;             // 订单主键
    private Integer userId;         // 购买服务的用户ID
    private Integer serviceId;      // 购买的服务ID
    private BigDecimal amount;      // 实际支付金额
    private String status;          // 订单状态：INIT, PAID, COMPLETED, CANCELLED
    private LocalDateTime createdAt;// 订单创建时间
}
