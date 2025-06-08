<template>
  <div class="notifications-container">
    <!-- 顶部导航 -->
    <div class="notifications-header">
      <a class="back-button" @click.prevent="router.push('/profile')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>消息通知</h1>
    </div>

    <!-- 消息列表 -->
    <div class="notifications-content">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model:loading="listLoading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <div v-if="notifications.length > 0" class="notification-list">
            <div v-for="notification in notifications" :key="notification.id" 
                 class="notification-item" :class="{ unread: notification.status === 'unread' }"
                 @click="handleNotificationClick(notification)">
              <div class="notification-title">{{ notification.title }}</div>
              <div class="notification-content">{{ notification.content }}</div>
              <div class="notification-time">{{ formatTime(notification.createTime) }}</div>
            </div>
          </div>
          <div v-else class="empty-state">
            <span class="material-icons">notifications_off</span>
            <p>暂无消息通知</p>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const notifications = ref([])
const page = ref(1)
const size = ref(10)
const refreshing = ref(false)
const listLoading = ref(false)
const finished = ref(false)

// 格式化时间
function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  })
}

// 获取消息列表
async function fetchNotifications(isLoadMore = false) {
  if (listLoading.value && !refreshing.value) return
  try {
    const res = await request.get('/notifications', {
      params: {
        page: page.value,
        size: size.value
      }
    })
    if (res.code === 200 || res.code === 0) {
      if (isLoadMore) {
        notifications.value.push(...res.data.list)
      } else {
        notifications.value = res.data.list
      }
      return res.data.total
    }
    return 0
  } catch (err) {
    console.error('获取消息列表失败:', err)
    return 0
  }
}

// 下拉刷新
async function onRefresh() {
  refreshing.value = true
  page.value = 1
  finished.value = false
  try {
    await fetchNotifications()
  } finally {
    refreshing.value = false
  }
}

// 加载更多
async function onLoad() {
  listLoading.value = true
  try {
    const total = await fetchNotifications(true)
    if (notifications.value.length >= total) {
      finished.value = true
    } else {
      page.value++
    }
  } finally {
    listLoading.value = false
  }
}

// 处理消息点击
async function handleNotificationClick(notification) {
  if (notification.status === 'unread') {
    try {
      await request.put(`/notifications/${notification.id}/read`)
      notification.status = 'read'
    } catch (err) {
      console.error('标记消息已读失败:', err)
    }
  }
  
  // 根据消息类型处理跳转
  switch (notification.type) {
    case 'task':
      router.push({
        path: '/tasks',
        query: { id: notification.targetId }
      })
      break
    case 'schedule':
      router.push({
        path: '/schedules',
        query: { id: notification.targetId }
      })
      break
    case 'system':
    default:
      // 系统消息不跳转
      break
  }
}

onMounted(() => {
  fetchNotifications()
})
</script>

<style scoped>
.notifications-container {
  max-width: 420px;
  margin: 0 auto;
  min-height: 100vh;
  background: #f8f9fa;
}

.notifications-header {
  padding: 28px 0 18px 0;
  text-align: center;
  font-size: 22px;
  font-weight: 700;
  color: var(--primary-color, #2196F3);
  letter-spacing: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.notifications-header .back-button {
  position: absolute;
  left: 18px;
  top: 32px;
  color: #888;
  text-decoration: none;
}

.notifications-content {
  padding: 20px;
}

.notification-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.notification-item {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transition: transform 0.2s;
}

.notification-item:active {
  transform: scale(0.98);
}

.notification-item.unread {
  border-left: 4px solid var(--primary-color, #2196F3);
}

.notification-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.notification-content {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.5;
}

.notification-time {
  font-size: 12px;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #999;
}

.empty-state .material-icons {
  font-size: 48px;
  margin-bottom: 12px;
}

.empty-state p {
  font-size: 14px;
  margin: 0;
}
</style> 