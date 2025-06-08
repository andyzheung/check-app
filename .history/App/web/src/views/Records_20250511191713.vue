<template>
  <div class="records-page">
    <header class="header">
      <button class="back-btn" @click="router.back()">返回</button>
      <h1>巡检记录</h1>
    </header>
    
    <div class="filter-bar">
      <input type="date" v-model="startDate" class="date-input" />
      <span>至</span>
      <input type="date" v-model="endDate" class="date-input" />
      <button class="filter-btn" @click="handleFilter">筛选</button>
    </div>
    
    <div class="records-list">
      <div v-for="record in records" :key="record.id" class="record-item">
        <div class="record-header">
          <span class="record-id">{{ record.id }}</span>
          <span class="record-status" :class="record.status">{{ record.statusText }}</span>
        </div>
        <div class="record-info">
          <p>设备：{{ record.device }}</p>
          <p>时间：{{ record.time }}</p>
          <p>巡检人：{{ record.inspector }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const startDate = ref('')
const endDate = ref('')

// 模拟数据
const records = ref([
  {
    id: 'INSP-2024-001',
    device: '设备A',
    time: '2024-05-11 10:30',
    inspector: '张三',
    status: 'normal',
    statusText: '正常'
  },
  {
    id: 'INSP-2024-002',
    device: '设备B',
    time: '2024-05-11 11:15',
    inspector: '李四',
    status: 'warning',
    statusText: '异常'
  }
])

const handleFilter = () => {
  // TODO: 实现筛选逻辑
  console.log('筛选日期:', startDate.value, endDate.value)
}
</script>

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/records.css');
@import url('https://fonts.googleapis.com/icon?family=Material+Icons');

.records-page {
  min-height: 100vh;
  background: #f7f7f7;
}

.header {
  background: #4CAF50;
  color: white;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  position: relative;
}

.back-btn {
  background: transparent;
  border: none;
  color: white;
  font-size: 16px;
  cursor: pointer;
  position: absolute;
  left: 20px;
}

h1 {
  width: 100%;
  text-align: center;
  margin: 0;
  font-size: 18px;
}

.filter-bar {
  padding: 15px;
  background: white;
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.date-input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filter-btn {
  padding: 8px 15px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.records-list {
  padding: 0 15px;
}

.record-item {
  background: white;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.record-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.record-id {
  font-weight: bold;
  color: #333;
}

.record-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.record-status.normal {
  background: #e8f5e9;
  color: #4CAF50;
}

.record-status.warning {
  background: #fff3e0;
  color: #ff9800;
}

.record-info p {
  margin: 5px 0;
  color: #666;
}
</style> 