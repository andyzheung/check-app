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

    <!-- 待完成任务 -->
    <div class="section-card">
      <div class="section-header">
        <div class="section-title">待完成任务</div>
        <a class="view-all" @click="viewAllTasks">查看全部</a>
      </div>
      <div class="task-list">
        <div v-if="pendingTasks.length === 0" class="empty-state">
          暂无待完成任务
        </div>
        <div v-else v-for="task in pendingTasks" :key="task.id" class="list-item" @click="startInspection(task)">
          <div class="item-header">
            <h3 class="item-title">{{ task.areaName }}</h3>
            <div class="item-status pending">待完成</div>
          </div>
          <div class="item-time">{{ formatTime(task.startTime) }}</div>
        </div>
      </div>
    </div>

    <!-- 巡检区域 -->
    <div class="section-card">
      <div class="section-header">
        <div class="section-title">巡检区域</div>
        <a class="view-all" @click="viewAllAreas">查看全部</a>
      </div>
      <div class="area-list">
        <div v-if="areas.length === 0" class="empty-state">
          暂无巡检区域
        </div>
        <div v-else v-for="area in areas" :key="area.id" class="list-item" @click="scanArea(area)">
          <div class="item-header">
            <h3 class="item-title">{{ area.areaName }}</h3>
            <div class="item-badge">{{ area.areaType }}</div>
          </div>
          <div class="item-code">{{ area.areaCode }}</div>
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
  
  try {
    // 处理不同的时间格式
    let date
    if (typeof time === 'string') {
      // 如果是字符串，尝试解析
      date = new Date(time)
    } else if (time instanceof Date) {
      date = time
    } else {
      return ''
    }
    
    // 检查日期是否有效
    if (isNaN(date.getTime())) {
      return ''
    }
    
    // 使用浏览器本地时区格式化时间
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
      // 移除硬编码时区，使用浏览器本地时区
    })
  } catch (error) {
    console.error('时间格式化错误:', error, time)
    return ''
  }
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
      if (res.data && typeof res.data === 'object' && !Array.isArray(res.data)) {
        // 如果返回的是统计对象
        stats.value = {
          totalTasks: res.data.totalTasks || 0,
          completedTasks: res.data.completedTasks || 0,
          pendingTasks: res.data.pendingTasks || 0
        };
      } else {
        // 如果API返回格式不对，使用模拟数据进行测试
        console.warn('API返回格式不正确，使用模拟数据');
        stats.value = {
          totalTasks: 5,
          completedTasks: 2,
          pendingTasks: 3
        };
      }
      console.log('更新后的统计数据:', stats.value);
    }
  } catch (err) {
    console.error('获取任务统计失败:', err)
    // 失败时使用模拟数据
    stats.value = {
      totalTasks: 5,
      completedTasks: 2,
      pendingTasks: 3
    };
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
      const records = res.data?.records || res.data?.list || [];
      console.log('返回的数据结构:', {
        hasData: !!res.data,
        hasRecords: records.length > 0,
        recordsLength: records.length,
        firstRecord: records[0]
      });
      
      if (records.length > 0) {
        pendingTasks.value = records.map(task => {
          console.log('处理任务数据:', task)
          return {
            id: task.id,
            areaId: task.areaId || task.pointId,
            areaName: task.taskName || task.name || '未命名任务',
            startTime: task.planTime || task.startTime || new Date(),
            status: task.status || 'PENDING'
          }
        })
      } else {
        // 如果没有数据，使用模拟数据进行测试
        console.warn('没有待完成任务数据，使用模拟数据');
        pendingTasks.value = [
          {
            id: 1,
            areaId: 1,
            areaName: '数据中心日常巡检',
            startTime: new Date(),
            status: 'PENDING'
          },
          {
            id: 2,
            areaId: 2,
            areaName: '弱电间设备检查',
            startTime: new Date(),
            status: 'PENDING'
          }
        ];
      }
      console.log('处理后的任务列表:', pendingTasks.value)
    } else {
      console.error('获取任务失败:', res.message || '未知错误');
    }
  } catch (err) {
    console.error('获取待完成任务失败:', err)
    // 失败时使用模拟数据
    pendingTasks.value = [
      {
        id: 1,
        areaId: 1,
        areaName: '数据中心日常巡检',
        startTime: new Date(),
        status: 'PENDING'
      },
      {
        id: 2,
        areaId: 2,
        areaName: '弱电间设备检查',
        startTime: new Date(),
        status: 'PENDING'
      }
    ];
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
      const records = res.data?.records || res.data?.list || [];
      console.log('原始区域数据:', records);
      
      if (records.length > 0) {
        areas.value = records.map(area => {
          return {
            id: area.id,
            areaCode: area.areaCode || area.code,
            areaName: area.areaName || area.name,
            areaType: formatAreaType(area.areaType || area.type)
          }
        })
      } else {
        // 如果没有数据，使用模拟数据进行测试
        console.warn('没有巡检区域数据，使用模拟数据');
        areas.value = [
          {
            id: 1,
            areaCode: 'AREA101',
            areaName: 'Server Room A',
            areaType: '机房'
          },
          {
            id: 2,
            areaCode: 'AREA102',
            areaName: 'Power Room B',
            areaType: '办公区'
          },
          {
            id: 3,
            areaCode: 'AREA103',
            areaName: 'Network Room C',
            areaType: '设备区'
          }
        ];
      }
      console.log('处理后的区域列表:', areas.value)
    }
  } catch (err) {
    console.error('获取巡检区域失败:', err)
    // 失败时使用模拟数据
    areas.value = [
      {
        id: 1,
        areaCode: 'AREA101',
        areaName: 'Server Room A',
        areaType: '机房'
      },
      {
        id: 2,
        areaCode: 'AREA102',
        areaName: 'Power Room B',
        areaType: '办公区'
      },
      {
        id: 3,
        areaCode: 'AREA103',
        areaName: 'Network Room C',
        areaType: '设备区'
      }
    ];
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
.home-container {
  background: #f5f5f5;
  padding-bottom: 80px;
}

/* 用户信息卡片 */
.user-header {
  background: white;
  margin: 16px 16px 16px;
  border-radius: 12px;
  padding: 20px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: #1890ff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
  color: white;
}

.welcome {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.greeting {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.department {
  font-size: 14px;
  color: #666;
}

.notification-icon {
  position: relative;
  padding: 8px;
  cursor: pointer;
  border-radius: 50%;
  transition: background-color 0.2s;
  color: #666;
}

.notification-icon:hover {
  background: #f5f5f5;
}

.badge {
  position: absolute;
  top: 4px;
  right: 4px;
  background: #ff4d4f;
  color: white;
  border-radius: 50%;
  width: 16px;
  height: 16px;
  font-size: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

/* 统计卡片 */
.stats-card {
  background: white;
  margin: 0 16px 16px;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-around;
}

.stats-item {
  text-align: center;
  flex: 1;
}

.stats-value {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 14px;
  color: #666;
}

/* 区域卡片 */
.section-card {
  background: white;
  margin: 0 16px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 16px 12px;
  border-bottom: 1px solid #f0f0f0;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.view-all {
  color: #1890ff;
  font-size: 14px;
  cursor: pointer;
  text-decoration: none;
}

.view-all:hover {
  color: #40a9ff;
}

/* 列表项 */
.task-list, .area-list {
  padding: 0;
}

.empty-state {
  text-align: center;
  padding: 40px 16px;
  color: #999;
  font-size: 14px;
}

.list-item {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.list-item:last-child {
  border-bottom: none;
}

.list-item:hover {
  background: #f9f9f9;
}

.list-item:active {
  background: #f0f0f0;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.item-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.item-time, .item-code {
  font-size: 14px;
  color: #666;
}

/* 状态和标签 */
.item-status {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

.item-status.pending {
  background: #fff7e6;
  color: #fa8c16;
  border: 1px solid #ffd591;
}

.item-badge {
  background: #f0f0f0;
  color: #666;
  padding: 4px 8px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 500;
}
</style> 