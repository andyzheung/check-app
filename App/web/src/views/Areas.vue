<template>
  <div class="areas-container">
    <!-- 顶部导航 -->
    <div class="areas-header">
      <a class="back-button" @click.prevent="router.push('/')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>巡检区域</h1>
    </div>

    <!-- 区域列表 -->
    <div class="areas-content">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <div v-for="area in areas" :key="area.id" class="area-card" @click="scanArea(area)">
            <div class="area-info">
              <div class="area-name">{{ area.areaName }}</div>
              <div class="area-code">{{ area.areaCode }}</div>
              <div class="area-desc">{{ area.description }}</div>
            </div>
            <div class="area-type">{{ area.areaType }}</div>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>

    <!-- 扫码按钮 -->
    <div class="scan-button" @click="startScan">
      <span class="material-icons">qr_code_scanner</span>
      <span>扫码巡检</span>
    </div>
    
    <!-- 底部导航栏 -->
    <div class="bottom-nav">
      <router-link to="/home" class="nav-item" active-class="active">
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
const areas = ref([])
const loading = ref(false)
const refreshing = ref(false)
const finished = ref(false)
const page = ref(1)
const size = ref(10)

// 获取区域列表
async function fetchAreas(isLoadMore = false) {
  if (loading.value && !refreshing.value) return
  
  try {
    console.log('获取区域列表 - page:', page.value, 'size:', size.value);
    // 修复API请求路径，使用完整的API路径
    const res = await request.get('/areas', {
      params: {
        status: 'active',
        page: page.value,
        size: size.value
      }
    });
    
    console.log('区域列表API响应:', res);
    
    if (res.code === 200 || res.code === 0) {
      // 处理返回的数据结构
      const records = res.data?.records || [];
      const total = res.data?.total || 0;
      
      console.log('获取到区域数据:', {
        records: records,
        total: total,
        currentPage: page.value
      });
      
      // 处理区域数据，确保字段名称正确
      const formattedRecords = records.map(area => {
        return {
          id: area.id,
          areaCode: area.areaCode || area.code,
          areaName: area.areaName || area.name,
          areaType: formatAreaType(area.areaType || area.type),
          description: area.description || '',
          status: area.status
        };
      });
      
      // 更新区域列表
      if (isLoadMore) {
        areas.value = [...areas.value, ...formattedRecords];
      } else {
        areas.value = formattedRecords;
      }
      
      console.log('处理后的区域数据:', areas.value);
      
      // 判断是否还有更多数据
      finished.value = areas.value.length >= total;
      
      return total;
    } else {
      console.error('获取区域列表失败:', res.message);
      return 0;
    }
  } catch (err) {
    console.error('获取区域列表失败:', err);
    return 0;
  } finally {
    loading.value = false;
  }
}

// 格式化区域类型
function formatAreaType(type) {
  const typeMap = {
    'server_room': '服务器机房',
    'power_room': '配电室',
    'network_room': '网络机房',
    'ups_room': 'UPS机房',
    'monitor_room': '监控室',
    'A': '机房',
    'B': '配电室'
  };
  return typeMap[type] || type || '未知类型';
}

// 下拉刷新
async function onRefresh() {
  refreshing.value = true
  page.value = 1
  finished.value = false
  try {
    await fetchAreas()
  } finally {
    refreshing.value = false
  }
}

// 加载更多
async function onLoad() {
  loading.value = true
  try {
    const total = await fetchAreas(true)
    if (areas.value.length >= total) {
      finished.value = true
    } else {
      page.value++
    }
  } finally {
    loading.value = false
  }
}

// 扫描区域
function scanArea(area) {
  router.push({
    path: '/scan',
    query: { areaId: area.id }
  })
}

// 开始扫码
function startScan() {
  router.push('/scan')
}

onMounted(() => {
  fetchAreas()
})
</script>

<style scoped>
.areas-container {
  max-width: 420px;
  margin: 0 auto;
  min-height: 100vh;
  background: #f8f9fa;
  padding-bottom: 100px;
}

.areas-header {
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

.areas-header .back-button {
  position: absolute;
  left: 18px;
  top: 32px;
  color: #888;
  text-decoration: none;
}

.areas-content {
  padding: 20px;
}

.area-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transition: transform 0.2s;
}

.area-card:active {
  transform: scale(0.98);
}

.area-info {
  flex: 1;
  margin-right: 12px;
}

.area-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.area-code {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.area-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.4;
}

.area-type {
  font-size: 14px;
  color: var(--primary-color, #2196F3);
  background: rgba(33, 150, 243, 0.1);
  padding: 4px 8px;
  border-radius: 4px;
  white-space: nowrap;
}

.scan-button {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--primary-color, #2196F3);
  color: #fff;
  border-radius: 24px;
  padding: 12px 24px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
  transition: transform 0.2s;
}

.scan-button:active {
  transform: translateX(-50%) scale(0.95);
}

.scan-button .material-icons {
  font-size: 20px;
}

.scan-button span:not(.material-icons) {
  font-size: 16px;
  font-weight: 500;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 -2px 12px rgba(0,0,0,0.06);
}

.nav-item {
  text-decoration: none;
  color: #666;
  font-size: 16px;
  font-weight: 500;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.nav-item.active {
  color: var(--primary-color, #2196F3);
}
</style> 