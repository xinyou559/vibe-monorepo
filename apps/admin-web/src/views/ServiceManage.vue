<template>
  <div class="page-container">
    <div class="header">
      <h2>服务管理</h2>
      <button @click="showCreateModal = true">添加服务</button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>服务名称</th>
            <th>价格</th>
            <th>提成比例</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in services" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td>¥{{ item.price }}</td>
            <td>{{ item.commissionRate * 100 }}%</td>
            <td>
              <span :class="['status', item.status.toLowerCase()]">
                {{ item.status === 'ACTIVE' ? '上架中' : '已下架' }}
              </span>
            </td>
            <td>{{ new Date(item.createdAt).toLocaleString() }}</td>
            <td>
              <button class="btn-text" @click="editService(item)">编辑</button>
            </td>
          </tr>
          <tr v-if="services.length === 0">
            <td colspan="7" class="empty">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑弹窗 -->
    <div class="modal" v-if="showCreateModal">
      <div class="modal-content">
        <h3>{{ currentForm.id ? '编辑服务' : '添加服务' }}</h3>
        <div class="form-group">
          <label>服务名称</label>
          <input v-model="currentForm.name" type="text" placeholder="如：精油开背">
        </div>
        <div class="form-group">
          <label>服务价格</label>
          <input v-model.number="currentForm.price" type="number" placeholder="金额（元）">
        </div>
        <div class="form-group">
          <label>提成比例</label>
          <input v-model.number="currentForm.commissionRate" type="number" step="0.01" placeholder="如：0.1 表示 10%">
        </div>
        <div class="form-group">
          <label>状态</label>
          <select v-model="currentForm.status">
            <option value="ACTIVE">上架中</option>
            <option value="INACTIVE">已下架</option>
          </select>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="closeModal">取消</button>
          <button class="btn-confirm" @click="saveService">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { adminApi } from 'api-client'
import type { ServiceItem } from 'shared-types'

const services = ref<ServiceItem[]>([])
const showCreateModal = ref(false)
const currentForm = ref<Partial<ServiceItem>>({
  name: '',
  price: 0,
  commissionRate: 0.1,
  status: 'ACTIVE'
})

const fetchServices = async () => {
  try {
    services.value = await adminApi.getServices()
  } catch (error) {
    console.error('获取服务列表失败:', error)
  }
}

const editService = (item: ServiceItem) => {
  currentForm.value = { ...item }
  showCreateModal.value = true
}

const closeModal = () => {
  showCreateModal.value = false
  currentForm.value = {
    name: '',
    price: 0,
    commissionRate: 0.1,
    status: 'ACTIVE'
  }
}

const saveService = async () => {
  try {
    if (currentForm.value.id) {
      await adminApi.updateService(currentForm.value.id, currentForm.value)
    } else {
      await adminApi.createService(currentForm.value)
    }
    closeModal()
    fetchServices()
  } catch (error) {
    alert('保存失败')
  }
}

onMounted(() => {
  fetchServices()
})
</script>

<style scoped>
/* 这里暂放些简易样式 */
.page-container { padding: 20px; background: #fff; border-radius: 8px; }
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; }
.status.active { color: #67c23a; }
.status.inactive { color: #f56c6c; }
.btn-text { background: none; border: none; color: #409eff; cursor: pointer; }
.modal { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; }
.modal-content { background: white; padding: 24px; border-radius: 8px; width: 400px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; color: #606266; }
.form-group input, .form-group select { width: 100%; padding: 8px; border: 1px solid #dcdfe6; border-radius: 4px; }
.modal-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
.btn-confirm { background: #409eff; color: white; border: none; padding: 8px 16px; border-radius: 4px; cursor: pointer; }
.btn-cancel { background: #fff; border: 1px solid #dcdfe6; padding: 8px 16px; border-radius: 4px; cursor: pointer; }
</style>