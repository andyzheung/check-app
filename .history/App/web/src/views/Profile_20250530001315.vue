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
        <div class="menu-item disabled">
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
      <router-link to="/" class="nav-item" active-class="active">
        <span style="font-size: 22px;">🏠</span>
        <span>首页</span>
      </router-link>
      <router-link to="/scan" class="nav-item" active-class="active">
        <span style="font-size: 22px;">📷</span>
        <span>巡检</span>
      </router-link>
      <router-link to="/records" class="nav-item" active-class="active">
        <span style="font-size: 22px;">📜</span>
        <span>记录</span>
      </router-link>
      <router-link to="/profile" class="nav-item" active-class="active">
        <span style="font-size: 22px;">👤</span>
        <span>我的</span>
      </router-link>
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

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/profile.css');
.menu-list {
  margin: 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:not(.disabled):active {
  background: #f5f5f5;
}

.menu-item-left {
  display: flex;
  align-items: center;
}

.menu-item-left .material-icons {
  font-size: 24px;
  color: var(--primary-color, #2196F3);
  width: 32px;
  margin-right: 12px;
}

.menu-item-left span:not(.material-icons) {
  font-size: 16px;
  color: #333;
}

.menu-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.menu-item.disabled .material-icons {
  color: #999;
}

.badge {
  background: #f44336;
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  margin-left: 8px;
  min-width: 18px;
  text-align: center;
}

.menu-item > .material-icons {
  color: #999;
  font-size: 20px;
}
</style> 