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
        <option value="1">机房A</option>
        <option value="2">机房B</option>
        <option value="3">机房C</option>
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
      <div v-for="r in records" :key="r.id" class="record-card">
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
      <div class="pagination-wrapper">
        <van-pagination 
          v-model="page" 
          :total-items="total" 
          :items-per-page="size"
          :show-page-size="3"
          force-ellipses
          @change="handlePageChange"
        />
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
  try {
    const res = await request.get('/users/current')
    isAdmin.value = res.data?.role === 'admin'
  } catch (err) {
    console.error('获取用户信息失败:', err)
    // 不影响记录列表的显示
  }
}

async function fetchRecords() {
  try {
    const params = {
      page: page.value,
      size: size.value,
      ...filters.value
    }
    console.log('请求参数:', params)
    const res = await getRecordList(params)
    if (res.code === 200) {
      records.value = res.data.list || []
      total.value = res.data.total || 0
      console.log('分页数据:', {
        当前页: page.value,
        每页条数: size.value,
        总记录数: total.value,
        当前页记录数: records.value.length
      })
    } else {
      throw new Error(res.message || '获取记录失败')
    }
  } catch (err) {
    console.error('获取巡检记录失败:', err)
    window.alert(err.message || '获取巡检记录失败')
  }
}

function handleSearch() {
  page.value = 1 // 重置页码
  fetchRecords()
}

function handlePageChange() {
  fetchRecords()
}

onMounted(() => {
  // 并行执行请求
  Promise.all([
    fetchUserInfo(),
    fetchRecords()
  ]).catch(err => {
    console.error('初始化数据失败:', err)
  })
})
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/records.css');
.status-normal { color: #4CAF50; }
.status-abnormal { color: #F44336; }
.pagination-wrapper {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}
</style> 