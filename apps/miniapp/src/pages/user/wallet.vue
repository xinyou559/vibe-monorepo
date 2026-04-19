<template>
  <view class="page-container">
    <view class="wallet-hero">
      <text class="wallet-label">当前余额</text>
      <view class="balance-wrap">
        <text class="currency">¥</text>
        <text class="balance-num">{{ userBalance }}</text>
      </view>
      <button class="recharge-btn" @click="handleRecharge">充值</button>
    </view>

    <view class="tx-section">
      <view class="tx-header">
        <text class="text-subtitle">资金明细</text>
      </view>
      <view class="tx-list">
        <view class="tx-item" v-for="item in transactions" :key="item.id">
          <view class="tx-left">
            <text class="tx-type">{{ formatType(item.type) }}</text>
            <text class="tx-time">{{ new Date(item.createdAt).toLocaleString() }}</text>
          </view>
          <view :class="['tx-right', item.amount > 0 ? 'text-accent' : 'text-primary']">
            {{ item.amount > 0 ? '+' : '' }}{{ Number(item.amount).toFixed(2) }}
          </view>
        </view>
        <view class="empty" v-if="transactions.length === 0">
          <text class="text-caption">暂无资金流水</text>
        </view>
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
    userBalance.value = Number(user.balance).toFixed(2)
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
    confirmColor: '#FF6B4A',
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

<style scoped>
.wallet-hero { background: var(--bg-midnight); border-radius: var(--radius-lg); padding: 60rpx 40rpx; color: var(--text-inverse); display: flex; flex-direction: column; align-items: center; box-shadow: 0 20rpx 40rpx rgba(26, 27, 38, 0.15); margin-bottom: 48rpx; position: relative; overflow: hidden; }
.wallet-hero::before { content: ''; position: absolute; top: -50%; right: -20%; width: 400rpx; height: 400rpx; background: radial-gradient(circle, rgba(255,107,74,0.15) 0%, transparent 70%); border-radius: 50%; pointer-events: none; }
.wallet-label { font-size: 26rpx; opacity: 0.8; margin-bottom: 16rpx; letter-spacing: 2rpx; text-transform: uppercase; }
.balance-wrap { display: flex; align-items: baseline; gap: 8rpx; margin-bottom: 48rpx; }
.currency { font-size: 40rpx; font-weight: 600; color: var(--accent-color); }
.balance-num { font-size: 88rpx; font-weight: 700; font-variant-numeric: tabular-nums; letter-spacing: -2rpx; line-height: 1; }
.recharge-btn { background: var(--accent-color); color: var(--text-inverse); font-size: 28rpx; font-weight: 600; height: 72rpx; line-height: 72rpx; padding: 0 80rpx; border-radius: 36rpx; border: none; }
.recharge-btn::after { border: none; }
.recharge-btn:active { background: var(--accent-hover); transform: scale(0.96); }

.tx-section { display: flex; flex-direction: column; }
.tx-header { padding: 20rpx 0; margin-bottom: 16rpx; }
.tx-list { background: var(--bg-surface); border-radius: var(--radius-md); border: 1px solid var(--border-light); }
.tx-item { display: flex; justify-content: space-between; align-items: center; padding: 32rpx; border-bottom: 1px solid var(--border-light); }
.tx-item:last-child { border-bottom: none; }
.tx-left { display: flex; flex-direction: column; gap: 8rpx; }
.tx-type { font-size: 30rpx; font-weight: 600; color: var(--text-primary); }
.tx-time { font-size: 24rpx; color: var(--text-tertiary); }
.tx-right { font-size: 32rpx; font-weight: 600; font-variant-numeric: tabular-nums; }
.empty { text-align: center; padding: 80rpx 0; }
</style>
