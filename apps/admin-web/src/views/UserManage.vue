<template>
  <div class="page-container">
    <div class="header">
      <h2>用户与分销管理</h2>
      <button @click="fetchData">刷新</button>
    </div>

    <div class="table-container">
      <h3>用户列表</h3>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>OpenID</th>
            <th>手机号</th>
            <th>当前余额</th>
            <th>推荐人ID</th>
            <th>注册时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in users" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.openid }}</td>
            <td>{{ item.phone || '-' }}</td>
            <td>¥{{ item.balance }}</td>
            <td>{{ item.referrerId || '无' }}</td>
            <td>{{ new Date(item.createdAt).toLocaleString() }}</td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="6" class="empty">暂无用户</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="table-container" style="margin-top: 40px;">
      <h3>全站资金流水 (充值/消费/提成)</h3>
      <table>
        <thead>
          <tr>
            <th>流水号</th>
            <th>用户ID</th>
            <th>变动金额</th>
            <th>类型</th>
            <th>关联单号</th>
            <th>发生时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in transactions" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.userId }}</td>
            <td :class="{ positive: item.amount > 0, negative: item.amount < 0 }">
              {{ item.amount > 0 ? '+' : '' }}{{ item.amount }}
            </td>
            <td>{{ formatType(item.type) }}</td>
            <td>{{ item.relatedId || '-' }}</td>
            <td>{{ new Date(item.createdAt).toLocaleString() }}</td>
          </tr>
          <tr v-if="transactions.length === 0">
            <td colspan="6" class="empty">暂无流水</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { adminApi } from 'api-client'
import type { User, Transaction } from 'shared-types'

const users = ref<User[]>([])
const transactions = ref<Transaction[]>([])

const fetchData = async () => {
  try {
    const [u, t] = await Promise.all([
      adminApi.getUsers(),
      adminApi.getTransactions()
    ])
    users.value = u
    transactions.value = t
  } catch (error) {
    console.error('获取用户数据失败:', error)
  }
}

const formatType = (type: string) => {
  const map: Record<string, string> = {
    RECHARGE: '用户充值',
    CONSUME: '服务消费',
    COMMISSION: '分销提成'
  }
  return map[type] || type
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.page-container { padding: 20px; background: #fff; border-radius: 8px; }
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; }
.positive { color: #67c23a; font-weight: bold; }
.negative { color: #f56c6c; font-weight: bold; }
.empty { text-align: center; color: #909399; }
h3 { margin-bottom: 10px; color: #303133; font-size: 16px; }
</style>