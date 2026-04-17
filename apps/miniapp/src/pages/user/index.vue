<template>
  <view class="page">
    <view v-if="user" class="surface profile">
      <view class="row">
        <view class="identity">
          <text class="name">账户</text>
          <text class="sub mono">UID #{{ String(user.id).padStart(4, '0') }}</text>
        </view>
        <view class="balance">
          <text class="label">余额</text>
          <text class="value mono">¥{{ Number(user.balance).toFixed(2) }}</text>
        </view>
      </view>
      <view class="meta">
        <text class="meta-item">{{ user.phone || '未绑定手机号' }}</text>
      </view>
    </view>

    <view v-else class="surface profile" @click="goToLogin">
      <view class="row">
        <view class="identity">
          <text class="name">未登录</text>
          <text class="sub">点击登录 / 注册</text>
        </view>
        <button class="btn btn-primary">登录</button>
      </view>
    </view>

    <view class="surface actions" v-if="user">
      <view class="action-row" @click="goToWallet">
        <text class="left">我的钱包</text>
        <text class="right mono">¥{{ Number(user.balance).toFixed(2) }}</text>
      </view>
      <view class="action-row" @click="goToOrders">
        <text class="left">我的订单</text>
        <text class="right">查看</text>
      </view>
      <button class="action-row share" open-type="share">
        <text class="left">分享拉新客</text>
        <text class="right">赚提成</text>
      </button>
    </view>

    <view class="surface danger" v-if="user">
      <view class="action-row" @click="logout">
        <text class="left">退出登录</text>
        <text class="right">注销</text>
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
.profile {
  padding: 28rpx;
  margin-bottom: 20rpx;
}

.identity {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.name {
  font-size: 34rpx;
  font-weight: 800;
  color: var(--text);
}

.sub {
  font-size: 24rpx;
  color: var(--muted);
}

.balance {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8rpx;
}

.balance .label {
  font-size: 22rpx;
  color: var(--muted);
}

.balance .value {
  font-size: 34rpx;
  font-weight: 800;
  color: var(--text);
}

.meta {
  margin-top: 18rpx;
  padding-top: 18rpx;
  border-top: 1px solid var(--hairline);
}

.meta-item {
  font-size: 24rpx;
  color: var(--muted);
}

.actions {
  overflow: hidden;
  margin-bottom: 20rpx;
}

.action-row {
  padding: 24rpx 28rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--hairline);
  background: transparent;
  text-align: left;
  line-height: 1;
}

.action-row::after {
  border: none;
}

.action-row:last-child {
  border-bottom: none;
}

.action-row .left {
  font-size: 28rpx;
  font-weight: 700;
  color: var(--text);
}

.action-row .right {
  font-size: 24rpx;
  color: var(--muted);
}

.action-row.share {
  border-radius: 0;
}

.danger {
  overflow: hidden;
  border-color: rgba(228, 61, 51, 0.18);
}

.danger .action-row .left {
  color: var(--danger);
}
</style>
