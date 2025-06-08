<template>
  <div class="records-container">
    <div class="records-header">
      <h1>巡检记录</h1>
      <div v-if="isAdmin" class="admin-tip">（管理员可查看所有记录）</div>
    </div>
    <div class="records-filter">
      <input type="date" class="filter-input" placeholder="日期" />
      <select class="filter-input">
        <option>全部区域</option>
        <option>A区机房</option>
        <option>B区机房</option>
        <option>C区机房</option>
      </select>
      <select class="filter-input" style="display:none;">
        <option>全部人员</option>
        <option>张三</option>
        <option>李四</option>
        <option>王五</option>
      </select>
      <select class="filter-input">
        <option>全部状态</option>
        <option>正常</option>
        <option>异常</option>
      </select>
      <input type="text" class="filter-input" placeholder="搜索关键字" />
      <button class="search-btn"><span class="material-icons">search</span></button>
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
const isAdmin = ref(false)

async function fetchUserInfo() {
  const res = await request.get('/users/current')
  isAdmin.value = res.data?.data?.role === 'admin'
}

async function fetchRecords() {
  const res = await getRecordList()
  records.value = res.data?.data || []
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