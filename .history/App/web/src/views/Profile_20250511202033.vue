<template>
  <div class="profile-container">
    <div class="profile-topbar">个人中心</div>
    <div class="profile-content-wrapper">
      <div class="profile-card">
        <div class="profile-avatar">张</div>
        <div class="profile-info-block">
          <div class="profile-name">张三</div>
          <div class="profile-meta">工号：10086</div>
          <div class="profile-meta">部门：运维部</div>
        </div>
      </div>
      <div class="menu-list">
        <div class="menu-item">
          <span class="menu-icon"><span class="material-icons">person</span></span>
          <span class="menu-label">个人信息</span>
          <span class="material-icons">chevron_right</span>
        </div>
        <div class="menu-item">
          <span class="menu-icon"><span class="material-icons">notifications</span></span>
          <span class="menu-label">消息通知</span>
          <span class="material-icons">chevron_right</span>
        </div>
        <div class="menu-item">
          <span class="menu-icon"><span class="material-icons">info</span></span>
          <span class="menu-label">关于</span>
          <span class="material-icons">chevron_right</span>
        </div>
      </div>
      <div class="logout-button highlight" @click="logout">退出登录</div>
    </div>
    <div style="height:40px;"></div>
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
const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/profile.css');
@import url('https://fonts.googleapis.com/icon?family=Material+Icons');
</style> 