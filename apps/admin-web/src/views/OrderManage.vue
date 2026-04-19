<template>
  <div class="page-view">
    <div class="page-header">
      <h3>订单核销</h3>
      <button class="action-btn" @click="fetchOrders">
        ⟳ 刷新数据
      </button>
    </div>

    <div class="data-table-wrapper">
      <table class="data-table">
        <thead>
          <tr>
            <th width="100">订单号</th>
            <th width="100">用户 ID</th>
            <th width="100">服务 ID</th>
            <th width="140">实付金额</th>
            <th width="140">订单状态</th>
            <th width="180">下单时间</th>
            <th width="120">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in orders" :key="item.id">
            <td class="cell-id">#{{ String(item.id).padStart(6, '0') }}</td>
            <td class="cell-ref">{{ item.userId }}</td>
            <td class="cell-ref">{{ item.serviceId }}</td>
            <td class="cell-price">¥{{ Number(item.amount).toFixed(2) }}</td>
            <td>
              <div class="status-badge" :class="item.status.toLowerCase()">
                <span class="dot"></span>
                {{ formatStatus(item.status) }}
              </div>
            </td>
            <td class="cell-time">{{ new Date(item.createdAt).toLocaleString() }}</td>
            <td>
              <button 
                class="action-btn primary" 
                v-if="item.status === 'PAID'" 
                @click="completeOrder(item.id)">
                核销完成
              </button>
              <span v-else class="action-placeholder">-</span>
            </td>
          </tr>
          <tr v-if="orders.length === 0">
            <td colspan="7" class="empty-state">
              <div class="empty-content">
                <span class="empty-icon">○</span>
                <p>暂无订单记录</p>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { adminApi } from 'api-client'
import type { Order } from 'shared-types'

const orders = ref<Order[]>([])

const fetchOrders = async () => {
  try {
    orders.value = await adminApi.getOrders()
  } catch (error) {
    console.error('获取订单列表失败:', error)
  }
}

const completeOrder = async (id: number) => {
  if (!confirm('确认该服务已核销完成？')) return
  try {
    await adminApi.completeOrder(id)
    fetchOrders()
  } catch (error) {
    alert('操作失败')
  }
}

const formatStatus = (status: string) => {
  const map: Record<string, string> = {
    INIT: '待支付',
    PAID: '待核销',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped lang="scss">
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

.cell-ref {
  color: var(--text-secondary);
}

.cell-price {
  font-variant-numeric: tabular-nums;
  font-weight: 500;
}

.cell-time {
  color: var(--text-tertiary);
  font-size: 13px;
}

.action-placeholder {
  color: var(--text-tertiary);
  font-size: 14px;
  padding-left: 12px;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  
  .dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
  }
  
  &.init {
    background: #F5F5F5;
    color: var(--text-secondary);
    .dot { background: var(--text-tertiary); }
  }
  
  &.paid {
    background: rgba(255, 107, 74, 0.08);
    color: var(--accent-color);
    .dot { 
      background: var(--accent-color); 
      box-shadow: 0 0 6px rgba(255, 107, 74, 0.4);
    }
  }
  
  &.completed {
    background: #F5F5F5;
    color: var(--text-primary);
    .dot { background: var(--text-primary); }
  }
  
  &.cancelled {
    background: rgba(245, 108, 108, 0.1);
    color: #f56c6c;
    .dot { background: #f56c6c; }
  }
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
</style>