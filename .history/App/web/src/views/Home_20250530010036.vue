<template>
  <div class="home-container">
    <!-- 顶部用户信息 -->
    <div class="user-header">
      <div class="user-info">
        <div class="avatar">{{ userInfo.realName?.[0] || '?' }}</div>
        <div class="welcome">
          <div class="greeting">{{ greeting }}，{{ userInfo.realName }}</div>
          <div class="department">{{ userInfo.department }}</div>
        </div>
      </div>
      <div class="notification-icon" @click="goToNotifications">
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

    <!-- 任务列表 -->
    <div class="task-section">
      <div class="section-header">
        <div class="section-title">待完成任务</div>
        <a class="view-all" @click="viewAllTasks">查看全部</a>
      </div>
      <div class="task-list">
        <div v-for="task in pendingTasks" :key="task.id" class="task-card" @click="startInspection(task)">
          <div class="task-info">
            <div class="task-name">{{ task.areaName }}</div>
            <div class="task-time">{{ formatTime(task.startTime) }}</div>
          </div>
          <span class="material-icons">chevron_right</span>
        </div>
      </div>
    </div>

    <!-- 巡检区域 -->
    <div class="area-section">
      <div class="section-header">
        <div class="section-title">巡检区域</div>
        <a class="view-all" @click="viewAllAreas">查看全部</a>
      </div>
      <div class="area-list">
        <div v-for="area in areas" :key="area.id" class="area-card" @click="scanArea(area)">
          <div class="area-info">
            <div class="area-name">{{ area.areaName }}</div>
            <div class="area-type">{{ formatAreaType(area.areaType) }}</div>
          </div>
          <span class="material-icons">chevron_right</span>
        </div>
      </div>
    </div>

    <!-- 底部导航栏 -->
    <div class="bottom-nav">
      <router-link to="/" class="nav-item" exact-active-class="active" replace @click.native.prevent="goHome">
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
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import TaskList from '@/components/TaskList.vue'

const router = useRouter()
const userInfo = ref({})
const unreadCount = ref(0)
const stats = ref({
  totalTasks: 0,
  completedTasks: 0,
  pendingTasks: 0
})
const pendingTasks = ref([])
const areas = ref([])

// 计算问候语
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  return '晚上好'
})

// 格式化时间
function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  })
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
      stats.value = res.data || {
        totalTasks: 0,
        completedTasks: 0,
        pendingTasks: 0
      }
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
      pendingTasks.value = (res.data?.list || []).map(task => ({
        id: task.id,
        areaId: task.areaId,
        areaName: task.areaName,
        startTime: task.planTime,
        status: task.status
      }))
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
        status: 'active',
        page: 1,
        size: 3
      }
    })
    if (res.code === 200 || res.code === 0) {
      areas.value = (res.data?.list || []).map(area => ({
        id: area.id,
        areaCode: area.areaCode,
        areaName: area.areaName,
        areaType: area.areaType
      }))
    }
  } catch (err) {
    console.error('获取巡检区域失败:', err)
  }
}

// 页面跳转
function goToNotifications() {
  router.push('/notifications')
}

function viewAllTasks() {
  router.push('/tasks')
}

function viewAllAreas() {
  router.push('/areas')
}

// 开始巡检
function startInspection(task) {
  router.push({
    path: '/scan',
    query: { areaId: task.areaId }
  })
}

// 扫描区域
function scanArea(area) {
  router.push({
    path: '/scan',
    query: { areaId: area.id }
  })
}

// 加载更多任务
async function loadMoreTasks() {
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
    console.error('加载更多任务失败:', err)
  }
}

// 添加 goHome 方法
function goHome() {
  if (router.currentRoute.value.path === '/') {
    // 如果已经在首页，刷新数据
    fetchUserInfo()
    fetchUnreadCount()
    fetchTaskStats()
    fetchPendingTasks()
    fetchAreas()
  } else {
    // 如果不在首页，跳转到首页
    router.replace('/')
  }
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
  padding: 16px;
  padding-bottom: 60px; /* 为底部导航栏留出空间 */
}

.user-header {
  background: #fff;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  border-radius: 12px;
  margin-bottom: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 48px;
  height: 48px;
  background: #1989fa;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.welcome {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.greeting {
  font-size: 16px;
  font-weight: 500;
  color: #323233;
}

.department {
  font-size: 14px;
  color: #969799;
}

.notification-icon {
  position: relative;
  cursor: pointer;
}

.notification-icon .material-icons {
  font-size: 24px;
  color: #666;
}

.badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #ee0a24;
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 16px;
  text-align: center;
}

.stats-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  justify-content: space-around;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  margin-bottom: 16px;
}

.stats-item {
  text-align: center;
}

.stats-value {
  font-size: 24px;
  font-weight: 500;
  color: #323233;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 14px;
  color: #969799;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #323233;
}

.view-all {
  font-size: 14px;
  color: #1989fa;
  cursor: pointer;
}

.task-section,
.area-section {
  margin-bottom: 16px;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.task-list,
.area-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.task-card,
.area-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  transition: transform 0.2s;
  border-bottom: 1px solid #f5f5f5;
}

.task-card:last-child,
.area-card:last-child {
  border-bottom: none;
}

.task-card:hover,
.area-card:hover {
  transform: translateY(-2px);
}

.task-info,
.area-info {
  flex: 1;
}

.task-name,
.area-name {
  font-size: 16px;
  color: #323233;
  margin-bottom: 4px;
}

.task-time,
.area-type {
  font-size: 14px;
  color: #969799;
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
  gap: 4px;
  color: #969799;
  text-decoration: none;
  font-size: 14px;
}

.nav-item.active {
  color: #1989fa;
}
</style> 