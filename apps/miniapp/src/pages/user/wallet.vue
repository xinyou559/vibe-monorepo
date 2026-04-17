<template>
  <view class="page">
    <view class="surface wallet-hero">
      <view class="row">
        <view class="col">
          <text class="title-sm">当前余额</text>
          <text class="balance mono">¥{{ Number(userBalance).toFixed(2) }}</text>
        </view>
        <button class="btn btn-primary" @click="handleRecharge">充值</button>
      </view>
      <view class="hint">充值 / 消费 / 提成收入均会记录到流水</view>
    </view>

    <view class="section">
      <view class="row section-head">
        <text class="section-title">资金流水</text>
        <text class="section-sub">{{ transactions.length }} 条</text>
      </view>
      <view class="surface list">
        <view class="tx-item" v-for="item in transactions" :key="item.id">
          <view class="left">
            <text class="type">{{ formatType(item.type) }}</text>
            <text class="time">{{ new Date(item.createdAt).toLocaleString() }}</text>
          </view>
          <view class="amount mono" :class="item.amount > 0 ? 'plus' : 'minus'">
            {{ item.amount > 0 ? '+' : '' }}{{ Number(item.amount).toFixed(2) }}
          </view>
        </view>
        <view class="empty" v-if="transactions.length === 0">
          <text class="empty-title">暂无资金流水</text>
          <text class="empty-sub">完成一次充值或购买后会显示在这里</text>
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
.wallet-hero {
  padding: 28rpx;
  margin-bottom: 22rpx;
}

.wallet-hero .col {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.balance {
  font-size: 48rpx;
  font-weight: 800;
  color: var(--text);
}

.hint {
  margin-top: 18rpx;
  padding-top: 18rpx;
  border-top: 1px solid var(--hairline);
  font-size: 24rpx;
  color: var(--muted);
}

.section {
  margin-top: 10rpx;
}

.section-head {
  margin: 10rpx 0 16rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: 800;
  color: var(--text);
}

.section-sub {
  font-size: 24rpx;
  color: var(--muted);
}

.list {
  overflow: hidden;
}

.tx-item {
  padding: 22rpx 28rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--hairline);
}

.tx-item:last-child {
  border-bottom: none;
}

.left {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.type {
  font-size: 28rpx;
  font-weight: 700;
  color: var(--text);
}

.time {
  font-size: 22rpx;
  color: var(--muted);
}

.amount {
  font-size: 30rpx;
  font-weight: 800;
}

.plus {
  color: #00c853;
}

.minus {
  color: var(--text);
  opacity: 0.9;
}

.empty {
  padding: 90rpx 32rpx;
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
