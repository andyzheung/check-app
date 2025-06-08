<template>
  <div class="bottom-nav">
    <router-link to="/" class="nav-item" active-class="active">
      <span class="material-icons">home</span>
      <span>首页</span>
    </router-link>
    <router-link to="/scan" class="nav-item" active-class="active">
      <span class="material-icons">qr_code_scanner</span>
      <span>巡检</span>
    </router-link>
    <router-link to="/records" class="nav-item" active-class="active">
      <span class="material-icons">assignment</span>
      <span>记录</span>
    </router-link>
    <router-link to="/profile" class="nav-item" active-class="active">
      <span class="material-icons">person</span>
      <span>我的</span>
    </router-link>
  </div>
</template>

<script setup>
// 无需额外逻辑
</script>

<style scoped>
.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  display: flex;
  justify-content: space-around;
  padding: 8px 0;
  box-shadow: 0 -2px 12px rgba(0,0,0,0.06);
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #666;
  text-decoration: none;
  font-size: 12px;
  padding: 4px 0;
}

.nav-item .material-icons {
  font-size: 24px;
  margin-bottom: 4px;
}

.nav-item.active {
  color: var(--primary-color, #2196F3);
}
</style> 