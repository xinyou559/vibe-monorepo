package com.example.apiserver.service;

import com.example.apiserver.common.BusinessException;
import com.example.apiserver.common.ErrorCode;
import com.example.apiserver.dto.LoginRequest;
import com.example.apiserver.dto.OrderRequest;
import com.example.apiserver.dto.RechargeRequest;
import com.example.apiserver.entity.Order;
import com.example.apiserver.entity.ServiceItem;
import com.example.apiserver.entity.Transaction;
import com.example.apiserver.entity.User;
import com.example.apiserver.mapper.OrderMapper;
import com.example.apiserver.mapper.ServiceItemMapper;
import com.example.apiserver.mapper.TransactionMapper;
import com.example.apiserver.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeautyPlatformServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private ServiceItemMapper serviceItemMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private BeautyPlatformService beautyPlatformService;

    private User testUser;
    private User referrerUser;
    private ServiceItem testService;

    @BeforeEach
    void setUp() {
        referrerUser = new User();
        referrerUser.setId(1);
        referrerUser.setOpenid("referrer-openid");
        referrerUser.setBalance(new BigDecimal("100.00"));

        testUser = new User();
        testUser.setId(2);
        testUser.setOpenid("test-openid");
        testUser.setBalance(new BigDecimal("500.00"));
        testUser.setReferrerId(1);

        testService = new ServiceItem();
        testService.setId(10);
        testService.setName("精油开背");
        testService.setPrice(new BigDecimal("100.00"));
        testService.setCommissionRate(new BigDecimal("0.10")); // 10%
        testService.setStatus("ACTIVE");
    }

    @Test
    void testLoginOrRegister_NewUserWithReferrer() {
        LoginRequest request = new LoginRequest();
        request.setOpenid("new-user");
        request.setReferrerId(1);

        when(userMapper.findByOpenid("new-user")).thenReturn(null);
        when(userMapper.findById(1)).thenReturn(referrerUser);

        User result = beautyPlatformService.loginOrRegister(request);

        assertNotNull(result);
        assertEquals("new-user", result.getOpenid());
        assertEquals(1, result.getReferrerId());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    void testRecharge_Success() {
        RechargeRequest request = new RechargeRequest();
        request.setUserId(2);
        request.setAmount(new BigDecimal("200.00"));

        when(userMapper.findById(2)).thenReturn(testUser);

        beautyPlatformService.recharge(request);

        verify(userMapper, times(1)).updateBalance(eq(2), eq(new BigDecimal("200.00")));
        verify(transactionMapper, times(1)).insert(any(Transaction.class));
    }

    @Test
    void testCreateOrder_SuccessWithCommission() {
        OrderRequest request = new OrderRequest();
        request.setUserId(2);
        request.setServiceId(10);

        when(userMapper.findById(2)).thenReturn(testUser);
        when(serviceItemMapper.findById(10)).thenReturn(testService);
        when(userMapper.findById(1)).thenReturn(referrerUser); // mock referrer for commission

        Order order = beautyPlatformService.createOrder(request);

        assertNotNull(order);
        assertEquals("PAID", order.getStatus());
        assertEquals(new BigDecimal("100.00"), order.getAmount());

        // 验证用户扣款
        verify(userMapper, times(1)).updateBalance(eq(2), eq(new BigDecimal("-100.00")));
        
        // 验证分销提成 (100 * 0.1 = 10)
        verify(userMapper, times(1)).updateBalance(eq(1), eq(new BigDecimal("10.0000")));

        // 验证流水记录 (1笔消费 + 1笔分销)
        verify(transactionMapper, times(2)).insert(any(Transaction.class));
        verify(orderMapper, times(1)).insert(any(Order.class));
    }

    @Test
    void testCreateOrder_InsufficientBalance() {
        testUser.setBalance(new BigDecimal("50.00")); // 余额不足100

        OrderRequest request = new OrderRequest();
        request.setUserId(2);
        request.setServiceId(10);

        when(userMapper.findById(2)).thenReturn(testUser);
        when(serviceItemMapper.findById(10)).thenReturn(testService);

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> beautyPlatformService.createOrder(request));

        assertEquals(ErrorCode.BALANCE_INSUFFICIENT, exception.getErrorCode());
        verify(userMapper, never()).updateBalance(any(), any());
    }
}