<template>
  <nav class="bottom-nav">
    <div class="nav-content">
      <router-link
        v-for="item in navItems"
        :key="item.path"
        :to="item.path"
        class="nav-item"
        :class="{ active: isActive(item.path) }"
      >
        <span class="material-icons">{{ item.icon }}</span>
        <span class="nav-label">{{ item.label }}</span>
      </router-link>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const navItems = [
  {
    path: '/home',
    icon: 'home',
    label: '首页'
  },
  {
    path: '/scan',
    icon: 'qr_code_scanner',
    label: '扫码'
  },
  {
    path: '/records',
    icon: 'assignment',
    label: '记录'
  },
  {
    path: '/profile',
    icon: 'person',
    label: '我的'
  }
];

const isActive = (path) => {
  return route.path === path || route.path.startsWith(path + '/');
};
</script>

<style scoped>
.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 480px;
  background: #fff;
  border-top: 1px solid #e8e8e8;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.nav-content {
  display: flex;
  align-items: center;
  padding: 8px 0;
  height: 60px;
  width: 100%;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  color: #666;
  transition: color 0.2s;
  flex: 1;
  padding: 6px 0;
  border-radius: 8px;
}

.nav-item:hover {
  color: #1890ff;
}

.nav-item.active {
  color: #1890ff;
}

.nav-item .material-icons {
  font-size: 20px;
  margin-bottom: 2px;
}

.nav-label {
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

/* 为移动端优化触摸区域 */
@media (max-width: 480px) {
  .nav-item {
    min-height: 48px;
    padding: 6px 2px;
  }
  
  .nav-item .material-icons {
    font-size: 18px;
  }
  
  .nav-label {
    font-size: 11px;
  }
}
</style> 