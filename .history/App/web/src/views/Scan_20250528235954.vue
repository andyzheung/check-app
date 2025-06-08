<template>
  <div class="scan-container">
    <div class="scan-header">
      <h1>扫码巡检</h1>
    </div>
    <div class="scan-body">
      <div class="scan-frame">
        <div class="scan-area">
          <input type="text" v-model="code" placeholder="请输入区域编号" />
          <button @click="handleCode">确认</button>
        </div>
      </div>
      <div class="area-info" v-if="areaInfo">
        <div class="info-item">
          <span class="label">区域编号：</span>
          <span class="value">{{ areaInfo.areaCode }}</span>
        </div>
        <div class="info-item">
          <span class="label">区域名称：</span>
          <span class="value">{{ areaInfo.areaName }}</span>
        </div>
        <div class="info-item">
          <span class="label">区域类型：</span>
          <span class="value">{{ areaInfo.areaTypeName }}</span>
        </div>
        <button class="start-btn" @click="startInspection">开始巡检</button>
      </div>
      <div class="error-msg" v-if="error">{{ error }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getAreaByCode } from '../api/inspection'

const router = useRouter()
const code = ref('')
const areaInfo = ref(null)
const error = ref('')

async function handleCode() {
  if (!code.value) {
    error.value = '请输入区域编号'
    return
  }
  
  try {
    error.value = ''
    const response = await getAreaByCode(code.value)
    if (response.data) {
      areaInfo.value = response.data
    }
  } catch (err) {
    error.value = err.response?.data?.message || '获取区域信息失败'
    areaInfo.value = null
  }
}

function startInspection() {
  if (areaInfo.value) {
    router.push({
      path: '/inspection-form',
      query: { 
        code: areaInfo.value.areaCode,
        areaId: areaInfo.value.id
      }
    })
  }
}
</script>

<style scoped>
.scan-container {
  min-height: 100vh;
  background: #f5f5f5;
  position: relative;
}

.scan-header {
  padding: 28px 0 18px 0;
  text-align: center;
  font-size: 22px;
  font-weight: 700;
  color: var(--primary-color, #2196F3);
  letter-spacing: 2px;
}

.scan-body {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 24px;
}

.scan-frame {
  width: 260px;
  height: 260px;
  background: #fff;
  border-radius: 18px;
  margin-bottom: 18px;
  position: relative;
  box-shadow: 0 4px 24px rgba(0,0,0,0.08);
  display: flex;
  align-items: center;
  justify-content: center;
}

.scan-area {
  width: 80%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.scan-area input {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
}

.scan-area button {
  padding: 12px;
  background: var(--primary-color, #2196F3);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

.area-info {
  background: white;
  padding: 20px;
  border-radius: 12px;
  width: 260px;
  margin-top: 20px;
}

.info-item {
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
}

.label {
  color: #666;
}

.value {
  color: #333;
  font-weight: 500;
}

.start-btn {
  width: 100%;
  padding: 12px;
  background: var(--primary-color, #2196F3);
  color: white;
  border: none;
  border-radius: 8px;
  margin-top: 16px;
  font-size: 16px;
  cursor: pointer;
}

.error-msg {
  color: #f44336;
  margin-top: 12px;
  text-align: center;
}
</style> 