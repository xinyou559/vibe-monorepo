<template>
  <div class="page-container">
    <div class="header">
      <h2>订单管理</h2>
      <button @click="fetchOrders">刷新</button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>订单号</th>
            <th>用户ID</th>
            <th>服务ID</th>
            <th>支付金额</th>
            <th>状态</th>
            <th>下单时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in orders" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.userId }}</td>
            <td>{{ item.serviceId }}</td>
            <td>¥{{ item.amount }}</td>
            <td>
              <span :class="['status', item.status.toLowerCase()]">
                {{ formatStatus(item.status) }}
              </span>
            </td>
            <td>{{ new Date(item.createdAt).toLocaleString() }}</td>
            <td>
              <button 
                class="btn-text" 
                v-if="item.status === 'PAID'" 
                @click="completeOrder(item.id)">
                核销完成
              </button>
            </td>
          </tr>
          <tr v-if="orders.length === 0">
            <td colspan="7" class="empty">暂无数据</td>
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
    alert('操作成功')
    fetchOrders()
  } catch (error) {
    alert('操作失败')
  }
}

const formatStatus = (status: string) => {
  const map: Record<string, string> = {
    INIT: '待支付',
    PAID: '已支付/待服务',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.page-container { padding: 20px; background: #fff; border-radius: 8px; }
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; }
.status.init { color: #909399; }
.status.paid { color: #e6a23c; }
.status.completed { color: #67c23a; }
.status.cancelled { color: #f56c6c; }
.btn-text { background: none; border: none; color: #409eff; cursor: pointer; }
.empty { text-align: center; color: #909399; }
</style>