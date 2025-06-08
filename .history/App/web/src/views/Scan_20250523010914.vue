<template>
  <div class="scan-container">
    <!-- 返回按钮 -->
    <button class="back-btn" @click="router.back()">
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
      </div>
      <div class="scan-tip">
        <div class="tip-main">将二维码放入框内</div>
        <div class="tip-sub">自动扫描</div>
      </div>
      <div class="input-group">
        <input type="text" v-model="code" placeholder="请输入巡检点编号" @keyup.enter="handleCode" />
        <button @click="handleCode">确定</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const code = ref('')
const torchOn = ref(false)

function handleCode() {
  if (!code.value) {
    window.alert('请输入巡检点编号')
    return
  }
  router.push({
    path: '/inspection-form',
    query: { code: code.value }
  })
}

function toggleTorch() {
  torchOn.value = !torchOn.value
  // 这里只做UI切换，实际扫码功能可后续对接
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
</style> 