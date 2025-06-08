<template>
  <div class="home-container">
    <!-- 顶部用户信息 -->
    <div class="user-info">
      <div class="avatar">{{ userInfo.realName?.[0] || '?' }}</div>
      <div class="info">
        <div class="name">{{ userInfo.realName }}</div>
        <div class="department">{{ userInfo.department }}</div>
      </div>
      <div class="notification" @click="goToNotifications">
        <span class="material-icons">notifications</span>
        <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
      </div>
    </div>

    <!-- 任务统计 -->
    <div class="stats-card">
      <div class="stats-item">
        <div class="stats-value">{{ stats.totalTasks || 0 }}</div>
        <div class="stats-label">今日任务</div>
      </div>
      <div class="stats-item">
        <div class="stats-value">{{ stats.completedTasks || 0 }}</div>
        <div class="stats-label">已完成</div>
      </div>
      <div class="stats-item">
        <div class="stats-value">{{ stats.pendingTasks || 0 }}</div>
        <div class="stats-label">待完成</div>
      </div>
    </div>

    <!-- 待完成任务 -->
    <div class="section">
      <div class="section-header">
        <h2>待完成任务</h2>
        <a @click="goToTasks('pending')">查看全部</a>
      </div>
      <task-list :tasks="pendingTasks" :loading="loading" />
    </div>

    <!-- 巡检区域 -->
    <div class="section">
      <div class="section-header">
        <h2>巡检区域</h2>
        <a @click="goToAreas">查看全部</a>
      </div>
      <div class="area-list">
        <div v-for="area in areas" :key="area.id" class="area-card">
          <div class="area-info">
            <div class="area-name">{{ area.areaName }}</div>
            <div class="area-type">{{ formatAreaType(area.areaType) }}</div>
          </div>
          <button class="scan-btn" @click="startScan(area)">扫码巡检</button>
        </div>
      </div>
    </div>

    <!-- 底部导航 -->
    <div class="bottom-nav">
      <a :class="['nav-item', $route.path === '/' ? 'active' : '']" @click.prevent="router.push('/')">
        <span class="material-icons">home</span>
        <span>首页</span>
      </a>
      <a :class="['nav-item', $route.path === '/records' ? 'active' : '']" @click.prevent="router.push('/records')">
        <span class="material-icons">assignment</span>
        <span>记录</span>
      </a>
      <a :class="['nav-item', $route.path === '/profile' ? 'active' : '']" @click.prevent="router.push('/profile')">
        <span class="material-icons">person</span>
        <span>我的</span>
      </a>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import TaskList from '@/components/TaskList.vue'

const router = useRouter()
const userInfo = ref({})
const unreadCount = ref(0)
const stats = ref({})
const pendingTasks = ref([])
const areas = ref([])
const loading = ref(false)

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

// 获取任务统计
async function fetchTaskStats() {
  try {
    const res = await request.get('/tasks/today/stats')
    if (res.code === 200 || res.code === 0) {
      stats.value = res.data
    }
  } catch (err) {
    console.error('获取任务统计失败:', err)
  }
}

// 获取待完成任务
async function fetchPendingTasks() {
  try {
    const res = await request.get('/tasks', {
      params: {
        status: 'PENDING',
        page: 1,
        size: 3
      }
    })
    if (res.code === 200 || res.code === 0) {
      pendingTasks.value = res.data.list
    }
  } catch (err) {
    console.error('获取待完成任务失败:', err)
  }
}

// 获取巡检区域
async function fetchAreas() {
  try {
    const res = await request.get('/areas', {
      params: {
        page: 1,
        size: 3
      }
    })
    if (res.code === 200 || res.code === 0) {
      areas.value = res.data.list
    }
  } catch (err) {
    console.error('获取巡检区域失败:', err)
  }
}

// 格式化区域类型
function formatAreaType(type) {
  const typeMap = {
    'server_room': '服务器机房',
    'power_room': '配电室',
    'network_room': '网络机房',
    'ups_room': 'UPS机房'
  }
  return typeMap[type] || type
}

// 页面跳转
function goToNotifications() {
  router.push('/notifications')
}

function goToTasks(status) {
  router.push({
    path: '/tasks',
    query: { status }
  })
}

function goToAreas() {
  router.push('/areas')
}

// 开始扫码
function startScan(area) {
  router.push({
    path: '/scan',
    query: { areaId: area.id }
  })
}

onMounted(() => {
  fetchUserInfo()
  fetchUnreadCount()
  fetchTaskStats()
  fetchPendingTasks()
  fetchAreas()
})
</script>

<style scoped>
.home-container {
  max-width: 420px;
  margin: 0 auto;
  min-height: 100vh;
  background: #f8f9fa;
  padding-bottom: 70px;
}

.user-info {
  background: #fff;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: var(--primary-color, #2196F3);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 500;
  margin-right: 12px;
}

.info {
  flex: 1;
}

.name {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.department {
  font-size: 14px;
  color: #666;
}

.notification {
  position: relative;
  cursor: pointer;
}

.notification .material-icons {
  font-size: 24px;
  color: #666;
}

.badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #f44336;
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
}

.stats-card {
  background: #fff;
  border-radius: 16px;
  margin: 20px;
  padding: 20px;
  display: flex;
  justify-content: space-around;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.stats-item {
  text-align: center;
}

.stats-value {
  font-size: 24px;
  font-weight: 600;
  color: var(--primary-color, #2196F3);
  margin-bottom: 4px;
}

.stats-label {
  font-size: 14px;
  color: #666;
}

.section {
  margin: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h2 {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin: 0;
}

.section-header a {
  font-size: 14px;
  color: var(--primary-color, #2196F3);
  cursor: pointer;
}

.area-list {
  display: grid;
  gap: 16px;
}

.area-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.area-info {
  flex: 1;
}

.area-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.area-type {
  font-size: 14px;
  color: #666;
}

.scan-btn {
  background: var(--primary-color, #2196F3);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.scan-btn:active {
  background: var(--primary-dark-color, #1976D2);
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  display: flex;
  justify-content: space-around;
  padding: 8px 0;
  box-shadow: 0 -2px 12px rgba(0,0,0,0.06);
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #666;
  text-decoration: none;
  font-size: 12px;
  padding: 4px 0;
  cursor: pointer;
}

.nav-item .material-icons {
  font-size: 24px;
  margin-bottom: 4px;
}

.nav-item.active {
  color: var(--primary-color, #2196F3);
}
</style> 
</style> 