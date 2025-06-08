<template>
  <div class="profile-container">
    <div class="profile-topbar">个人中心</div>
    <div class="profile-content-wrapper">
      <div class="profile-card">
        <div class="profile-avatar">张</div>
        <div class="profile-info-block">
          <div class="profile-name">{{ userInfo.realName }}</div>
          <div class="profile-meta">工号：10086</div>
          <div class="profile-meta">部门：{{ userInfo.department }}</div>
        </div>
      </div>
      <div class="menu-list">
        <div class="menu-item">
          <span class="menu-icon"><span class="material-icons">person</span></span>
          <span class="menu-label">个人信息</span>
          <span class="material-icons">chevron_right</span>
        </div>
        <div class="menu-item">
          <span class="menu-icon"><span class="material-icons">notifications</span></span>
          <span class="menu-label">消息通知</span>
          <span class="material-icons">chevron_right</span>
        </div>
        <div class="menu-item">
          <span class="menu-icon"><span class="material-icons">info</span></span>
          <span class="menu-label">关于</span>
          <span class="material-icons">chevron_right</span>
        </div>
      </div>
      <div class="logout-button highlight" @click="handleLogout">退出登录</div>
    </div>
    <div style="height:40px;"></div>
    <div class="bottom-nav">
      <a :class="['nav-item', $route.path === '/scan' ? 'active' : '']" @click.prevent="router.push('/scan')">
        <span style="font-size: 22px;">📷</span>
        <span>巡检</span>
      </a>
      <a :class="['nav-item', $route.path === '/records' ? 'active' : '']" @click.prevent="router.push('/records')">
        <span style="font-size: 22px;">📜</span>
        <span>记录</span>
      </a>
      <a :class="['nav-item', $route.path === '/profile' ? 'active' : '']" @click.prevent="router.push('/profile')">
        <span style="font-size: 22px;">👤</span>
        <span>我的</span>
      </a>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const userInfo = ref({
  realName: '',
  phone: '',
  email: '',
  department: '',
  role: ''
})

async function fetchUserInfo() {
  try {
    const res = await request.get('/api/v1/users/current')
    if (res.data?.code === 0) {
      userInfo.value = res.data.data
    }
  } catch (err) {
    console.error('获取用户信息失败:', err)
  }
}

async function handleLogout() {
  try {
    await request.post('/api/v1/auth/logout')
    userStore.clearToken()
    router.push('/login')
  } catch (err) {
    console.error('退出登录失败:', err)
  }
}

onMounted(async () => {
  await fetchUserInfo()
})
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/profile.css');
</style> 