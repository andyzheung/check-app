<template>
  <div class="scan-container">
    <!-- 返回按钮 -->
    <button class="back-btn" @click="goHome">
      <span class="material-icons">arrow_back</span>
    </button>
    <div class="scan-header">
      <h1>扫码巡检</h1>
    </div>
    <div class="scan-body">
      <div class="scan-frame">
        <div class="scan-line"></div>
        <!-- 手电筒按钮 -->
        <button class="torch-btn" @click="toggleTorch">
          <span class="material-icons">{{ torchOn ? 'flashlight_on' : 'flashlight_off' }}</span>
        </button>
        
        <!-- Android扫码按钮 -->
        <button class="camera-btn" @click="startNativeScan" v-if="isAndroid">
          <span class="material-icons">camera_alt</span>
        </button>
      </div>
      <div class="scan-tip">
        <div class="tip-main">将二维码放入框内</div>
        <div class="tip-sub">自动扫描</div>
      </div>
      <div class="input-group">
        <input type="text" v-model="code" placeholder="请输入区域编号(如：AREAB001)" @keyup.enter="handleCode" />
        <button @click="handleCode">确定</button>
      </div>
      <!-- 添加区域信息显示 -->
      <div v-if="areaInfo" class="area-info">
        <h3>区域信息</h3>
        <div class="info-item">
          <span class="label">区域编号：</span>
          <span>{{ areaInfo.areaCode }}</span>
        </div>
        <div class="info-item">
          <span class="label">区域名称：</span>
          <span>{{ areaInfo.areaName }}</span>
        </div>
        <div class="info-item">
          <span class="label">区域类型：</span>
          <span>{{ areaInfo.areaTypeName }}</span>
        </div>
        <div class="info-item">
          <span class="label">状态：</span>
          <span>{{ areaInfo.status === 'active' ? '活跃' : '未激活' }}</span>
        </div>
        <button class="start-btn" @click="startInspection" :disabled="areaInfo.status !== 'active'">
          开始巡检
        </button>
      </div>
      <!-- 添加错误提示 -->
      <div v-if="error" class="error-message">
        {{ error }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getAreaByCode } from '../api/inspection'

const router = useRouter()
const code = ref('')
const torchOn = ref(false)
const areaInfo = ref(null)
const error = ref('')

// 检测是否为Android环境
const isAndroid = ref(window.android !== undefined)

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

function toggleTorch() {
  torchOn.value = !torchOn.value
}

function goHome() {
  router.push('/home')
}

// 启动原生扫码功能（Android）
function startNativeScan() {
  if (window.android && window.android.startScan) {
    try {
      window.android.startScan()
    } catch (error) {
      console.error('启动扫码失败:', error)
      error.value = '启动扫码功能失败'
    }
  }
}

// 处理Android扫码结果的全局回调函数
window.handleScanResult = async (scanResult) => {
  console.log('收到扫码结果:', scanResult)
  
  try {
    error.value = ''
    
    // 尝试解析二维码数据
    if (scanResult.startsWith('{') && scanResult.endsWith('}')) {
      // JSON格式的二维码，解析区域编码
      const qrData = JSON.parse(scanResult)
      if (qrData.areaCode) {
        code.value = qrData.areaCode
        
        // 验证二维码并获取区域信息
        const response = await getAreaByCode(qrData.areaCode)
        if (response.data) {
          areaInfo.value = response.data
        }
      } else {
        error.value = '二维码格式错误，缺少区域编码'
      }
    } else {
      // 简单文本格式，直接作为区域编码处理
      code.value = scanResult
      const response = await getAreaByCode(scanResult)
      if (response.data) {
        areaInfo.value = response.data
      }
    }
  } catch (err) {
    console.error('处理扫码结果失败:', err)
    error.value = err.response?.data?.message || '处理扫码结果失败'
    areaInfo.value = null
  }
}
</script>

<style scoped>
.scan-container {
  min-height: 100vh;
  background: #f5f5f5;
  position: relative;
}

.back-btn {
  position: absolute;
  top: 18px;
  left: 12px;
  background: none;
  border: none;
  color: #888;
  font-size: 28px;
  z-index: 10;
  cursor: pointer;
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
  background: #888;
  border-radius: 18px;
  margin-bottom: 18px;
  position: relative;
  box-shadow: 0 4px 24px rgba(0,0,0,0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px dashed #fff;
}

.scan-line {
  position: absolute;
  left: 16px;
  right: 16px;
  height: 3px;
  background-color: #3b82f6;
  top: 60px;
  border-radius: 2px;
  animation: scan 2s linear infinite;
}

@keyframes scan {
  0% { top: 60px; }
  50% { top: 200px; }
  100% { top: 60px; }
}

.torch-btn {
  position: absolute;
  right: 12px;
  bottom: 12px;
  background: rgba(255,255,255,0.85);
  border: none;
  border-radius: 50%;
  width: 38px;
  height: 38px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fbc02d;
  font-size: 26px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
}

.camera-btn {
  position: absolute;
  left: 50%;
  bottom: 12px;
  transform: translateX(-50%);
  background: rgba(33, 150, 243, 0.9);
  border: none;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 28px;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
  cursor: pointer;
  transition: transform 0.2s;
}

.camera-btn:active {
  transform: translateX(-50%) scale(0.95);
}

.scan-tip {
  margin-top: 8px;
  text-align: center;
}

.tip-main {
  font-size: 17px;
  color: #222;
  margin-bottom: 2px;
}

.tip-sub {
  font-size: 14px;
  color: #888;
}

.input-group {
  display: flex;
  gap: 12px;
  margin-top: 18px;
  width: 260px;
}

.input-group input {
  flex: 1;
  height: 44px;
  padding: 0 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  background: #fff;
}

.input-group button {
  width: 80px;
  background: #1989fa;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.input-group button:active {
  background: #1976d2;
}

.area-info {
  margin-top: 24px;
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  width: 260px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.area-info h3 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 18px;
}

.info-item {
  margin-bottom: 8px;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.info-item .label {
  color: #666;
  width: 80px;
}

.error-message {
  margin-top: 12px;
  color: #f44336;
  font-size: 14px;
}

.start-btn {
  width: 100%;
  height: 40px;
  margin-top: 16px;
  background: #1989fa;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.start-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style> 