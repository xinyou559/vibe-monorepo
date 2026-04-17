<template>
  <div class="layout">
    <aside class="sidebar">
      <h2>美业后台管理</h2>
      <nav>
        <router-link to="/services">服务管理</router-link>
        <router-link to="/orders">订单管理</router-link>
        <router-link to="/users">用户与分销</router-link>
      </nav>
    </aside>
    <main class="main-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 200px;
  background-color: #2c3e50;
  color: white;
  padding: 20px 0;
}

.sidebar h2 {
  text-align: center;
  margin-bottom: 30px;
  font-size: 1.2rem;
}

nav {
  display: flex;
  flex-direction: column;
}

nav a {
  padding: 15px 20px;
  color: #a0aec0;
  text-decoration: none;
  transition: all 0.3s;
}

nav a:hover, nav a.router-link-active {
  background-color: #34495e;
  color: white;
}

.main-content {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
}
</style>