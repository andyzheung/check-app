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
        <option v-for="area in areas" :key="area.id" :value="area.id">
          {{ area.name }}
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
      <div class="record-list">
        <div v-for="r in records" :key="r.id" class="record-card" @click="viewDetail(r.id)">
          <div class="record-header">
            <div>{{ r.areaName }}</div>
            <div class="record-status" :class="{'status-abnormal': r.status === 'abnormal', 'status-normal': r.status === 'normal'}">
              {{ r.status === 'normal' ? '正常' : '异常' }}
            </div>
            <div class="record-inspector">{{ r.inspectorName }}</div>
          </div>
          <div class="record-info">
            <div class="record-time">{{ r.inspectionTime }}</div>
            <div class="record-remark">{{ r.remark }}</div>
          </div>
        </div>
      </div>
      <div class="pagination-wrapper">
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

async function fetchUserInfo() {
  try {
    const res = await request.get('/api/v1/users/current')
    isAdmin.value = res.data?.role === 'admin'
  } catch (err) {
    console.error('获取用户信息失败:', err)
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

async function fetchRecords() {
  try {
    const params = {
      page: page.value,
      size: size.value,
      ...filters.value
    }
    const res = await getRecordList(params)
    if (res.code === 200 || res.code === 0) {
      records.value = (res.data?.list || []).map(record => ({
        ...record,
        inspectorName: record.inspectorName || record.username,
        areaName: record.areaName || areas.value.find(a => a.id === record.areaId)?.name || record.areaCode
      }))
      total.value = res.data?.total || 0
    }
  } catch (err) {
    console.error('获取巡检记录失败:', err)
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
.status-normal { color: #4CAF50; }
.status-abnormal { color: #F44336; }
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
.record-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
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
.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.record-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}
.record-inspector {
  color: #999;
  font-size: 14px;
}
.record-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.record-time {
  color: #666;
  font-size: 14px;
}
.record-remark {
  color: #999;
  font-size: 14px;
}
</style> 