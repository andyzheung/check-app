<template>
  <div class="records-container">
    <div class="records-header">
      <h1>巡检记录</h1>
      <div v-if="isAdmin" class="admin-tip">（管理员可查看所有记录）</div>
    </div>
    <div class="records-filter">
      <input type="date" class="filter-input" v-model="filters.startDate" placeholder="开始日期" />
      <span style="margin: 0 6px;">-</span>
      <input type="date" class="filter-input" v-model="filters.endDate" placeholder="结束日期" />
      <select class="filter-input" v-model="filters.areaId">
        <option value="">全部区域</option>
        <option v-for="(area, index) in areas" :key="area?.id || index" :value="area?.id || ''">
          {{ area?.areaName || area?.name || '未命名区域' }}
        </option>
      </select>
      <select class="filter-input" v-model="filters.status">
        <option value="">全部状态</option>
        <option value="normal">正常</option>
        <option value="abnormal">异常</option>
      </select>
      <input type="text" class="filter-input" v-model="filters.keyword" placeholder="搜索关键字" />
      <button class="search-btn" @click="handleSearch"><span class="material-icons">search</span></button>
    </div>
    <div class="records-content">
      <div v-if="records.length === 0" class="empty-records">
        暂无记录数据
      </div>
      
      <div v-for="(r, index) in records" :key="r?.id || index" class="record-card" @click="r?.id ? viewDetail(r.id) : null">
        <div class="record-header">
          <div>{{ r?.areaName || '未知区域' }}</div>
          <div class="record-status" :class="{'status-abnormal': r?.status === 'abnormal', 'status-normal': r?.status === 'normal'}">
            {{ r?.status === 'normal' ? '正常' : '异常' }}
          </div>
          <div class="record-inspector">{{ r?.inspectorName || '未知巡检员' }}</div>
        </div>
        <div class="record-info">
          <div class="record-time">{{ formatDateTime(r?.inspectionTime) }}</div>
          <div class="record-id">{{ r?.recordNo || `记录${index+1}` }}</div>
        </div>
      </div>
      <div class="pagination-wrapper" v-if="records.length > 0">
        <van-pagination 
          v-model="page" 
          :total-items="total" 
          :items-per-page="size"
          :show-page-size="3"
          force-ellipses
          @change="handlePageChange"
        >
          <template #prev-text>
            <van-icon name="arrow-left" />上一页
          </template>
          <template #next-text>
            下一页<van-icon name="arrow" />
          </template>
          <template #page="{ text }">
            {{ text }}
          </template>
        </van-pagination>
        <div class="pagination-info">
          共 {{ total }} 条记录，每页 {{ size }} 条
        </div>
      </div>
    </div>
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
import { getRecordList, getAllAreas } from '@/api/inspection'
import request from '@/utils/request'

const router = useRouter()
const records = ref([])
const areas = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const filters = ref({
  areaId: '',
  status: '',
  startDate: '',
  endDate: '',
  keyword: ''
})
const isAdmin = ref(false)

// 格式化日期时间
function formatDateTime(dateTimeStr) {
  if (!dateTimeStr) return '';
  
  try {
    const date = new Date(dateTimeStr);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    });
  } catch (err) {
    console.error('日期格式化错误:', err);
    return dateTimeStr;
  }
}

async function fetchAreas() {
  try {
    const res = await getAllAreas()
    if (res.code === 200 || res.code === 0) {
      areas.value = res.data || []
    }
  } catch (err) {
    console.error('获取区域列表失败:', err)
  }
}

async function fetchUserInfo() {
  try {
    const res = await request.get('/users/current')
    isAdmin.value = res.data?.role === 'admin'
  } catch (err) {
    console.error('获取用户信息失败:', err)
  }
}

async function fetchRecords() {
  try {
    const params = {
      page: page.value,
      size: size.value,
      ...filters.value
    }
    
    // 发送API请求
    const response = await request.get('/records', { params });
    
    // 处理响应
    if (response && (response.code === 200 || response.code === 0)) {
      if (response.data && response.data.list && Array.isArray(response.data.list)) {
        const recordList = response.data.list;
        
        // 更新数据
        records.value = recordList;
        total.value = response.data.total || recordList.length;
      } else {
        console.error('API响应中缺少list数组');
        records.value = [];
        total.value = 0;
      }
    } else {
      console.error('API响应状态错误:', response?.code, response?.message);
      records.value = [];
      total.value = 0;
    }
  } catch (err) {
    console.error('获取巡检记录失败:', err);
    records.value = [];
    total.value = 0;
  }
}

function handleSearch() {
  page.value = 1
  fetchRecords()
}

function handlePageChange() {
  fetchRecords()
}

function viewDetail(id) {
  router.push({
    path: '/record-detail',
    query: { id }
  })
}

onMounted(async () => {
  await Promise.all([
    fetchUserInfo(),
    fetchAreas()
  ])
  fetchRecords()
})
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/records.css');

.record-status {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: 500;
  white-space: nowrap;
}

.status-normal {
  color: #4CAF50;
}

.status-abnormal {
  background: #FFEBEE;
  color: #F44336;
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  width: fit-content;
}
.pagination-wrapper {
  margin: 20px 0;
  padding: 0 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}
.pagination-info {
  color: #666;
  font-size: 14px;
}
:deep(.van-pagination) {
  --van-pagination-height: 40px;
  --van-pagination-item-width: 40px;
  --van-pagination-font-size: 16px;
}
:deep(.van-pagination__item) {
  margin: 0 4px;
}
:deep(.van-pagination__item--active) {
  background-color: #1989fa;
  color: white;
}
:deep(.van-pagination__prev),
:deep(.van-pagination__next) {
  padding: 0 12px;
  min-width: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}
.record-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  padding: 16px;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: transform 0.2s;
}

.record-card:active {
  transform: scale(0.98);
}

.empty-records {
  text-align: center;
  padding: 40px 0;
  color: #999;
  font-size: 16px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  margin: 0 16px;
}

.record-id {
  font-size: 14px;
  color: #888;
  margin-left: 15px;
}
</style> 