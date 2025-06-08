<template>
  <div class="home-page">
    <header class="header">
      <h1>巡检系统</h1>
      <div class="user-info">
        <span>{{ username }}</span>
        <button @click="handleLogout">退出</button>
      </div>
    </header>
    
    <nav class="nav-menu">
      <div class="nav-item" @click="router.push('/scan')">
        <i class="icon-scan"></i>
        <span>扫码巡检</span>
      </div>
      <div class="nav-item" @click="router.push('/records')">
        <i class="icon-records"></i>
        <span>巡检记录</span>
      </div>
      <div class="nav-item" @click="router.push('/profile')">
        <i class="icon-profile"></i>
        <span>个人信息</span>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('管理员')

const handleLogout = () => {
  router.push('/login')
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: #f7f7f7;
}

.header {
  background: #4CAF50;
  color: white;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-info button {
  background: transparent;
  border: 1px solid white;
  color: white;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
}

.nav-menu {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  padding: 20px;
}

.nav-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.nav-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.icon-scan, .icon-records, .icon-profile {
  font-size: 24px;
  margin-bottom: 10px;
  color: #4CAF50;
}
</style> 