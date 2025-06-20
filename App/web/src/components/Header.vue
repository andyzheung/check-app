<template>
  <header class="app-header">
    <div class="header-content">
      <div class="header-left">
        <button 
          v-if="showBack" 
          class="back-btn" 
          @click="goBack"
        >
          <span class="material-icons">arrow_back</span>
        </button>
      </div>
      <h1 class="header-title">{{ title }}</h1>
      <div class="header-right">
        <slot name="actions"></slot>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  showBack: {
    type: Boolean,
    default: false
  }
});

const title = computed(() => {
  return props.title || route.meta.title || '巡检系统';
});

const showBack = computed(() => {
  return props.showBack || route.meta.showBack || false;
});

const goBack = () => {
  if (window.history.length > 1) {
    router.go(-1);
  } else {
    router.push('/');
  }
};
</script>

<style scoped>
.app-header {
  background: #1890ff;
  position: fixed;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 480px;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  color: #fff;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  height: 56px;
  max-width: 480px;
  margin: 0 auto;
}

.header-left, .header-right {
  display: flex;
  align-items: center;
  flex: 1;
}

.header-right {
  justify-content: flex-end;
}

.header-title {
  flex: 2;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
}

.back-btn {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: background-color 0.2s;
}

.back-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.material-icons {
  font-size: 20px;
}
</style> 