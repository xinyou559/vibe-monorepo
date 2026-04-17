<template>
  <view class="container">
    <view class="header">
      <text class="title">我的订单</text>
    </view>
    
    <view class="order-list">
      <view class="order-item" v-for="item in orders" :key="item.id">
        <view class="top">
          <text class="order-no">订单号: {{ item.id }}</text>
          <text :class="['status', item.status.toLowerCase()]">{{ formatStatus(item.status) }}</text>
        </view>
        <view class="middle">
          <text class="service-id">服务ID: {{ item.serviceId }}</text>
          <text class="price">¥{{ item.amount }}</text>
        </view>
        <view class="bottom">
          <text class="time">{{ new Date(item.createdAt).toLocaleString() }}</text>
        </view>
      </view>
      
      <view class="empty" v-if="orders.length === 0">
        <text>暂无订单记录</text>
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
.container { padding: 20rpx; background: #f5f7fa; min-height: 100vh; }
.header { padding: 20rpx 0; }
.title { font-size: 36rpx; font-weight: bold; color: #333; }
.order-list { margin-top: 20rpx; }
.order-item { background: #fff; padding: 30rpx; border-radius: 12rpx; margin-bottom: 20rpx; display: flex; flex-direction: column; }
.top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20rpx; border-bottom: 1px solid #f5f5f5; padding-bottom: 20rpx; }
.order-no { font-size: 28rpx; color: #666; }
.status { font-size: 28rpx; font-weight: bold; }
.status.init { color: #909399; }
.status.paid { color: #e6a23c; }
.status.completed { color: #67c23a; }
.status.cancelled { color: #f56c6c; }
.middle { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20rpx; }
.service-id { font-size: 32rpx; color: #333; font-weight: 500; }
.price { font-size: 36rpx; color: #e43d33; font-weight: bold; }
.bottom { text-align: right; }
.time { font-size: 24rpx; color: #999; }
.empty { text-align: center; padding: 50rpx; color: #999; font-size: 28rpx; }
</style>