<template>
  <div class="profile-container">
    <div class="profile-header">
      <h1>个人中心</h1>
    </div>
    <div class="profile-content">
      <div class="profile-info">
        <div class="avatar">
          <img src="@/assets/images/avatar.png" alt="avatar" />
        </div>
        <div class="info">
          <div class="name">{{ userInfo?.realName || '未设置' }}</div>
          <div class="role">{{ userInfo?.role === 'admin' ? '管理员' : '巡检员' }}</div>
        </div>
      </div>
      <div class="profile-menu">
        <div class="menu-item" @click="router.push('/settings')">
          <span class="material-icons">settings</span>
          <span>设置</span>
        </div>
        <div class="menu-item" @click="logout">
          <span class="material-icons">logout</span>
          <span>退出登录</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const logout = async () => {
  try { 
    await request.post('/api/v1/logout') 
  } catch(e) {}
  localStorage.removeItem('token')
  localStorage.removeItem('loginInfo')
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/profile.css');
</style> 