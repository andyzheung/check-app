<template>
  <div class="profile-container">
    <div class="profile-topbar">个人中心</div>
    <div class="profile-content-wrapper">
      <div class="profile-card">
        <div class="profile-avatar">{{ userInfo.realName?.[0] || '?' }}</div>
        <div class="profile-info-block">
          <div class="profile-name">{{ userInfo.realName }}</div>
          <div class="profile-meta">工号：{{ userInfo.username }}</div>
          <div class="profile-meta">部门：{{ userInfo.department }}</div>
        </div>
      </div>
      <div class="menu-list">
        <div class="menu-item" @click="goToPersonalInfo">
          <div class="menu-item-left">
            <span class="material-icons">person</span>
            <span>个人信息</span>
          </div>
          <span class="material-icons">chevron_right</span>
        </div>
        <div class="menu-item" @click="goToNotifications">
          <div class="menu-item-left">
            <span class="material-icons">notifications</span>
            <span>消息通知</span>
            <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
          </div>
          <span class="material-icons">chevron_right</span>
        </div>
        <div class="menu-item disabled">
          <div class="menu-item-left">
            <span class="material-icons">info</span>
            <span>关于</span>
          </div>
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

const router = useRouter()
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
function goToPersonalInfo() {
  router.push('/personal-info')
}

function goToNotifications() {
  router.push('/notifications')
}

// 退出登录
async function handleLogout() {
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

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/profile.css');
.menu-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style> 