package com.example.apiserver.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 美业服务项目实体类：对应 service_items 表
 * 由后台管理配置的服务列表，供小程序用户购买
 */
@Data
public class ServiceItem {
    private Integer id;              // 服务项目主键
    private String name;             // 服务名称（如：精油开背）
    private String description;      // 服务详情介绍
    private BigDecimal price;        // 服务售价
    private BigDecimal commissionRate;// 提成比例（例如 0.10 代表 10%）
    private String status;           // 服务状态：ACTIVE/INACTIVE
    private LocalDateTime createdAt; // 创建时间
}
