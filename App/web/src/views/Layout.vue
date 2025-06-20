<template>
  <div class="app-layout">
    <Header />
    <div class="app-content">
      <router-view />
    </div>
    <BottomNav v-if="showBottomNav" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import Header from '@/components/Header.vue';
import BottomNav from '@/components/BottomNav.vue';

const route = useRoute();
const showBottomNav = computed(() => route.meta.showBottomNav !== false);
</script>

<style scoped>
.app-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f0f2f5; /* A slightly softer background color */
  
  /* Constrain width for mobile-first view and center it */
  max-width: 480px;
  margin: 0 auto;
  position: relative; /* Needed for child absolute positioning */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid #e0e0e0;
}

.app-content {
  flex: 1;
  overflow-y: auto;
  padding-top: 56px; /* Header height */
  padding-bottom: 60px; /* Adjust if bottom nav is present */
}
</style> 