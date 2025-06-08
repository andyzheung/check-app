<template>
  <div class="main-container">
    <div class="content-area">
      <div class="card task-card">
        <div class="card-title">今日巡检任务</div>
        <div class="card-desc">待完成任务：{{ pendingTasks }}</div>
        <div class="card-desc">已完成任务：{{ completedTasks }}</div>
      </div>
      <div class="card area-card">
        <div class="card-title">巡检区域</div>
        <div v-for="area in areas" :key="area.id" class="card-desc">{{ area.name }}</div>
      </div>
      <div class="scan-btn-section">
        <button class="scan-round-btn" @click="router.push('/scan')">
          <span class="material-icons">photo_camera</span>
        </button>
        <div class="scan-btn-label">扫码开始巡检</div>
      </div>
    </div>
    <div class="bottom-nav">
      <a :class="['nav-item', $route.path === '/home' ? 'active' : '']" @click.prevent="router.push('/home')">
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
const pendingTasks = ref(0)
const completedTasks = ref(0)
const areas = ref([])

async function fetchTodayTasks() {
  try {
    const res = await request.get('/api/v1/schedules/today')
    if (res.data?.code === 0) {
      pendingTasks.value = res.data.data.pendingTasks
      completedTasks.value = res.data.data.completedTasks
    }
  } catch (err) {
    console.error('获取今日任务失败:', err)
  }
}

async function fetchAreas() {
  try {
    const res = await request.get('/api/v1/areas')
    if (res.data?.code === 0) {
      areas.value = res.data.data
    }
  } catch (err) {
    console.error('获取巡检区域失败:', err)
  }
}

onMounted(async () => {
  await fetchTodayTasks()
  await fetchAreas()
})
</script>

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/main.css');
</style> 