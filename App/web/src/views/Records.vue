<template>
  <div class="records-container">
    <!-- 筛选区域 -->
    <div class="filter-section">
      <div class="filter-row">
        <input type="date" v-model="filters.startDate" class="date-input" />
        <span class="separator">-</span>
        <input type="date" v-model="filters.endDate" class="date-input" />
      </div>
      <div class="filter-row">
        <select v-model="filters.areaId" class="filter-select">
          <option :value="null">全部区域</option>
          <option v-for="area in areaList" :key="area.id" :value="area.id">{{ area.name }}</option>
        </select>
        <select v-model="filters.status" class="filter-select">
          <option :value="null">全部状态</option>
          <option value="COMPLETED">正常</option>
          <option value="EXCEPTION">异常</option>
        </select>
      </div>
      <div class="filter-row">
        <div class="search-container">
          <input type="text" v-model="filters.keyword" placeholder="搜索关键词" class="search-input" />
          <button @click="applyFilters" class="search-button">
            <span class="material-icons">search</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 记录列表 -->
    <div class="list-container">
      <div v-if="loading" class="state-info">加载中...</div>
      <div v-else-if="records.length === 0" class="state-info">
        暂无记录数据
      </div>
      <div v-else class="records-list">
        <div v-for="record in records" :key="record.id" class="record-card" @click="viewRecord(record.id)">
          <div class="record-header">
            <h3 class="record-area">{{ record.areaName }}</h3>
            <div class="record-status" :class="getStatusClass(record.status)">
              {{ formatStatus(record.status) }}
            </div>
          </div>
          <div class="record-time">{{ formatTime(record.inspectionTime) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request';

const router = useRouter();
const loading = ref(false);
const records = ref([]);
const areaList = ref([]);
const filters = reactive({
  startDate: null,
  endDate: null,
  areaId: null,
  status: null,
  keyword: '',
});

const fetchRecords = async () => {
  loading.value = true;
  try {
    const params = {
      page: 1,
      size: 100, // Fetch all for now
      ...filters,
    };
    const res = await request.get('/records', { params });
    if (res.data && res.data.records) {
      records.value = res.data.records;
    }
  } catch (error) {
    console.error("Failed to fetch records:", error);
  } finally {
    loading.value = false;
  }
};

const fetchAreas = async () => {
  try {
    const res = await request.get('/areas', { params: { size: 100 } });
    if (res.data && res.data.records) {
      areaList.value = res.data.records;
    }
  } catch (error) {
    console.error("Failed to fetch areas:", error);
  }
};

onMounted(() => {
  fetchRecords();
  fetchAreas();
});

const applyFilters = () => {
  fetchRecords();
};

const viewRecord = (id) => {
  router.push(`/record-detail/${id}`);
};

const formatTime = (time) => {
  if (!time) return '';
  return new Date(time).toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
};

const formatStatus = (status) => {
  const statusMap = {
    'COMPLETED': '正常',
    'EXCEPTION': '异常'
  };
  return statusMap[status] || '未知';
};

const getStatusClass = (status) => {
  return {
    'status-completed': status === 'COMPLETED',
    'status-exception': status === 'EXCEPTION',
  };
};
</script>

<style scoped>
.records-container {
  background: #f5f5f5;
}

/* 筛选区域 */
.filter-section {
  background: white;
  margin: 16px 16px 16px;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.filter-row {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
  align-items: center;
}

.filter-row:last-child {
  margin-bottom: 0;
}

.date-input, .filter-select {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  background: white;
}

.separator {
  color: #666;
  font-weight: 500;
}

.search-container {
  display: flex;
  flex: 1;
  gap: 8px;
}

.search-input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
}

.search-button {
  padding: 10px 16px;
  background: #1890ff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-button:hover {
  background: #40a9ff;
}

/* 列表容器 */
.list-container {
  padding: 0 16px 80px;
}

.state-info {
  text-align: center;
  padding: 40px 0;
  color: #999;
  font-size: 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.records-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 记录卡片 */
.record-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.2s;
}

.record-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-1px);
}

.record-card:active {
  transform: translateY(0);
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.record-area {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.record-time {
  font-size: 14px;
  color: #666;
}

/* 状态样式 */
.record-status {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

.status-completed {
  background: #f6ffed;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}

.status-exception {
  background: #fff2f0;
  color: #ff4d4f;
  border: 1px solid #ffccc7;
}
</style> 