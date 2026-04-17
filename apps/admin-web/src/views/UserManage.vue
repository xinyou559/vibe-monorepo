<template>
  <div class="page-view">
    <div class="page-header">
      <h3>用户与分销资产</h3>
      <button class="action-btn" @click="fetchData">
        ⟳ 刷新数据
      </button>
    </div>

    <div class="split-view">
      <!-- 用户资产列表 -->
      <div class="panel">
        <div class="panel-header">
          <h4>用户账户资产</h4>
          <span class="badge">{{ users.length }} 个用户</span>
        </div>
        <div class="data-table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th width="80">UID</th>
                <th>手机号 / OpenID</th>
                <th width="120">当前余额</th>
                <th width="100">推荐人</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in users" :key="item.id">
                <td class="cell-id">#{{ String(item.id).padStart(4, '0') }}</td>
                <td>
                  <div class="user-identity">
                    <span class="phone">{{ item.phone || '未绑定手机' }}</span>
                    <span class="openid" :title="item.openid">{{ truncateOpenId(item.openid) }}</span>
                  </div>
                </td>
                <td class="cell-price">¥{{ Number(item.balance).toFixed(2) }}</td>
                <td class="cell-ref">
                  <span v-if="item.referrerId" class="has-ref">#{{ String(item.referrerId).padStart(4, '0') }}</span>
                  <span v-else class="no-ref">-</span>
                </td>
              </tr>
              <tr v-if="users.length === 0">
                <td colspan="4" class="empty-state">
                  <div class="empty-content">
                    <span class="empty-icon">❖</span>
                    <p>暂无用户数据</p>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 全局资金流水 -->
      <div class="panel">
        <div class="panel-header">
          <h4>全局资金流水</h4>
          <span class="badge">{{ transactions.length }} 笔记录</span>
        </div>
        <div class="data-table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th width="80">UID</th>
                <th width="140">发生时间</th>
                <th width="100">变动类型</th>
                <th width="120">发生额</th>
                <th>关联单号</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in transactions" :key="item.id">
                <td class="cell-id">#{{ String(item.userId).padStart(4, '0') }}</td>
                <td class="cell-time">{{ new Date(item.createdAt).toLocaleString() }}</td>
                <td>
                  <span class="tx-type">{{ formatType(item.type) }}</span>
                </td>
                <td class="cell-price">
                  <span :class="item.amount > 0 ? 'plus' : 'minus'">
                    {{ item.amount > 0 ? '+' : '' }}{{ Number(item.amount).toFixed(2) }}
                  </span>
                </td>
                <td class="cell-ref">{{ item.relatedId ? `#${String(item.relatedId).padStart(6, '0')}` : '-' }}</td>
              </tr>
              <tr v-if="transactions.length === 0">
                <td colspan="5" class="empty-state">
                  <div class="empty-content">
                    <span class="empty-icon">≈</span>
                    <p>暂无资金流水</p>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
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

const truncateOpenId = (str: string) => {
  if (!str) return ''
  if (str.length <= 12) return str
  return `${str.slice(0, 6)}...${str.slice(-4)}`
}

const formatType = (type: string) => {
  const map: Record<string, string> = {
    RECHARGE: '账户充值',
    CONSUME: '服务消费',
    COMMISSION: '分销提成'
  }
  return map[type] || type
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.split-view {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
  align-items: start;
}

.panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.panel-header {
  display: flex;
  align-items: center;
  gap: 12px;
  
  h4 {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
  }
  
  .badge {
    background: var(--bg-surface);
    border: 1px solid var(--border-light);
    padding: 2px 8px;
    border-radius: 12px;
    font-size: 11px;
    color: var(--text-tertiary);
    font-weight: 500;
  }
}

.data-table-wrapper {
  background: var(--bg-surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.cell-id {
  font-family: monospace;
  font-weight: 500;
}

.user-identity {
  display: flex;
  flex-direction: column;
  gap: 4px;
  
  .phone {
    font-weight: 500;
    color: var(--text-primary);
  }
  
  .openid {
    font-size: 12px;
    color: var(--text-tertiary);
    font-family: monospace;
  }
}

.cell-price {
  font-variant-numeric: tabular-nums;
  font-weight: 500;
  
  .plus { color: #00C853; }
  .minus { color: var(--text-primary); }
}

.cell-ref {
  color: var(--text-tertiary);
  font-family: monospace;
  font-size: 13px;
  
  .has-ref {
    color: var(--accent-color);
    background: rgba(0,0,0,0.04);
    padding: 2px 6px;
    border-radius: 4px;
  }
}

.cell-time {
  color: var(--text-tertiary);
  font-size: 13px;
}

.tx-type {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
}

.empty-state {
  padding: 80px 0;
  
  .empty-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: var(--text-tertiary);
    
    .empty-icon {
      font-size: 32px;
      margin-bottom: 16px;
      opacity: 0.5;
    }
    
    p {
      margin: 0;
      font-size: 14px;
    }
  }
}

/* 响应式：屏幕较小时转为上下堆叠 */
@media (max-width: 1200px) {
  .split-view {
    grid-template-columns: 1fr;
  }
}
</style>