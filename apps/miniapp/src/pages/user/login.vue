<template>
  <view class="container">
    <view class="title">登录/注册</view>
    <view class="form">
      <view class="input-item">
        <text class="label">模拟 OpenID</text>
        <input class="input" v-model="openid" placeholder="输入任意唯一字符串如 user123" />
      </view>
      <view class="input-item">
        <text class="label">手机号(选填)</text>
        <input class="input" v-model="phone" placeholder="输入手机号" />
      </view>
      <view class="input-item" v-if="referrerId">
        <text class="label">绑定推荐人</text>
        <text class="input disabled">ID: {{ referrerId }} (不可修改)</text>
      </view>

      <button class="login-btn" @click="handleLogin">确认登录</button>
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
.container { padding: 40rpx; background: #fff; min-height: 100vh; }
.title { font-size: 48rpx; font-weight: bold; margin-bottom: 60rpx; color: #333; }
.form { display: flex; flex-direction: column; }
.input-item { margin-bottom: 40rpx; }
.label { font-size: 28rpx; color: #666; margin-bottom: 10rpx; display: block; }
.input { border-bottom: 1px solid #eee; padding: 20rpx 0; font-size: 32rpx; }
.disabled { color: #999; }
.login-btn { background: #409eff; color: #fff; border-radius: 8rpx; margin-top: 40rpx; height: 88rpx; line-height: 88rpx; font-size: 32rpx; width: 100%; }
</style>