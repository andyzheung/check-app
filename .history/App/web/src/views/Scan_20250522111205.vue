<template>
  <div class="scan-container">
    <div class="scan-header">
      <a class="back-button" @click.prevent="router.push('/home')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>扫码巡检</h1>
    </div>
    <div class="scan-body">
      <div class="scan-frame">
        <div class="scan-line"></div>
      </div>
      <div class="scan-tip">
        <div class="tip-main">将二维码放入框内</div>
        <div class="tip-sub">自动扫描</div>
      </div>
    </div>
    <div class="scan-bottom">
      <button class="scan-btn" @click="handleScan" :disabled="loading">
        <span class="material-icons">qr_code_scanner</span>
        <span>{{ loading ? '加载中...' : '开始扫描' }}</span>
      </button>
      <button class="manual-btn" @click="showManualInput = true" :disabled="loading">
        <span class="material-icons">edit</span>
        <span>手动输入</span>
      </button>
    </div>

    <!-- 手动输入对话框 -->
    <div v-if="showManualInput" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h3>手动输入编号</h3>
          <button class="close-btn" @click="showManualInput = false">
            <span class="material-icons">close</span>
          </button>
        </div>
        <div class="modal-body">
          <input 
            v-model="manualCode" 
            type="text" 
            placeholder="请输入巡检点编号"
            @keyup.enter="handleManualInput"
            :disabled="loading"
          >
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="showManualInput = false" :disabled="loading">取消</button>
          <button class="confirm-btn" @click="handleManualInput" :disabled="loading">
            {{ loading ? '加载中...' : '确认' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getInspectionPoint } from '@/api/inspection'

const router = useRouter()
const showManualInput = ref(false)
const manualCode = ref('')
const loading = ref(false)

// 检查是否在Android环境中
const isAndroid = () => {
  return window.android !== undefined
}

// 调用原生扫码功能
const handleScan = () => {
  if (isAndroid()) {
    window.android.startScan()
  } else {
    console.log('非Android环境，使用模拟数据')
    // 模拟扫码结果
    setTimeout(() => {
      handleScanResult('TEST-001')
    }, 1000)
  }
}

// 处理扫码结果
const handleScanResult = async (code) => {
  try {
    loading.value = true
    const response = await getInspectionPoint(code)
    const point = response.data.data
    
    // 跳转到巡检表单页面
    router.push({
      path: '/inspection-form',
      query: { 
        code: point.code,
        pointId: point.id,
        pointName: point.name
      }
    })
  } catch (error) {
    console.error('获取巡检点信息失败:', error)
    alert('获取巡检点信息失败，请重试')
  } finally {
    loading.value = false
  }
}

// 处理手动输入
const handleManualInput = async () => {
  if (!manualCode.value) {
    alert('请输入巡检点编号')
    return
  }
  await handleScanResult(manualCode.value)
  showManualInput.value = false
  manualCode.value = ''
}

// 注册JSBridge回调
if (isAndroid()) {
  window.handleScanResult = handleScanResult
}
</script>

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/scan.css');

.scan-bottom {
  display: flex;
  justify-content: space-around;
  padding: 20px;
  background: #fff;
  border-top: 1px solid #eee;
}

.scan-btn, .manual-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 20px;
  border: none;
  background: none;
  color: #333;
  cursor: pointer;
}

.scan-btn .material-icons, .manual-btn .material-icons {
  font-size: 24px;
  margin-bottom: 5px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: #fff;
  border-radius: 8px;
  width: 80%;
  max-width: 400px;
}

.modal-header {
  padding: 15px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-body {
  padding: 20px;
}

.modal-body input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.modal-footer {
  padding: 15px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.modal-footer button {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn {
  background: #f5f5f5;
  color: #333;
}

.confirm-btn {
  background: #1890ff;
  color: #fff;
}

.close-btn {
  background: none;
  border: none;
  cursor: pointer;
  color: #999;
}
</style> 