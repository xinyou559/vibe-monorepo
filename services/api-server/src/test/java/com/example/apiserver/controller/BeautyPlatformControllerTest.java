package com.example.apiserver.controller;

import com.example.apiserver.common.ErrorCode;
import com.example.apiserver.dto.LoginRequest;
import com.example.apiserver.dto.OrderRequest;
import com.example.apiserver.dto.RechargeRequest;
import com.example.apiserver.entity.Order;
import com.example.apiserver.entity.User;
import com.example.apiserver.service.BeautyPlatformService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BeautyPlatformControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BeautyPlatformService beautyPlatformService;

    @InjectMocks
    private BeautyPlatformController beautyPlatformController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(beautyPlatformController).build();
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setOpenid("test-openid");

        User user = new User();
        user.setId(1);
        user.setOpenid("test-openid");
        user.setBalance(BigDecimal.ZERO);

        when(beautyPlatformService.loginOrRegister(any(LoginRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.openid").value("test-openid"));
    }

    @Test
    void testRecharge_Success() throws Exception {
        RechargeRequest request = new RechargeRequest();
        request.setUserId(1);
        request.setAmount(new BigDecimal("100.00"));

        // recharge returns void

        mockMvc.perform(post("/api/v1/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setUserId(1);
        request.setServiceId(10);

        Order order = new Order();
        order.setId(1);
        order.setUserId(1);
        order.setServiceId(10);
        order.setAmount(new BigDecimal("50.00"));
        order.setStatus("PAID");

        when(beautyPlatformService.createOrder(any(OrderRequest.class))).thenReturn(order);

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.status").value("PAID"))
                .andExpect(jsonPath("$.data.amount").value(50.00));
    }
}