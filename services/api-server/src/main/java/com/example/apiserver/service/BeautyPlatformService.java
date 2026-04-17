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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeautyPlatformService {

    private final UserMapper userMapper;
    private final ServiceItemMapper serviceItemMapper;
    private final OrderMapper orderMapper;
    private final TransactionMapper transactionMapper;

    @Transactional
    public User loginOrRegister(LoginRequest request) {
        User user = userMapper.findByOpenid(request.getOpenid());
        if (user == null) {
            user = new User();
            user.setOpenid(request.getOpenid());
            user.setPhone(request.getPhone());
            user.setBalance(BigDecimal.ZERO);
            // 仅在注册时绑定推荐人，且不能是自己
            if (request.getReferrerId() != null) {
                User referrer = userMapper.findById(request.getReferrerId());
                if (referrer != null && !request.getOpenid().equals(referrer.getOpenid())) {
                    user.setReferrerId(request.getReferrerId());
                }
            }
            userMapper.insert(user);
        }
        return user;
    }

    public User getUserInfo(Integer userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public List<ServiceItem> getActiveServices() {
        return serviceItemMapper.findByStatus("ACTIVE");
    }

    @Transactional
    public void recharge(RechargeRequest request) {
        User user = userMapper.findById(request.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 增加余额
        userMapper.updateBalance(user.getId(), request.getAmount());

        // 记录流水
        Transaction tx = new Transaction();
        tx.setUserId(user.getId());
        tx.setAmount(request.getAmount());
        tx.setType("RECHARGE");
        transactionMapper.insert(tx);
    }

    @Transactional
    public Order createOrder(OrderRequest request) {
        User user = userMapper.findById(request.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        ServiceItem service = serviceItemMapper.findById(request.getServiceId());
        if (service == null) {
            throw new BusinessException(ErrorCode.SERVICE_NOT_FOUND);
        }
        if (!"ACTIVE".equals(service.getStatus())) {
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
        }

        if (user.getBalance().compareTo(service.getPrice()) < 0) {
            throw new BusinessException(ErrorCode.BALANCE_INSUFFICIENT);
        }

        // 扣减余额
        userMapper.updateBalance(user.getId(), service.getPrice().negate());

        // 创建订单 (直接设为 PAID 状态，因为是余额扣款)
        Order order = new Order();
        order.setUserId(user.getId());
        order.setServiceId(service.getId());
        order.setAmount(service.getPrice());
        order.setStatus("PAID");
        orderMapper.insert(order);

        // 记录消费流水
        Transaction consumeTx = new Transaction();
        consumeTx.setUserId(user.getId());
        consumeTx.setAmount(service.getPrice().negate());
        consumeTx.setType("CONSUME");
        consumeTx.setRelatedId(order.getId());
        transactionMapper.insert(consumeTx);

        // 处理分销提成
        if (user.getReferrerId() != null && service.getCommissionRate().compareTo(BigDecimal.ZERO) > 0) {
            User referrer = userMapper.findById(user.getReferrerId());
            if (referrer != null) {
                BigDecimal commissionAmount = service.getPrice().multiply(service.getCommissionRate());
                
                // 增加推荐人余额
                userMapper.updateBalance(referrer.getId(), commissionAmount);
                
                // 记录提成流水
                Transaction commissionTx = new Transaction();
                commissionTx.setUserId(referrer.getId());
                commissionTx.setAmount(commissionAmount);
                commissionTx.setType("COMMISSION");
                commissionTx.setRelatedId(order.getId());
                transactionMapper.insert(commissionTx);
                
                log.info("Commission granted: userId={}, amount={}", referrer.getId(), commissionAmount);
            }
        }

        return order;
    }
    
    public List<Transaction> getUserTransactions(Integer userId) {
        return transactionMapper.findByUserId(userId);
    }
}