<template>
  <view class="page-container">
    <view class="user-hero" v-if="user">
      <view class="avatar">
        <text class="avatar-text">{{ user.phone ? user.phone.slice(-4) : 'User' }}</text>
      </view>
      <view class="user-info">
        <text class="phone">{{ user.phone || '未绑定手机号' }}</text>
        <text class="id-text">ID: {{ user.id }}</text>
      </view>
    </view>
    <view class="user-hero not-login" v-else @click="goToLogin">
      <view class="avatar"><text class="avatar-text">?</text></view>
      <view class="user-info">
        <text class="phone">点击登录/注册</text>
        <text class="id-text">体验完整功能</text>
      </view>
    </view>

    <view class="menu-list" v-if="user">
      <view class="surface-card menu-item" @click="goToWallet">
        <text class="text-subtitle">我的钱包</text>
        <text class="text-accent menu-value">¥{{ user.balance }}</text>
      </view>
      <view class="surface-card menu-item" @click="goToOrders">
        <text class="text-subtitle">我的订单</text>
        <text class="arrow">›</text>
      </view>
      <button class="surface-card menu-item share-btn" open-type="share">
        <text class="text-subtitle">分享拉新客 (赚提成)</text>
        <text class="arrow">›</text>
      </button>
      <view class="surface-card menu-item" @click="logout">
        <text class="text-subtitle text-danger">退出登录</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow, onShareAppMessage } from '@dcloudio/uni-app'
import { request } from '../../utils/request'
import type { User } from 'shared-types'

const user = ref<User | null>(null)

const fetchUserInfo = async (id: number) => {
  try {
    const res = await request<User>(`/users/${id}`, 'GET')
    user.value = res
    uni.setStorageSync('user', res) // 更新本地缓存的余额
  } catch (error) {
    console.error(error)
  }
}

const goToLogin = () => {
  uni.navigateTo({ url: '/pages/user/login' })
}

const goToWallet = () => {
  uni.navigateTo({ url: '/pages/user/wallet' })
}

const goToOrders = () => {
  uni.navigateTo({ url: '/pages/order/index' })
}

const logout = () => {
  uni.removeStorageSync('user')
  user.value = null
}

onShow(() => {
  const localUser = uni.getStorageSync('user')
  if (localUser && localUser.id) {
    fetchUserInfo(localUser.id)
  }
})

// 小程序分享，带上推荐人ID
onShareAppMessage(() => {
  const currentUserId = user.value?.id || ''
  return {
    title: '推荐给你一家超赞的美业店！',
    path: `/pages/index/index?referrerId=${currentUserId}`
  }
})
</script>

<style scoped>
.user-hero { background: var(--bg-midnight); padding: 60rpx 40rpx; border-radius: var(--radius-lg); display: flex; align-items: center; margin-bottom: 40rpx; color: var(--text-inverse); box-shadow: 0 20rpx 40rpx rgba(26, 27, 38, 0.1); }
.not-login { background: var(--bg-surface); color: var(--text-primary); border: 1px solid var(--border-light); box-shadow: none; }
.not-login .avatar { background: var(--bg-canvas); color: var(--text-tertiary); }
.not-login .id-text { color: var(--text-tertiary); }
.avatar { width: 120rpx; height: 120rpx; background: var(--accent-color); border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-right: 32rpx; box-shadow: 0 8rpx 16rpx rgba(255, 107, 74, 0.3); }
.avatar-text { font-size: 36rpx; font-weight: 700; color: var(--text-inverse); }
.user-info { display: flex; flex-direction: column; }
.phone { font-size: 40rpx; font-weight: 700; margin-bottom: 8rpx; letter-spacing: -0.5rpx; }
.id-text { font-size: 26rpx; opacity: 0.6; font-variant-numeric: tabular-nums; }
.menu-list { display: flex; flex-direction: column; gap: 16rpx; }
.menu-item { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0; padding: 40rpx 32rpx; transition: transform 0.2s, background 0.2s; }
.menu-item:active { transform: scale(0.98); background: #FAFAFA; }
.menu-value { font-size: 32rpx; font-variant-numeric: tabular-nums; }
.arrow { color: var(--text-tertiary); font-size: 40rpx; line-height: 1; }
.share-btn { background: var(--bg-surface); text-align: left; line-height: 1.5; font-size: inherit; }
.share-btn::after { border: none; }
.text-danger { color: #E43D33; }
</style>
