package com.example.apiserver.controller;

import com.example.apiserver.common.Result;
import com.example.apiserver.dto.LoginRequest;
import com.example.apiserver.dto.OrderRequest;
import com.example.apiserver.dto.RechargeRequest;
import com.example.apiserver.entity.Order;
import com.example.apiserver.entity.ServiceItem;
import com.example.apiserver.entity.Transaction;
import com.example.apiserver.entity.User;
import com.example.apiserver.service.BeautyPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BeautyPlatformController {

    private final BeautyPlatformService platformService;

    @PostMapping("/auth/login")
    public Result<User> loginOrRegister(@Validated @RequestBody LoginRequest request) {
        return Result.success(platformService.loginOrRegister(request));
    }

    @GetMapping("/users/{userId}")
    public Result<User> getUserInfo(@PathVariable Integer userId) {
        return Result.success(platformService.getUserInfo(userId));
    }

    @GetMapping("/services")
    public Result<List<ServiceItem>> getActiveServices() {
        return Result.success(platformService.getActiveServices());
    }

    @PostMapping("/recharge")
    public Result<Void> recharge(@Validated @RequestBody RechargeRequest request) {
        platformService.recharge(request);
        return Result.success();
    }

    @PostMapping("/orders")
    public Result<Order> createOrder(@Validated @RequestBody OrderRequest request) {
        return Result.success(platformService.createOrder(request));
    }

    @GetMapping("/users/{userId}/transactions")
    public Result<List<Transaction>> getUserTransactions(@PathVariable Integer userId) {
        return Result.success(platformService.getUserTransactions(userId));
    }
}