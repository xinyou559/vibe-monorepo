package com.example.apiserver.controller;

import com.example.apiserver.common.Result;
import com.example.apiserver.entity.Order;
import com.example.apiserver.entity.ServiceItem;
import com.example.apiserver.entity.Transaction;
import com.example.apiserver.entity.User;
import com.example.apiserver.mapper.OrderMapper;
import com.example.apiserver.mapper.ServiceItemMapper;
import com.example.apiserver.mapper.TransactionMapper;
import com.example.apiserver.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ServiceItemMapper serviceItemMapper;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;

    // --- 服务管理 ---
    @PostMapping("/services")
    public Result<ServiceItem> createService(@RequestBody ServiceItem item) {
        if (item.getStatus() == null) item.setStatus("ACTIVE");
        serviceItemMapper.insert(item);
        return Result.success(item);
    }

    @PutMapping("/services/{id}")
    public Result<ServiceItem> updateService(@PathVariable Integer id, @RequestBody ServiceItem item) {
        item.setId(id);
        serviceItemMapper.update(item);
        return Result.success(item);
    }

    // --- 订单管理 ---
    @GetMapping("/orders")
    public Result<List<Order>> getOrders() {
        // MVP: 假设 Mapper 加一个 findAll
        return Result.success(orderMapper.findAll());
    }

    @PostMapping("/orders/{id}/complete")
    public Result<Void> completeOrder(@PathVariable Integer id) {
        orderMapper.updateStatus(id, "COMPLETED");
        return Result.success();
    }

    // --- 用户与流水 ---
    @GetMapping("/users")
    public Result<List<User>> getUsers() {
        return Result.success(userMapper.findAll());
    }

    @GetMapping("/transactions")
    public Result<List<Transaction>> getTransactions() {
        return Result.success(transactionMapper.findAll());
    }
}