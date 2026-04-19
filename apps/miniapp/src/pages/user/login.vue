<template>
  <view class="page-container">
    <view class="header">
      <text class="text-title">登录美业系统</text>
      <text class="text-subtitle" style="margin-top: 16rpx; display: block;">体验完整的服务与分销</text>
    </view>
    
    <view class="form">
      <view class="input-item">
        <text class="label">模拟 OpenID</text>
        <input class="input" v-model="openid" placeholder="输入唯一标识如 user123" />
      </view>
      <view class="input-item">
        <text class="label">手机号(选填)</text>
        <input class="input" v-model="phone" placeholder="输入手机号" />
      </view>
      <view class="input-item" v-if="referrerId">
        <text class="label">绑定推荐人</text>
        <view class="chip">
          <text>ID: {{ referrerId }} (已锁定)</text>
        </view>
      </view>

      <button class="btn-primary" @click="handleLogin" style="margin-top: 60rpx;">确认登录</button>
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
  const localRefId = uni.getStorageSync('referrerId')
  if (localRefId) {
    referrerId.value = localRefId
  }
})
</script>

<style scoped>
.header { margin-bottom: 80rpx; padding-top: 40rpx; }
.form { display: flex; flex-direction: column; }
.input-item { margin-bottom: 48rpx; }
.label { font-size: 26rpx; font-weight: 600; color: var(--text-secondary); margin-bottom: 16rpx; display: block; text-transform: uppercase; letter-spacing: 1rpx; }
.input { border-bottom: 1px solid var(--border-light); padding: 20rpx 0; font-size: 32rpx; transition: border-color 0.3s; }
.input:focus { border-color: var(--accent-color); }
.chip { display: inline-flex; background: rgba(255, 107, 74, 0.08); color: var(--accent-color); padding: 12rpx 24rpx; border-radius: var(--radius-sm); font-size: 26rpx; font-weight: 500; font-variant-numeric: tabular-nums; }
</style>
