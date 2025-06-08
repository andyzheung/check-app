<template>
  <div class="layout">
    <router-view />
    <van-tabbar v-model="active" route>
      <van-tabbar-item to="/home" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/scan" icon="scan">巡检</van-tabbar-item>
      <van-tabbar-item to="/records" icon="description">记录</van-tabbar-item>
      <van-tabbar-item to="/profile" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const active = ref(0)

// 根据当前路由更新底部导航激活状态
watch(() => route.path, (path) => {
  switch (path) {
    case '/home':
      active.value = 0
      break
    case '/scan':
      active.value = 1
      break
    case '/records':
      active.value = 2
      break
    case '/profile':
      active.value = 3
      break
  }
}, { immediate: true })
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

:deep(.van-tabbar) {
  background-color: #fff;
  border-top: 1px solid #f5f5f5;
}

:deep(.van-tabbar-item) {
  color: #666;
  font-size: 12px;
}

:deep(.van-tabbar-item--active) {
  color: var(--primary-color, #2196F3);
}
</style> 