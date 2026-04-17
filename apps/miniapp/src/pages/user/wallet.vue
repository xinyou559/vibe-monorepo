<template>
  <view class="container">
    <view class="wallet-card">
      <text class="title">当前余额(元)</text>
      <text class="balance">{{ userBalance }}</text>
      <button class="recharge-btn" @click="handleRecharge">去充值</button>
    </view>

    <view class="section-title">资金明细</view>
    <view class="tx-list">
      <view class="tx-item" v-for="item in transactions" :key="item.id">
        <view class="left">
          <text class="type">{{ formatType(item.type) }}</text>
          <text class="time">{{ new Date(item.createdAt).toLocaleString() }}</text>
        </view>
        <view :class="['right', item.amount > 0 ? 'plus' : 'minus']">
          {{ item.amount > 0 ? '+' : '' }}{{ item.amount }}
        </view>
      </view>
      <view class="empty" v-if="transactions.length === 0">
        <text>暂无资金流水</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { request } from '../../utils/request'
import type { Transaction, User } from 'shared-types'

const userBalance = ref('0.00')
const transactions = ref<Transaction[]>([])
const userId = ref<number>(0)

const fetchWalletData = async () => {
  if (!userId.value) return
  
  try {
    // 1. 获取最新余额
    const user = await request<User>(`/users/${userId.value}`, 'GET')
    userBalance.value = user.balance.toString()
    uni.setStorageSync('user', user)

    // 2. 获取流水
    const txs = await request<Transaction[]>(`/users/${userId.value}/transactions`, 'GET')
    transactions.value = txs
  } catch (error) {
    console.error('获取钱包数据失败', error)
  }
}

const handleRecharge = () => {
  uni.showModal({
    title: '充值(MVP模拟)',
    editable: true,
    placeholderText: '请输入充值金额(元)',
    success: async (res) => {
      if (res.confirm && res.content) {
        const amount = Number(res.content)
        if (isNaN(amount) || amount <= 0) {
          uni.showToast({ title: '金额无效', icon: 'none' })
          return
        }

        try {
          await request('/recharge', 'POST', {
            userId: userId.value,
            amount: amount
          })
          uni.showToast({ title: '充值成功', icon: 'success' })
          fetchWalletData()
        } catch (e) {}
      }
    }
  })
}

const formatType = (type: string) => {
  const map: Record<string, string> = {
    RECHARGE: '账户充值',
    CONSUME: '服务消费',
    COMMISSION: '分享提成收入'
  }
  return map[type] || type
}

onShow(() => {
  const user = uni.getStorageSync('user')
  if (user && user.id) {
    userId.value = user.id
    fetchWalletData()
  } else {
    uni.showToast({ title: '请先登录', icon: 'none' })
    setTimeout(() => { uni.navigateBack() }, 1000)
  }
})
</script>

<style>
.container { padding: 20rpx; background: #f5f7fa; min-height: 100vh; }
.wallet-card { background: linear-gradient(135deg, #e43d33 0%, #ff7a66 100%); border-radius: 16rpx; padding: 50rpx; color: #fff; text-align: center; box-shadow: 0 4rpx 12rpx rgba(228,61,51,0.3); }
.title { font-size: 28rpx; opacity: 0.9; display: block; margin-bottom: 20rpx; }
.balance { font-size: 72rpx; font-weight: bold; display: block; margin-bottom: 40rpx; }
.recharge-btn { background: #fff; color: #e43d33; font-weight: bold; border-radius: 40rpx; font-size: 28rpx; width: 240rpx; height: 80rpx; line-height: 80rpx; margin: 0 auto; }
.section-title { font-size: 32rpx; font-weight: bold; color: #333; margin: 40rpx 0 20rpx; padding-left: 10rpx; }
.tx-list { background: #fff; border-radius: 16rpx; overflow: hidden; }
.tx-item { padding: 30rpx; border-bottom: 1px solid #f5f5f5; display: flex; justify-content: space-between; align-items: center; }
.left { display: flex; flex-direction: column; }
.type { font-size: 30rpx; color: #333; margin-bottom: 8rpx; }
.time { font-size: 24rpx; color: #999; }
.right { font-size: 36rpx; font-weight: bold; }
.plus { color: #67c23a; }
.minus { color: #f56c6c; }
.empty { text-align: center; padding: 50rpx; color: #999; font-size: 28rpx; }
</style>