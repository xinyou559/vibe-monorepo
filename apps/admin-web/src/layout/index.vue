<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-logo">V</div>
        <h1 class="brand-title">Vibe Admin</h1>
      </div>
      
      <nav class="nav-menu">
        <div class="nav-section">
          <p class="nav-label">业务管理</p>
          <router-link to="/services" class="nav-item">
            <span class="icon">✧</span>
            <span class="text">服务配置</span>
          </router-link>
          <router-link to="/orders" class="nav-item">
            <span class="icon">○</span>
            <span class="text">订单核销</span>
          </router-link>
        </div>

        <div class="nav-section">
          <p class="nav-label">资产与用户</p>
          <router-link to="/users" class="nav-item">
            <span class="icon">❖</span>
            <span class="text">分销与流水</span>
          </router-link>
        </div>
      </nav>
      
      <div class="sidebar-footer">
        <div class="user-profile">
          <div class="avatar">Admin</div>
          <div class="info">
            <p class="name">超级管理员</p>
            <p class="role">System</p>
          </div>
        </div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-workspace">
      <header class="top-header">
        <h2 class="page-title">{{ currentRouteName }}</h2>
        <div class="header-actions">
          <div class="status-badge">
            <span class="dot"></span>
            System Online
          </div>
        </div>
      </header>
      
      <div class="content-scroll-area">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const currentRouteName = computed(() => route.meta.title || 'Overview')
</script>

<style lang="scss">
:root {
  --bg-canvas: #F7F7F9;
  --bg-surface: #FFFFFF;
  --bg-sidebar: #111111;
  --bg-sidebar-hover: #222222;
  
  --text-primary: #111111;
  --text-secondary: #666666;
  --text-tertiary: #999999;
  --text-inverse: #FFFFFF;
  --text-inverse-muted: #888888;
  
  --border-light: #EAEAEA;
  --border-dark: #333333;
  
  --accent-color: #000000;
  --accent-hover: #333333;
  
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
  
  --shadow-sm: 0 1px 2px rgba(0,0,0,0.04);
  --shadow-md: 0 4px 12px rgba(0,0,0,0.05);
}

body {
  background-color: var(--bg-canvas);
  color: var(--text-primary);
  font-family: -apple-system, BlinkMacSystemFont, "SF Pro Text", "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  margin: 0;
}

/* 全局表格样式重置，摒弃传统卡片感，采用清爽的线框风格 */
.data-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  text-align: left;
  
  th {
    padding: 16px 20px;
    font-size: 12px;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    color: var(--text-tertiary);
    border-bottom: 1px solid var(--border-light);
    background: transparent;
  }
  
  td {
    padding: 16px 20px;
    font-size: 14px;
    color: var(--text-primary);
    border-bottom: 1px solid var(--border-light);
    vertical-align: middle;
  }
  
  tr:last-child td {
    border-bottom: none;
  }
  
  tr:hover td {
    background-color: #FAFAFA;
  }
}

.action-btn {
  background: transparent;
  border: 1px solid var(--border-light);
  padding: 6px 12px;
  font-size: 12px;
  font-weight: 500;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s ease;
  color: var(--text-primary);
  
  &:hover {
    border-color: var(--text-primary);
    background: var(--bg-surface);
  }
  
  &.primary {
    background: var(--accent-color);
    color: var(--text-inverse);
    border-color: var(--accent-color);
    
    &:hover {
      background: var(--accent-hover);
    }
  }
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 32px;
  
  h3 {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    letter-spacing: -0.5px;
  }
}
</style>

<style scoped lang="scss">
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  width: 240px;
  background-color: var(--bg-sidebar);
  color: var(--text-inverse);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  border-right: 1px solid var(--border-dark);
}

.brand {
  padding: 32px 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  
  .brand-logo {
    width: 32px;
    height: 32px;
    background: var(--text-inverse);
    color: var(--bg-sidebar);
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
    font-size: 18px;
    border-radius: var(--radius-sm);
  }
  
  .brand-title {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
    letter-spacing: -0.5px;
  }
}

.nav-menu {
  flex: 1;
  padding: 0 16px;
  overflow-y: auto;
}

.nav-section {
  margin-bottom: 32px;
  
  .nav-label {
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 1px;
    color: var(--text-inverse-muted);
    margin: 0 0 12px 12px;
    font-weight: 600;
  }
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  color: var(--text-inverse-muted);
  text-decoration: none;
  border-radius: var(--radius-sm);
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
  
  .icon {
    font-size: 16px;
    opacity: 0.7;
  }
  
  &:hover {
    color: var(--text-inverse);
    background: var(--bg-sidebar-hover);
  }
  
  &.router-link-active {
    color: var(--text-inverse);
    background: var(--bg-sidebar-hover);
    
    .icon {
      opacity: 1;
    }
  }
}

.sidebar-footer {
  padding: 24px;
  border-top: 1px solid var(--border-dark);
  
  .user-profile {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .avatar {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      background: #333;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 10px;
      font-weight: 600;
    }
    
    .info {
      .name {
        margin: 0;
        font-size: 13px;
        font-weight: 500;
        color: var(--text-inverse);
      }
      .role {
        margin: 2px 0 0;
        font-size: 11px;
        color: var(--text-inverse-muted);
      }
    }
  }
}

.main-workspace {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.top-header {
  height: 64px;
  padding: 0 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--bg-surface);
  border-bottom: 1px solid var(--border-light);
  flex-shrink: 0;
  
  .page-title {
    margin: 0;
    font-size: 14px;
    font-weight: 600;
    color: var(--text-secondary);
  }
  
  .status-badge {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    color: var(--text-secondary);
    
    .dot {
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background: #00C853;
    }
  }
}

.content-scroll-area {
  flex: 1;
  overflow-y: auto;
  padding: 40px;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}
.fade-enter-from {
  opacity: 0;
  transform: translateY(4px);
}
.fade-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}
</style>