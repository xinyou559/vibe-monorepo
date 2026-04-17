package com.example.apiserver.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体类：对应 users 表
 * 记录用户基础信息、余额以及通过分销链接绑定的推荐人
 */
@Data
public class User {
    private Integer id;             // 用户主键
    private String openid;          // 微信唯一标识
    private String phone;           // 用户手机号
    private BigDecimal balance;     // 账户余额（充值、消费、分销提成均影响此值）
    private Integer referrerId;     // 推荐人ID，对应其他用户的id
    private LocalDateTime createdAt;// 注册时间
}
