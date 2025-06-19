<template>
  <div class="records-container">
    <div class="filter-card">
      <div class="filter-row">
        <input type="date" v-model="filters.startDate" class="date-input" />
        <span class="separator">-</span>
        <input type="date" v-model="filters.endDate" class="date-input" />
      </div>
      <div class="filter-row">
        <select v-model="filters.areaId">
          <option :value="null">全部区域</option>
          <option v-for="area in areaList" :key="area.id" :value="area.id">{{ area.name }}</option>
        </select>
        <select v-model="filters.status">
          <option :value="null">全部状态</option>
          <option value="COMPLETED">正常</option>
          <option value="EXCEPTION">异常</option>
        </select>
      </div>
      <div class="filter-row">
        <input type="text" v-model="filters.keyword" placeholder="搜索关键词" class="search-input" />
        <button @click="applyFilters" class="search-button">
          <span class="material-icons">search</span>
        </button>
      </div>
    </div>

    <div class="list-container">
      <div v-if="loading" class="state-info">加载中...</div>
      <div v-else-if="records.length === 0" class="state-info">
        暂无记录数据
      </div>
      <div v-else class="records-list">
        <div v-for="record in records" :key="record.id" class="record-card" @click="viewRecord(record.id)">
          <div class="record-info">
            <div class="record-area">{{ record.areaName }}</div>
            <div class="record-time">{{ formatTime(record.inspectionTime) }}</div>
          </div>
          <div class="record-status" :class="getStatusClass(record.status)">
            {{ formatStatus(record.status) }}
          </div>
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

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/records.css');

.record-status {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: 500;
  white-space: nowrap;
}

.status-normal {
  color: #4CAF50;
}

.status-abnormal {
  background: #FFEBEE;
  color: #F44336;
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  width: fit-content;
}
.pagination-wrapper {
  margin: 20px 0;
  padding: 0 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}
.pagination-info {
  color: #666;
  font-size: 14px;
}
:deep(.van-pagination) {
  --van-pagination-height: 40px;
  --van-pagination-item-width: 40px;
  --van-pagination-font-size: 16px;
}
:deep(.van-pagination__item) {
  margin: 0 4px;
}
:deep(.van-pagination__item--active) {
  background-color: #1989fa;
  color: white;
}
:deep(.van-pagination__prev),
:deep(.van-pagination__next) {
  padding: 0 12px;
  min-width: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}
.record-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  padding: 16px;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: transform 0.2s;
}

.record-card:active {
  transform: scale(0.98);
}

.empty-records {
  text-align: center;
  padding: 40px 0;
  color: #999;
  font-size: 16px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  margin: 0 16px;
}

.record-id {
  font-size: 14px;
  color: #888;
  margin-left: 15px;
}
</style> 