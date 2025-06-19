<template>
  <div class="profile-container">
    <div class="user-card">
      <div class="avatar">{{ userInfo.realName?.[0] || 'A' }}</div>
      <div class="username">{{ userInfo.realName || 'Administrator' }}</div>
      <div class="details">
        <span>工号: {{ userInfo.username || 'admin' }}</span>
        <span>部门: {{ userInfo.department }}</span>
      </div>
    </div>

    <div class="menu-list">
      <div class="menu-item" @click="goTo('/personal-info')">
        <span class="material-icons">person_outline</span>
        <span>个人信息</span>
        <span class="material-icons arrow">chevron_right</span>
      </div>
      <div class="menu-item" @click="goTo('/notifications')">
        <span class="material-icons">notifications_none</span>
        <span>消息通知</span>
        <span class="badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
        <span class="material-icons arrow">chevron_right</span>
      </div>
      <div class="menu-item" @click="goTo('/about')">
        <span class="material-icons">info_outline</span>
        <span>关于</span>
        <span class="material-icons arrow">chevron_right</span>
      </div>
    </div>

    <button class="logout-button" @click="logout">退出登录</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()

const userInfo = ref({})
const unreadCount = ref(0)

// 获取用户信息
async function fetchUserInfo() {
  try {
    const res = await request.get('/users/current')
    if (res.code === 200 || res.code === 0) {
      userInfo.value = res.data
    }
  } catch (err) {
    console.error('获取用户信息失败:', err)
  }
}

// 获取未读消息数量
async function fetchUnreadCount() {
  try {
    const res = await request.get('/notifications/unread/count')
    if (res.code === 200 || res.code === 0) {
      unreadCount.value = res.data
    }
  } catch (err) {
    console.error('获取未读消息数量失败:', err)
  }
}

// 页面跳转
function goTo(path) {
  router.push(path)
}

// 退出登录
async function logout() {
  try {
    await request.post('/auth/logout')
    localStorage.removeItem('token')
    router.push('/login')
  } catch (err) {
    console.error('退出登录失败:', err)
  }
}

onMounted(() => {
  fetchUserInfo()
  fetchUnreadCount()
})
</script>

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/profile.css');
</style> 