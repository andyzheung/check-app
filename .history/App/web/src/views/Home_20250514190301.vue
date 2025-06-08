<template>
  <div class="main-container">
    <div class="content-area">
      <div class="card task-card">
        <div class="card-title">今日巡检任务</div>
        <div class="card-desc">待完成任务：3</div>
        <div class="card-desc">已完成任务：2</div>
      </div>
      <div class="card area-card">
        <div class="card-title">巡检区域</div>
        <div class="card-desc">A区机房</div>
        <div class="card-desc">B区机房</div>
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
</script>

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/main.css');
@import url('https://fonts.googleapis.com/icon?family=Material+Icons');
</style> 