<template>
  <div class="detail-container">
    <!-- 顶部导航 -->
    <div class="detail-header">
      <a class="back-button" @click.prevent="router.push('/records')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>巡检记录详情</h1>
    </div>

    <div class="detail-content" v-if="recordDetail">
      <!-- 区域信息 -->
      <div class="detail-section">
        <div class="section-title">区域信息</div>
        <div class="info-list">
          <div class="info-item">
            <span class="info-label">区域编号：</span>
            <span class="info-value">{{ recordDetail.areaInfo?.areaCode }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">区域名称：</span>
            <span class="info-value">{{ recordDetail.areaInfo?.areaName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">区域类型：</span>
            <span class="info-value">{{ recordDetail.areaInfo?.areaTypeName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">区域状态：</span>
            <span class="info-value" :class="recordDetail.areaInfo?.status">
              {{ recordDetail.areaInfo?.status === 'active' ? '活跃' : '未激活' }}
            </span>
          </div>
        </div>
      </div>

      <!-- 巡检基本信息 -->
      <div class="detail-section">
        <div class="section-title">巡检信息</div>
        <div class="info-list">
          <div class="info-item">
            <span class="info-label">记录编号：</span>
            <span class="info-value">{{ recordDetail.recordNo }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">巡检人员：</span>
            <span class="info-value">{{ recordDetail.inspectorInfo?.realName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">开始时间：</span>
            <span class="info-value">{{ formatDateTime(recordDetail.startTime) }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">结束时间：</span>
            <span class="info-value">{{ formatDateTime(recordDetail.endTime) }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">巡检状态：</span>
            <span class="info-value" :class="recordDetail.status">
              {{ recordDetail.status === 'normal' ? '正常' : '异常' }}
            </span>
          </div>
        </div>
      </div>

      <!-- 环境巡检项 -->
      <div class="detail-section">
        <div class="section-title">环境巡检</div>
        <div class="item-list">
          <div class="inspection-item" v-for="item in recordDetail.environmentItems" :key="item.id">
            <div class="item-header">
              <span class="item-name">{{ item.name }}</span>
              <span class="item-status" :class="item.status">
                {{ item.status === 'normal' ? '正常' : '异常' }}
              </span>
            </div>
            <div class="item-remark" v-if="item.remark">
              备注：{{ item.remark }}
            </div>
          </div>
        </div>
      </div>

      <!-- 安全巡检项 -->
      <div class="detail-section">
        <div class="section-title">安全巡检</div>
        <div class="item-list">
          <div class="inspection-item" v-for="item in recordDetail.securityItems" :key="item.id">
            <div class="item-header">
              <span class="item-name">{{ item.name }}</span>
              <span class="item-status" :class="item.status">
                {{ item.status === 'normal' ? '正常' : '异常' }}
              </span>
            </div>
            <div class="item-remark" v-if="item.remark">
              备注：{{ item.remark }}
            </div>
          </div>
        </div>
      </div>

      <!-- 备注信息 -->
      <div class="detail-section" v-if="recordDetail.remark">
        <div class="section-title">备注信息</div>
        <div class="remark-content">
          {{ recordDetail.remark }}
        </div>
      </div>
    </div>

    <!-- 加载中 -->
    <div class="loading" v-else>
      <span class="material-icons loading-icon">sync</span>
      加载中...
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getRecordDetail } from '@/api/inspection'

const router = useRouter()
const route = useRoute()
const recordDetail = ref(null)

// 格式化日期时间
function formatDateTime(dateTime) {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

// 加载记录详情
async function loadRecordDetail() {
  try {
    const id = route.params.id
    if (!id) {
      throw new Error('缺少记录ID')
    }
    const res = await getRecordDetail(id)
    if (res.code === 200 || res.code === 0) {
      recordDetail.value = res.data
    } else {
      throw new Error(res.message || '获取记录详情失败')
    }
  } catch (err) {
    console.error('获取记录详情失败:', err)
    window.alert(err.message || '获取记录详情失败')
    router.push('/records')
  }
}

// 页面加载时获取记录详情
onMounted(() => {
  loadRecordDetail()
})
</script>

<style scoped>
.detail-container {
  max-width: 420px;
  margin: 0 auto;
  padding: 0 0 80px 0;
}

.detail-header {
  padding: 28px 0 18px 0;
  text-align: center;
  font-size: 22px;
  font-weight: 700;
  color: var(--primary-color, #2196F3);
  letter-spacing: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.detail-header .back-button {
  position: absolute;
  left: 18px;
  top: 32px;
  color: #888;
  text-decoration: none;
}

.detail-content {
  flex: 1;
  padding: 20px;
  padding-bottom: 70px;
  overflow-y: auto;
}

.detail-section {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  margin-bottom: 22px;
  padding: 20px 18px;
}

.section-title {
  font-size: 17px;
  font-weight: 600;
  margin-bottom: 14px;
  color: #222;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-label {
  color: #666;
  width: 80px;
  flex-shrink: 0;
}

.info-value {
  color: #333;
  flex: 1;
}

.info-value.active {
  color: #4CAF50;
}

.info-value.inactive {
  color: #F44336;
}

.item-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.inspection-item {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 12px;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.item-name {
  font-weight: 500;
  color: #333;
}

.item-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.item-status.normal {
  background: #E8F5E9;
  color: #4CAF50;
}

.item-status.abnormal {
  background: #FFEBEE;
  color: #F44336;
}

.item-remark {
  font-size: 14px;
  color: #666;
  margin-top: 8px;
}

.remark-content {
  color: #666;
  line-height: 1.5;
  white-space: pre-wrap;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #999;
}

.loading-icon {
  font-size: 32px;
  margin-bottom: 12px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style> 