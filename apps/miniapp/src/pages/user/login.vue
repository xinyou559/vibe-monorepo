<template>
  <view class="page">
    <view class="hero">
      <text class="title-xl">登录 / 注册</text>
      <text class="title-sm">MVP 模拟登录，用于快速跑通链路</text>
    </view>

    <view class="surface form">
      <view class="field">
        <text class="label">模拟 OpenID</text>
        <input class="input" v-model="openid" placeholder="例如：user_001" />
      </view>

      <view class="field">
        <text class="label">手机号（选填）</text>
        <input class="input" v-model="phone" placeholder="例如：13800000000" />
      </view>

      <view class="field" v-if="referrerId">
        <text class="label">绑定推荐人</text>
        <view class="chip">
          <text class="mono">UID #{{ String(referrerId).padStart(4, '0') }}</text>
          <text class="muted">（不可修改）</text>
        </view>
      </view>

      <button class="btn btn-primary full" @click="handleLogin">继续</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { request } from '../../utils/request'
import type { User } from 'shared-types'

const openid = ref('')
const phone = ref('')
const referrerId = ref<number | null>(null)

const handleLogin = async () => {
  if (!openid.value) {
    uni.showToast({ title: 'OpenID必填', icon: 'none' })
    return
  }

  try {
    const user = await request<User>('/auth/login', 'POST', {
      openid: openid.value,
      phone: phone.value,
      referrerId: referrerId.value
    })
    
    uni.setStorageSync('user', user)
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => {
      uni.switchTab({ url: '/pages/index/index' })
    }, 1000)
  } catch (error) {
    console.error('登录失败', error)
  }
}

onLoad(() => {
  // 读取本地缓存的推荐人ID
  const localRefId = uni.getStorageSync('referrerId')
  if (localRefId) {
    referrerId.value = localRefId
  }
})
</script>

<style>
.hero {
  padding: 24rpx 0 28rpx;
}

.form {
  padding: 28rpx;
  display: flex;
  flex-direction: column;
  gap: 22rpx;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.label {
  font-size: 24rpx;
  color: var(--muted);
  font-weight: 600;
}

.input {
  padding: 20rpx 18rpx;
  border: 1px solid var(--hairline);
  border-radius: 14rpx;
  background: #ffffff;
  font-size: 30rpx;
}

.chip {
  display: flex;
  align-items: center;
  gap: 10rpx;
  padding: 18rpx 16rpx;
  border: 1px solid var(--hairline);
  border-radius: 14rpx;
  background: rgba(17, 17, 17, 0.02);
}

.muted {
  font-size: 24rpx;
  color: var(--muted);
}

.full {
  width: 100%;
  margin-top: 8rpx;
}
</style>
