<template>
  <view class="page-container">
    <view class="header">
      <text class="text-title">我的订单</text>
    </view>
    
    <view class="order-list">
      <view class="surface-card order-item" v-for="item in orders" :key="item.id">
        <view class="top">
          <text class="order-no">订单号: {{ String(item.id).padStart(6, '0') }}</text>
          <text :class="['status-badge', item.status.toLowerCase()]">{{ formatStatus(item.status) }}</text>
        </view>
        <view class="middle">
          <text class="service-id">服务ID: {{ item.serviceId }}</text>
          <text class="text-accent price">¥{{ Number(item.amount).toFixed(2) }}</text>
        </view>
        <view class="bottom">
          <text class="text-caption time">{{ new Date(item.createdAt).toLocaleString() }}</text>
        </view>
      </view>
      
      <view class="empty" v-if="orders.length === 0">
        <text class="text-caption">暂无订单记录</text>
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
    const res = await request<Order[]>('/admin/orders', 'GET')
    orders.value = res.filter(o => o.userId === userId.value)
  } catch (error) {
    console.error('获取订单失败', error)
  }
}

const formatStatus = (status: string) => {
  const map: Record<string, string> = {
    INIT: '待支付',
    PAID: '待核销',
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

<style scoped>
.header { margin-bottom: 40rpx; }
.order-item { display: flex; flex-direction: column; padding: 32rpx; }
.top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24rpx; border-bottom: 1px solid var(--border-light); padding-bottom: 24rpx; }
.order-no { font-size: 26rpx; color: var(--text-secondary); font-family: monospace; }

.status-badge { font-size: 24rpx; font-weight: 600; padding: 4rpx 12rpx; border-radius: 4rpx; }
.status-badge.init { background: var(--bg-canvas); color: var(--text-tertiary); }
.status-badge.paid { background: rgba(255, 107, 74, 0.08); color: var(--accent-color); }
.status-badge.completed { background: var(--bg-canvas); color: var(--text-primary); }
.status-badge.cancelled { background: rgba(245, 108, 108, 0.08); color: #f56c6c; }

.middle { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20rpx; }
.service-id { font-size: 32rpx; color: var(--text-primary); font-weight: 500; }
.price { font-size: 36rpx; font-variant-numeric: tabular-nums; }
.bottom { text-align: right; }
.empty { text-align: center; padding: 100rpx 0; }
</style>
