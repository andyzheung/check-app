<template>
  <div class="task-list">
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list
        v-model:loading="listLoading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
      >
        <div v-if="tasks.length > 0">
          <div v-for="task in tasks" :key="task.id" class="task-card">
            <div class="task-info">
              <div class="task-name">{{ task.taskName }}</div>
              <div class="task-time">计划时间：{{ formatTime(task.planTime) }}</div>
            </div>
            <div class="task-status" :class="task.status.toLowerCase()">
              {{ formatStatus(task.status) }}
            </div>
            <button v-if="task.status === 'PENDING'" class="start-btn" @click="startInspection(task)">
              开始巡检
            </button>
            <button v-else class="view-btn" @click="viewDetail(task)">
              查看详情
            </button>
          </div>
        </div>
        <div v-else class="empty-state">
          <span class="material-icons">assignment_turned_in</span>
          <p>暂无任务</p>
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  tasks: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['load-more'])

const router = useRouter()
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

// 格式化状态
function formatStatus(status) {
  const statusMap = {
    pending: '待完成',
    completed: '已完成',
    abnormal: '异常'
  }
  return statusMap[status] || status
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

// 下拉刷新
async function onRefresh() {
  refreshing.value = true
  finished.value = false
  listLoading.value = false
  try {
    await emit('load-more')
  } finally {
    refreshing.value = false
  }
}

// 加载更多
async function onLoad() {
  listLoading.value = true
  try {
    const hasMore = await emit('load-more')
    finished.value = !hasMore
  } finally {
    listLoading.value = false
  }
}

// 监听任务列表变化
watch(() => props.tasks, (newVal) => {
  if (newVal.length === 0) {
    finished.value = true
  }
}, { deep: true })
</script>

<style scoped>
.task-list {
  padding: 20px;
}

.task-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.task-info {
  flex: 1;
}

.task-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.task-time {
  font-size: 14px;
  color: #666;
}

.task-status {
  font-size: 14px;
  padding: 4px 8px;
  border-radius: 4px;
  margin: 0 12px;
}

.task-status.pending {
  color: #ff9800;
  background: rgba(255, 152, 0, 0.1);
}

.task-status.completed {
  color: #4caf50;
  background: rgba(76, 175, 80, 0.1);
}

.task-status.abnormal {
  color: #f44336;
  background: rgba(244, 67, 54, 0.1);
}

.start-btn,
.view-btn {
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.start-btn {
  background: var(--primary-color, #2196F3);
  color: #fff;
}

.start-btn:active {
  background: #1976d2;
}

.view-btn {
  background: #f5f5f5;
  color: #666;
}

.view-btn:active {
  background: #e0e0e0;
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