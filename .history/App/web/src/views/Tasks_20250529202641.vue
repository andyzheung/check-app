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
          <task-list :tasks="pendingTasks" :loading="loading" @load-more="loadMore" />
        </van-tab>
        <van-tab title="已完成" name="completed">
          <task-list :tasks="completedTasks" :loading="loading" @load-more="loadMore" />
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import TaskList from '@/components/TaskList.vue'

const router = useRouter()
const activeTab = ref('pending')
const pendingTasks = ref([])
const completedTasks = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)

// 获取任务列表
async function fetchTasks(status, isLoadMore = false) {
  if (loading.value) return
  loading.value = true
  try {
    const res = await request.get('/tasks', {
      params: {
        status,
        page: page.value,
        size: size.value
      }
    })
    if (res.code === 200 || res.code === 0) {
      const tasks = res.data.list
      if (status === 'pending') {
        if (isLoadMore) {
          pendingTasks.value.push(...tasks)
        } else {
          pendingTasks.value = tasks
        }
      } else {
        if (isLoadMore) {
          completedTasks.value.push(...tasks)
        } else {
          completedTasks.value = tasks
        }
      }
      return res.data.total
    }
    return 0
  } catch (err) {
    console.error('获取任务列表失败:', err)
    return 0
  } finally {
    loading.value = false
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