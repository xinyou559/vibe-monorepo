package com.example.apiserver.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资金流水实体类：对应 transactions 表
 * 用于详细追溯用户余额的所有变动（充值、消费抵扣、分销提成收入等）
 */
@Data
public class Transaction {
    private Integer id;             // 资金流水主键
    private Integer userId;         // 发生资金变动的用户ID
    private BigDecimal amount;      // 变动金额（正数为增加，负数为扣减）
    private String type;            // 流水类型：RECHARGE(充值), CONSUME(消费), COMMISSION(提成)
    private Integer relatedId;      // 关联业务主键（例如 orders.id 或充值单号）
    private LocalDateTime createdAt;// 流水发生时间
}
