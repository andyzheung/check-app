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
        <option value="A">A区机房</option>
        <option value="B">B区机房</option>
        <option value="C">C区机房</option>
      </select>
      <select class="filter-input" v-model="filters.status">
        <option value="">全部状态</option>
        <option value="正常">正常</option>
        <option value="异常">异常</option>
      </select>
      <input type="text" class="filter-input" v-model="filters.keyword" placeholder="搜索关键字" />
      <button class="search-btn" @click="fetchRecords"><span class="material-icons">search</span></button>
    </div>
    <div class="records-content">
      <div v-for="r in records" :key="r.id" class="record-card">
        <div class="record-header">
          <div>{{ r.areaName || r.pointName || r.pointCode }}</div>
          <div class="record-status" :class="{'status-abnormal': r.status === '异常', 'status-normal': r.status === '正常'}">{{ r.status || '已提交' }}</div>
          <div class="record-inspector">{{ r.inspectorName || r.username }}</div>
        </div>
        <div class="record-info">
          <div class="record-time">{{ r.inspectionTime || r.createTime }}</div>
          <div class="record-remark">{{ r.remark }}</div>
        </div>
      </div>
    </div>
    <div class="bottom-nav">
      <a :class="['nav-item', $route.path === '/scan' ? 'active' : '']" @click.prevent="router.push('/scan')">
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
import { getRecordList } from '@/api/inspection'
import request from '@/utils/request'

const router = useRouter()
const records = ref([])
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
  const res = await request.get('/api/v1/users/current')
  isAdmin.value = res.data?.data?.role === 'admin'
}

async function fetchRecords() {
  const params = {
    page: page.value,
    size: size.value,
    ...filters.value
  }
  const res = await getRecordList(params)
  records.value = res.data?.data?.list || []
  total.value = res.data?.data?.total || 0
}

onMounted(async () => {
  await fetchUserInfo()
  await fetchRecords()
})
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/records.css');
.status-normal { color: #4CAF50; }
.status-abnormal { color: #F44336; }
</style> 