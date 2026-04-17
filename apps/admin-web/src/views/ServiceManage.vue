<template>
  <div class="page-view">
    <div class="page-header">
      <h3>服务项目库</h3>
      <button class="action-btn primary" @click="showCreateModal = true">
        + 新增服务
      </button>
    </div>

    <div class="data-table-wrapper">
      <table class="data-table">
        <thead>
          <tr>
            <th width="80">ID</th>
            <th width="240">服务名称</th>
            <th width="120">单价</th>
            <th width="120">分销提成</th>
            <th width="120">状态</th>
            <th width="180">上架时间</th>
            <th width="100">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in services" :key="item.id">
            <td class="cell-id">#{{ String(item.id).padStart(4, '0') }}</td>
            <td class="cell-name">
              <span class="name">{{ item.name }}</span>
            </td>
            <td class="cell-price">¥{{ Number(item.price).toFixed(2) }}</td>
            <td class="cell-rate">{{ Number(item.commissionRate) * 100 }}%</td>
            <td>
              <span :class="['status-dot', item.status.toLowerCase()]"></span>
              <span class="status-text">{{ item.status === 'ACTIVE' ? '营业中' : '已下架' }}</span>
            </td>
            <td class="cell-time">{{ new Date(item.createdAt).toLocaleDateString() }}</td>
            <td>
              <button class="action-btn" @click="editService(item)">配置</button>
            </td>
          </tr>
          <tr v-if="services.length === 0">
            <td colspan="7" class="empty-state">
              <div class="empty-content">
                <span class="empty-icon">✧</span>
                <p>暂无服务项目</p>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 侧边抽屉弹窗 (替代居中 Modal，更具应用感) -->
    <transition name="drawer">
      <div class="drawer-overlay" v-if="showCreateModal" @click.self="closeModal">
        <div class="drawer-content">
          <div class="drawer-header">
            <h4>{{ currentForm.id ? '配置服务项目' : '新建服务项目' }}</h4>
            <button class="close-btn" @click="closeModal">×</button>
          </div>
          
          <div class="drawer-body">
            <div class="form-item">
              <label>服务名称</label>
              <input v-model="currentForm.name" type="text" placeholder="例如：精油开背 60min">
            </div>
            
            <div class="form-row">
              <div class="form-item">
                <label>门市单价 (¥)</label>
                <input v-model.number="currentForm.price" type="number" placeholder="0.00">
              </div>
              <div class="form-item">
                <label>分销提成比例</label>
                <input v-model.number="currentForm.commissionRate" type="number" step="0.01" placeholder="例如：0.10">
                <span class="hint">0.10 即表示新客消费后，推荐人可得 10% 提成</span>
              </div>
            </div>

            <div class="form-item">
              <label>上架状态</label>
              <div class="radio-group">
                <label class="radio-label">
                  <input type="radio" v-model="currentForm.status" value="ACTIVE">
                  <span class="radio-text">营业中</span>
                </label>
                <label class="radio-label">
                  <input type="radio" v-model="currentForm.status" value="INACTIVE">
                  <span class="radio-text">已下架</span>
                </label>
              </div>
            </div>
          </div>
          
          <div class="drawer-footer">
            <button class="action-btn" @click="closeModal">取消</button>
            <button class="action-btn primary" @click="saveService">保存配置</button>
          </div>
        </div>
      </div>
    </transition>
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
  setTimeout(() => {
    currentForm.value = {
      name: '',
      price: 0,
      commissionRate: 0.1,
      status: 'ACTIVE'
    }
  }, 300) // 等待动画结束
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

<style scoped lang="scss">
.data-table-wrapper {
  background: var(--bg-surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.cell-id {
  font-family: monospace;
  color: var(--text-tertiary);
}

.cell-name {
  .name {
    font-weight: 500;
    color: var(--text-primary);
  }
}

.cell-price {
  font-variant-numeric: tabular-nums;
  font-weight: 500;
}

.cell-rate {
  color: var(--text-secondary);
}

.cell-time {
  color: var(--text-tertiary);
  font-size: 13px;
}

.status-dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-right: 8px;
  
  &.active { background-color: #00C853; }
  &.inactive { background-color: var(--text-tertiary); }
}

.status-text {
  font-size: 13px;
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

/* Drawer Component */
.drawer-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  z-index: 100;
  display: flex;
  justify-content: flex-end;
}

.drawer-content {
  width: 480px;
  background: var(--bg-surface);
  height: 100%;
  display: flex;
  flex-direction: column;
  box-shadow: -4px 0 24px rgba(0,0,0,0.1);
}

.drawer-header {
  padding: 24px 32px;
  border-bottom: 1px solid var(--border-light);
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  h4 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
  }
  
  .close-btn {
    background: none;
    border: none;
    font-size: 24px;
    color: var(--text-tertiary);
    cursor: pointer;
    line-height: 1;
    
    &:hover {
      color: var(--text-primary);
    }
  }
}

.drawer-body {
  flex: 1;
  padding: 32px;
  overflow-y: auto;
}

.drawer-footer {
  padding: 24px 32px;
  border-top: 1px solid var(--border-light);
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.form-item {
  margin-bottom: 24px;
  
  label {
    display: block;
    font-size: 13px;
    font-weight: 600;
    color: var(--text-secondary);
    margin-bottom: 8px;
  }
  
  input[type="text"],
  input[type="number"] {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid var(--border-light);
    border-radius: var(--radius-sm);
    font-size: 14px;
    outline: none;
    box-sizing: border-box;
    transition: border-color 0.2s;
    
    &:focus {
      border-color: var(--text-primary);
    }
  }
  
  .hint {
    display: block;
    margin-top: 6px;
    font-size: 12px;
    color: var(--text-tertiary);
  }
}

.form-row {
  display: flex;
  gap: 16px;
  
  .form-item {
    flex: 1;
  }
}

.radio-group {
  display: flex;
  gap: 24px;
  margin-top: 12px;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  
  .radio-text {
    font-size: 14px;
    color: var(--text-primary);
  }
}

/* Drawer Transition */
.drawer-enter-active,
.drawer-leave-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  
  .drawer-content {
    transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  }
}
.drawer-enter-from,
.drawer-leave-to {
  opacity: 0;
  
  .drawer-content {
    transform: translateX(100%);
  }
}
</style>