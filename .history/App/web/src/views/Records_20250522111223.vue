<template>
  <div class="records-container">
    <div class="records-header">
      <h1>巡检记录</h1>
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
      <div class="record-card" @click="goDetail">
        <div class="record-header">
          <div>区域A</div>
          <div class="record-status status-normal">正常</div>
          <div class="record-inspector" style="display:none;">张三</div>
        </div>
        <div class="record-info">
          <div class="record-time">2024-03-20 14:30</div>
        </div>
      </div>
      <div class="record-card" @click="goDetail">
        <div class="record-header">
          <div>区域B</div>
          <div class="record-status status-abnormal">异常</div>
          <div class="record-inspector" style="display:none;">李四</div>
        </div>
        <div class="record-info">
          <div class="record-time">2024-03-20 13:15</div>
        </div>
      </div>
      <div class="record-card" @click="goDetail">
        <div class="record-header">
          <div>区域C</div>
          <div class="record-status status-normal">正常</div>
          <div class="record-inspector" style="display:none;">王五</div>
        </div>
        <div class="record-info">
          <div class="record-time">2024-03-20 11:45</div>
        </div>
      </div>
    </div>
    <div class="bottom-nav">
      <a :class="['nav-item', $route.path === '/scan' ? 'active' : '']" @click.prevent="router.push('/scan')">
        <span class="material-icons">qr_code_scanner</span>
        <span>巡检</span>
      </a>
      <a :class="['nav-item', $route.path === '/records' ? 'active' : '']" @click.prevent="router.push('/records')">
        <span class="material-icons">history</span>
        <span>记录</span>
      </a>
      <a :class="['nav-item', $route.path === '/profile' ? 'active' : '']" @click.prevent="router.push('/profile')">
        <span class="material-icons">person</span>
        <span>我的</span>
      </a>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goDetail = () => {
  // 跳转到详情页，暂未实现
  alert('跳转到详情页（未实现）')
}
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/records.css');
</style> 