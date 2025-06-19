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
            <div class="area-code">{{ area.areaCode }}</div>
          </div>
          <div class="area-type">{{ area.areaType }}</div>
        </div>
      </div>
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
    'ups_room': 'UPS机房',
    'datacenter': '数据中心',
    'weakroom': '弱电间',
    'A': '机房',
    'B': '办公区',
    'C': '设备区',
    'D': '数据中心',
    'E': '弱电间'
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
    console.log('获取任务统计响应:', res);
    if (res.code === 200 || res.code === 0) {
      stats.value = res.data || {
        totalTasks: 0,
        completedTasks: 0,
        pendingTasks: 0
      };
      console.log('更新后的统计数据:', stats.value);
    }
  } catch (err) {
    console.error('获取任务统计失败:', err)
  }
}

// 获取待完成任务
async function fetchPendingTasks() {
  try {
    console.log('开始获取待完成任务...');
    const res = await request.get('/tasks', {
      params: {
        status: 'PENDING',
        page: 1,
        size: 3
      }
    })
    console.log('待完成任务API响应:', res)
    if (res.code === 200 || res.code === 0) {
      // 检查返回的数据结构
      console.log('返回的数据结构:', {
        hasData: !!res.data,
        hasRecords: !!(res.data && res.data.records),
        recordsLength: res.data?.records?.length || 0,
        firstRecord: res.data?.records?.[0]
      });
      
      pendingTasks.value = (res.data?.records || []).map(task => {
        console.log('处理任务数据:', task)
        return {
          id: task.id,
          areaId: task.pointId, // 使用pointId作为areaId
          areaName: task.name || '未命名任务', // 优先使用name字段
          startTime: task.planTime || new Date(),
          status: task.status || 'PENDING'
        }
      })
      console.log('处理后的任务列表:', pendingTasks.value)
    } else {
      console.error('获取任务失败:', res.message || '未知错误');
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
    console.log('巡检区域API响应:', res)
    if (res.code === 200 || res.code === 0) {
      const records = res.data?.records || [];
      console.log('原始区域数据:', records);
      
      areas.value = records.map(area => {
        return {
          id: area.id,
          areaCode: area.areaCode || area.code,
          areaName: area.areaName || area.name,
          areaType: formatAreaType(area.areaType || area.type)
        }
      })
      console.log('处理后的区域列表:', areas.value)
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
  
  // 添加测试接口调用
  testApiConnection()
})

// 测试API连接
async function testApiConnection() {
  try {
    console.log('测试API连接...')
    const res = await request.get('/tasks/test')
    console.log('API测试响应:', res)
  } catch (err) {
    console.error('API测试失败:', err)
  }
}
</script>

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/home.css');
</style> 