<template>
  <div class="app-container">
    <!-- 主内容区 -->
    <div class="main-content">
      <router-view></router-view>
    </div>

    <!-- 底部导航栏 -->
    <van-tabbar v-model="active" route>
      <van-tabbar-item
        to="/inspection/records"
        icon="records"
      >
        巡检记录
      </van-tabbar-item>
      <van-tabbar-item
        to="/inspection/tasks"
        icon="todo-list"
      >
        巡检任务
      </van-tabbar-item>
      <van-tabbar-item
        to="/inspection/scan"
        icon="scan"
      >
        扫码巡检
      </van-tabbar-item>
      <van-tabbar-item
        to="/user"
        icon="user-o"
      >
        我的
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const active = ref(0);
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  background-color: #f7f8fa;
}
</style> 