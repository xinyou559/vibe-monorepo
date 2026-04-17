<template>
  <view class="container">
    <view class="header">
      <text class="title">推荐美业服务</text>
    </view>
    
    <view class="service-list">
      <view class="service-item" v-for="item in services" :key="item.id">
        <view class="info">
          <text class="name">{{ item.name }}</text>
          <text class="desc">{{ item.description || '暂无描述' }}</text>
          <text class="price">¥ {{ item.price }}</text>
        </view>
        <button class="buy-btn" @click="buyService(item.id)">立即购买</button>
      </view>
      
      <view class="empty" v-if="services.length === 0">
        <text>暂无服务上架</text>
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

<style>
.container { padding: 20rpx; background: #f5f7fa; min-height: 100vh; }
.header { padding: 20rpx 0; }
.title { font-size: 36rpx; font-weight: bold; color: #333; }
.service-list { margin-top: 20rpx; }
.service-item { background: #fff; padding: 30rpx; border-radius: 12rpx; margin-bottom: 20rpx; display: flex; justify-content: space-between; align-items: center; }
.info { display: flex; flex-direction: column; flex: 1; }
.name { font-size: 32rpx; color: #333; font-weight: 500; margin-bottom: 10rpx; }
.desc { font-size: 24rpx; color: #999; margin-bottom: 15rpx; }
.price { font-size: 36rpx; color: #e43d33; font-weight: bold; }
.buy-btn { margin: 0; padding: 0 30rpx; height: 60rpx; line-height: 60rpx; font-size: 26rpx; background: #409eff; color: #fff; border-radius: 30rpx; }
.empty { text-align: center; padding: 50rpx; color: #999; font-size: 28rpx; }
</style>