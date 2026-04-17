<template>
  <view class="page">
    <view class="hero">
      <text class="title-xl">今日可约服务</text>
      <text class="title-sm">余额支付 · 分享返佣 · 订单可查</text>
    </view>

    <view class="surface list">
      <view class="item" v-for="item in services" :key="item.id">
        <view class="left">
          <text class="name">{{ item.name }}</text>
          <text class="desc">{{ item.description || '暂无描述' }}</text>
        </view>
        <view class="right">
          <text class="price mono">¥{{ Number(item.price).toFixed(2) }}</text>
          <button class="btn btn-primary" @click="buyService(item.id)">购买</button>
        </view>
      </view>

      <view class="empty" v-if="services.length === 0">
        <text class="empty-title">暂无服务上架</text>
        <text class="empty-sub">请先在后台创建并上架服务项目</text>
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
.hero {
  padding: 24rpx 0 28rpx;
}

.list {
  overflow: hidden;
}

.item {
  padding: 28rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--hairline);
}

.item:last-child {
  border-bottom: none;
}

.left {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
  min-width: 0;
  padding-right: 16rpx;
}

.name {
  font-size: 32rpx;
  font-weight: 700;
  color: var(--text);
  line-height: 1.2;
}

.desc {
  font-size: 26rpx;
  color: var(--muted);
  line-height: 1.35;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 14rpx;
  flex-shrink: 0;
}

.price {
  font-size: 32rpx;
  font-weight: 800;
}

.btn {
  min-width: 160rpx;
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
