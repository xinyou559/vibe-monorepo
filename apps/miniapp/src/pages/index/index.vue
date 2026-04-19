<template>
  <view class="page-container">
    <view class="header">
      <text class="text-title">推荐美业服务</text>
    </view>
    
    <view class="service-list">
      <view class="surface-card service-item" v-for="item in services" :key="item.id">
        <view class="info">
          <text class="text-subtitle">{{ item.name }}</text>
          <text class="text-caption desc">{{ item.description || '暂无描述' }}</text>
          <text class="text-accent price">¥ {{ Number(item.price).toFixed(2) }}</text>
        </view>
        <button class="buy-btn" @click="buyService(item.id)">立即购买</button>
      </view>
      
      <view class="empty" v-if="services.length === 0">
        <text class="text-caption">暂无服务上架</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { request } from '../../utils/request'
import type { ServiceItem } from 'shared-types'

const services = ref<ServiceItem[]>([])

const fetchServices = async () => {
  try {
    services.value = await request<ServiceItem[]>('/services', 'GET')
  } catch (error) {
    console.error('获取服务失败', error)
  }
}

const buyService = async (serviceId: number) => {
  const user = uni.getStorageSync('user')
  if (!user) {
    uni.showToast({ title: '请先登录', icon: 'none' })
    setTimeout(() => {
      uni.switchTab({ url: '/pages/user/index' })
    }, 1000)
    return
  }

  uni.showModal({
    title: '确认购买',
    content: '将使用余额支付，是否继续？',
    confirmColor: '#FF6B4A',
    success: async (res) => {
      if (res.confirm) {
        try {
          await request('/orders', 'POST', {
            userId: user.id,
            serviceId
          })
          uni.showToast({ title: '购买成功', icon: 'success' })
          // 可以在此处跳转订单页或更新余额
        } catch (error) {
          // 错误已被 request 拦截并弹窗
        }
      }
    }
  })
}

onLoad((options) => {
  // 如果从分享链接进来，保存推荐人ID
  if (options && options.referrerId) {
    uni.setStorageSync('referrerId', Number(options.referrerId))
  }
  fetchServices()
})
</script>

<style scoped>
.header { margin-bottom: 40rpx; }
.service-item { display: flex; justify-content: space-between; align-items: flex-end; }
.info { display: flex; flex-direction: column; flex: 1; }
.desc { margin: 8rpx 0 16rpx 0; }
.price { font-size: 32rpx; font-variant-numeric: tabular-nums; }
.buy-btn { margin: 0; padding: 0 32rpx; height: 56rpx; line-height: 56rpx; font-size: 24rpx; background: var(--accent-color); color: var(--text-inverse); border-radius: var(--radius-sm); border: none; font-weight: 500; }
.buy-btn::after { border: none; }
.buy-btn:active { background: var(--accent-hover); transform: scale(0.96); }
.empty { text-align: center; padding: 100rpx 0; }
</style>
