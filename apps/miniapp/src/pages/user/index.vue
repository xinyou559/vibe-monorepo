<template>
  <view class="container">
    <view class="user-card" v-if="user">
      <view class="avatar">
        <text class="avatar-text">{{ user.phone ? user.phone.slice(-4) : 'User' }}</text>
      </view>
      <view class="user-info">
        <text class="phone">{{ user.phone || '未绑定手机号' }}</text>
        <text class="id-text">ID: {{ user.id }}</text>
      </view>
    </view>
    <view class="user-card not-login" v-else @click="goToLogin">
      <view class="avatar"><text class="avatar-text">?</text></view>
      <view class="user-info">
        <text class="phone">点击登录/注册</text>
      </view>
    </view>

    <view class="menu-list" v-if="user">
      <view class="menu-item" @click="goToWallet">
        <text class="menu-title">我的钱包</text>
        <text class="menu-desc">余额: ¥{{ user.balance }} ></text>
      </view>
      <view class="menu-item" @click="goToOrders">
        <text class="menu-title">我的订单</text>
        <text class="menu-desc">></text>
      </view>
      <button class="menu-item share-btn" open-type="share">
        <text class="menu-title">分享拉新客（赚提成）</text>
        <text class="menu-desc">></text>
      </button>
      <view class="menu-item" @click="logout">
        <text class="menu-title" style="color: #f56c6c;">退出登录</text>
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

<style>
.container { padding: 20rpx; background: #f5f7fa; min-height: 100vh; }
.user-card { background: #409eff; padding: 40rpx; border-radius: 16rpx; display: flex; align-items: center; margin-bottom: 30rpx; color: #fff; }
.not-login { background: #909399; }
.avatar { width: 100rpx; height: 100rpx; background: rgba(255,255,255,0.2); border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-right: 30rpx; }
.avatar-text { font-size: 32rpx; font-weight: bold; }
.user-info { display: flex; flex-direction: column; }
.phone { font-size: 36rpx; font-weight: bold; margin-bottom: 10rpx; }
.id-text { font-size: 24rpx; opacity: 0.8; }
.menu-list { background: #fff; border-radius: 16rpx; overflow: hidden; }
.menu-item { padding: 30rpx; border-bottom: 1px solid #f5f5f5; display: flex; justify-content: space-between; align-items: center; }
.menu-item:last-child { border-bottom: none; }
.menu-title { font-size: 30rpx; color: #333; }
.menu-desc { font-size: 26rpx; color: #999; }
.share-btn { background: #fff; border-radius: 0; text-align: left; line-height: inherit; display: flex; flex-direction: row; }
.share-btn::after { border: none; }
</style>