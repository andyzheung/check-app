<template>
  <div class="tasks-container">
    <!-- 顶部导航 -->
    <div class="tasks-header">
      <a class="back-button" @click.prevent="router.push('/')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>巡检任务</h1>
    </div>

    <!-- 任务筛选 -->
    <div class="filter-section">
      <van-tabs v-model:active="activeTab" sticky>
        <van-tab title="待完成" name="pending">
          <div class="task-list-container">
            <div v-for="task in pendingTasks" :key="task.id" class="task-card">
              <div class="task-info">
                <div class="area-name">{{ task.areaName }}</div>
                <div class="task-time">{{ formatTime(task.startTime) }}</div>
              </div>
              <div class="task-status pending">待完成</div>
              <button class="start-btn" @click="startInspection(task)">开始巡检</button>
            </div>
            <div v-if="pendingTasks.length === 0 && !loading" class="empty-tip">
              暂无待完成任务
            </div>
            <div v-if="loading" class="loading-more">
              加载中...
            </div>
          </div>
        </van-tab>
        <van-tab title="已完成" name="completed">
          <div class="task-list-container">
            <div v-for="task in completedTasks" :key="task.id" class="task-card">
              <div class="task-info">
                <div class="area-name">{{ task.areaName }}</div>
                <div class="task-time">{{ formatTime(task.startTime) }}</div>
              </div>
              <div class="task-status completed">已完成</div>
              <button class="view-btn" @click="viewDetail(task)">查看详情</button>
            </div>
            <div v-if="completedTasks.length === 0 && !loading" class="empty-tip">
              暂无已完成任务
            </div>
            <div v-if="loading" class="loading-more">
              加载中...
            </div>
          </div>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const activeTab = ref('pending')
const pendingTasks = ref([])
const completedTasks = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)

// 格式化时间
function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  })
}

// 开始巡检
function startInspection(task) {
  router.push({
    path: '/scan',
    query: { areaId: task.areaId }
  })
}

// 查看详情
function viewDetail(task) {
  router.push({
    path: '/record-detail',
    query: { id: task.id }
  })
}

// 获取任务列表
async function fetchTasks(status, isLoadMore = false) {
  if (loading.value) return
  loading.value = true
  try {
    console.log('获取任务列表 - status:', status, 'page:', page.value, 'size:', size.value);
    const res = await request.get('/tasks', {
      params: {
        status: status.toUpperCase(), // 转为大写，匹配后端枚举值
        page: page.value,
        size: size.value
      }
    })
    console.log('任务列表API响应:', res);
    if (res.code === 200 || res.code === 0) {
      // 处理返回的数据结构
      const records = res.data?.records || [];
      const total = res.data?.total || 0;
      
      console.log('获取到任务数据:', {
        records: records,
        total: total,
        currentPage: page.value
      });
      
      // 处理任务数据
      const processedTasks = records.map(task => ({
        id: task.id,
        areaId: task.pointId || task.areaId, // 使用pointId或areaId
        areaName: task.name || task.taskName || '未命名任务',
        startTime: task.planTime,
        status: task.status?.toLowerCase() || 'pending'
      }));
      
      if (status === 'pending') {
        if (isLoadMore) {
          pendingTasks.value.push(...processedTasks);
        } else {
          pendingTasks.value = processedTasks;
        }
      } else {
        if (isLoadMore) {
          completedTasks.value.push(...processedTasks);
        } else {
          completedTasks.value = processedTasks;
        }
      }
      return total;
    }
    return 0;
  } catch (err) {
    console.error('获取任务列表失败:', err);
    return 0;
  } finally {
    loading.value = false;
  }
}

// 加载更多
async function loadMore() {
  page.value++
  const total = await fetchTasks(activeTab.value, true)
  const currentTasks = activeTab.value === 'pending' ? pendingTasks.value : completedTasks.value
  if (currentTasks.length >= total) {
    // 没有更多数据了
    return false
  }
  return true
}

// 监听标签切换
watch(activeTab, (newVal) => {
  page.value = 1
  fetchTasks(newVal)
})

onMounted(() => {
  fetchTasks(activeTab.value)
})
</script>

<style scoped>
.tasks-container {
  max-width: 420px;
  margin: 0 auto;
  min-height: 100vh;
  background: #f8f9fa;
}

.tasks-header {
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

.tasks-header .back-button {
  position: absolute;
  left: 18px;
  top: 32px;
  color: #888;
  text-decoration: none;
}

.filter-section {
  background: #fff;
  margin-bottom: 20px;
}

:deep(.van-tabs__wrap) {
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

:deep(.van-tab) {
  font-size: 16px;
  color: #666;
}

:deep(.van-tab--active) {
  color: var(--primary-color, #2196F3);
  font-weight: 500;
}

:deep(.van-tabs__line) {
  background-color: var(--primary-color, #2196F3);
}
</style> 