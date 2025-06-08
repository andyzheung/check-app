<template>
  <div class="record-list">
    <!-- 搜索表单 -->
    <van-form class="search-form">
      <van-cell-group inset>
        <van-field
          v-model="searchForm.keyword"
          label="关键字"
          placeholder="请输入关键字"
          clearable
        />
        <van-field
          v-model="searchForm.status"
          label="状态"
          placeholder="请选择状态"
          readonly
          @click="showStatusPicker = true"
        />
        <van-field
          v-model="searchForm.dateRange"
          label="日期范围"
          placeholder="请选择日期范围"
          readonly
          @click="showDatePicker = true"
        />
      </van-cell-group>
      <div class="search-buttons">
        <van-button type="primary" @click="handleSearch">搜索</van-button>
        <van-button plain @click="handleReset">重置</van-button>
      </div>
    </van-form>

    <!-- 记录列表 -->
    <van-list
      v-model:loading="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="onLoad"
    >
      <van-cell-group inset v-for="item in records" :key="item.id">
        <van-cell :title="item.areaName" :label="item.recordNo">
          <template #right-icon>
            <van-tag :type="getStatusType(item.status)">{{ getStatusText(item.status) }}</van-tag>
          </template>
        </van-cell>
        <van-cell title="巡检人" :value="item.inspectorName" />
        <van-cell title="巡检时间" :value="formatDateTime(item.inspectionTime)" />
      </van-cell-group>
    </van-list>

    <!-- 分页器 -->
    <div class="pagination">
      <van-pagination
        v-model="currentPage"
        :total-items="total"
        :items-per-page="pageSize"
        :show-page-size="3"
        force-ellipses
        @change="handlePageChange"
      />
    </div>

    <!-- 状态选择器 -->
    <van-popup v-model:show="showStatusPicker" position="bottom">
      <van-picker
        :columns="statusOptions"
        @confirm="onStatusConfirm"
        @cancel="showStatusPicker = false"
      />
    </van-popup>

    <!-- 日期选择器 -->
    <van-popup v-model:show="showDatePicker" position="bottom">
      <van-date-picker
        type="date-range"
        :min-date="minDate"
        :max-date="maxDate"
        @confirm="onDateConfirm"
        @cancel="showDatePicker = false"
      />
    </van-popup>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getRecordList } from '@/api/inspection';
import { showToast } from 'vant';
import dayjs from 'dayjs';

// 状态数据
const statusOptions = [
  { text: '全部', value: '' },
  { text: '正常', value: 'normal' },
  { text: '异常', value: 'abnormal' }
];

// 分页数据
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const records = ref([]);
const loading = ref(false);
const finished = ref(false);

// 搜索表单数据
const searchForm = ref({
  keyword: '',
  status: '',
  dateRange: '',
  startDate: '',
  endDate: ''
});

// 弹窗控制
const showStatusPicker = ref(false);
const showDatePicker = ref(false);

// 日期选择器配置
const minDate = new Date(new Date().getFullYear() - 1, 0, 1);
const maxDate = new Date();

// 获取记录列表
const fetchRecords = async () => {
  try {
    loading.value = true;
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchForm.value.keyword,
      status: searchForm.value.status,
      startDate: searchForm.value.startDate,
      endDate: searchForm.value.endDate
    };

    const res = await getRecordList(params);
    records.value = res.data.records;
    total.value = res.data.total;
    finished.value = currentPage.value * pageSize.value >= total.value;
  } catch (error) {
    showToast('获取记录列表失败');
    console.error('获取记录列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchRecords();
};

// 重置
const handleReset = () => {
  searchForm.value = {
    keyword: '',
    status: '',
    dateRange: '',
    startDate: '',
    endDate: ''
  };
  currentPage.value = 1;
  fetchRecords();
};

// 页码变化
const handlePageChange = (page) => {
  currentPage.value = page;
  fetchRecords();
};

// 状态选择确认
const onStatusConfirm = (value) => {
  searchForm.value.status = value.value;
  showStatusPicker.value = false;
};

// 日期选择确认
const onDateConfirm = ([start, end]) => {
  searchForm.value.startDate = dayjs(start).format('YYYY-MM-DD');
  searchForm.value.endDate = dayjs(end).format('YYYY-MM-DD');
  searchForm.value.dateRange = `${searchForm.value.startDate} 至 ${searchForm.value.endDate}`;
  showDatePicker.value = false;
};

// 格式化日期时间
const formatDateTime = (datetime) => {
  return dayjs(datetime).format('YYYY-MM-DD HH:mm:ss');
};

// 获取状态类型
const getStatusType = (status) => {
  return status === 'normal' ? 'success' : 'danger';
};

// 获取状态文本
const getStatusText = (status) => {
  return status === 'normal' ? '正常' : '异常';
};

// 加载更多
const onLoad = () => {
  currentPage.value++;
  fetchRecords();
};

// 初始化
onMounted(() => {
  fetchRecords();
});
</script>

<style scoped>
.record-list {
  padding: 16px;
}

.search-form {
  margin-bottom: 16px;
}

.search-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 16px;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

:deep(.van-cell-group) {
  margin-bottom: 12px;
}

:deep(.van-tag) {
  margin-left: 8px;
}
</style> 