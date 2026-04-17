<template>
  <view class="page">
    <view class="hero">
      <text class="title-xl">我的订单</text>
      <text class="title-sm">已购买服务 · 状态可追踪</text>
    </view>

    <view class="surface list">
      <view class="order-item" v-for="item in orders" :key="item.id">
        <view class="row top">
          <text class="oid mono">#{{ String(item.id).padStart(6, '0') }}</text>
          <view class="badge" :class="item.status.toLowerCase()">
            <text class="dot"></text>
            <text class="label">{{ formatStatus(item.status) }}</text>
          </view>
        </view>
        <view class="row mid">
          <text class="service">服务 ID {{ item.serviceId }}</text>
          <text class="amount mono">¥{{ Number(item.amount).toFixed(2) }}</text>
        </view>
        <view class="time">{{ new Date(item.createdAt).toLocaleString() }}</view>
      </view>

      <view class="empty" v-if="orders.length === 0">
        <text class="empty-title">暂无订单记录</text>
        <text class="empty-sub">购买成功后会显示在这里</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { request } from '../../utils/request'
import type { Order } from 'shared-types'

const orders = ref<Order[]>([])
const userId = ref<number>(0)

const fetchOrders = async () => {
  if (!userId.value) return
  try {
    // 为 MVP 简单过滤：由于管理端有 findAll，这里其实需要带 userId 查询
    // 由于后端未提供根据 userId 查 orders 的专门接口，这里暂用 admin 接口拉全量后前端过滤
    // (仅为 MVP 演示闭环，实际需要新接口 /api/v1/users/{userId}/orders)
    const res = await request<Order[]>('/admin/orders', 'GET')
    orders.value = res.filter(o => o.userId === userId.value)
  } catch (error) {
    console.error('获取订单失败', error)
  }
}

const formatStatus = (status: string) => {
  const map: Record<string, string> = {
    INIT: '待支付',
    PAID: '已支付/待服务',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

onShow(() => {
  const user = uni.getStorageSync('user')
  if (user && user.id) {
    userId.value = user.id
    fetchOrders()
  } else {
    uni.showToast({ title: '请先登录', icon: 'none' })
    setTimeout(() => { uni.navigateBack() }, 1000)
  }
})
</script>

<style>
.hero {
  padding: 24rpx 0 28rpx;
}

.list {
  overflow: hidden;
}

.order-item {
  padding: 26rpx 28rpx;
  border-bottom: 1px solid var(--hairline);
}

.order-item:last-child {
  border-bottom: none;
}

.top {
  margin-bottom: 16rpx;
}

.oid {
  font-size: 26rpx;
  color: var(--muted);
  font-weight: 700;
}

.badge {
  display: inline-flex;
  align-items: center;
  gap: 10rpx;
  padding: 8rpx 14rpx;
  border-radius: 999rpx;
  border: 1px solid var(--hairline);
  background: rgba(17, 17, 17, 0.02);
}

.badge .dot {
  width: 10rpx;
  height: 10rpx;
  border-radius: 999rpx;
  background: var(--muted);
}

.badge .label {
  font-size: 24rpx;
  color: var(--text);
  font-weight: 700;
}

.badge.paid {
  background: rgba(230, 162, 60, 0.08);
  border-color: rgba(230, 162, 60, 0.18);
}
.badge.paid .dot {
  background: #e6a23c;
}

.badge.completed {
  background: rgba(0, 200, 83, 0.08);
  border-color: rgba(0, 200, 83, 0.18);
}
.badge.completed .dot {
  background: #00c853;
}

.badge.cancelled {
  background: rgba(228, 61, 51, 0.08);
  border-color: rgba(228, 61, 51, 0.18);
}
.badge.cancelled .dot {
  background: var(--danger);
}

.mid {
  margin-bottom: 12rpx;
}

.service {
  font-size: 28rpx;
  font-weight: 700;
  color: var(--text);
}

.amount {
  font-size: 30rpx;
  font-weight: 800;
  color: var(--text);
}

.time {
  font-size: 22rpx;
  color: var(--muted);
}

.empty {
  padding: 100rpx 32rpx;
  text-align: center;
}

.empty-title {
  display: block;
  font-size: 30rpx;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 10rpx;
}

.empty-sub {
  display: block;
  font-size: 24rpx;
  color: var(--muted);
}
</style>
