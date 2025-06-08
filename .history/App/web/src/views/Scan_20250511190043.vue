<template>
  <div class="scan-page">
    <header class="header">
      <button class="back-btn" @click="router.back()">返回</button>
      <h1>扫码巡检</h1>
    </header>
    
    <div class="scan-area">
      <div class="scan-box">
        <div class="scan-line"></div>
      </div>
      <p class="scan-tip">请将二维码/条形码放入框内</p>
    </div>
    
    <div class="action-buttons">
      <button class="action-btn" @click="handleScan">
        <i class="icon-scan"></i>
        开始扫描
      </button>
      <button class="action-btn" @click="handleManual">
        <i class="icon-manual"></i>
        手动输入
      </button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const handleScan = () => {
  // TODO: 调用原生扫码功能
  console.log('开始扫描')
}

const handleManual = () => {
  // TODO: 显示手动输入对话框
  console.log('手动输入')
}
</script>

<style scoped>
.scan-page {
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

.scan-area {
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.scan-box {
  width: 250px;
  height: 250px;
  border: 2px solid #4CAF50;
  position: relative;
  margin-bottom: 20px;
}

.scan-line {
  position: absolute;
  width: 100%;
  height: 2px;
  background: #4CAF50;
  animation: scan 2s linear infinite;
}

@keyframes scan {
  0% { top: 0; }
  100% { top: 100%; }
}

.scan-tip {
  color: #666;
  text-align: center;
}

.action-buttons {
  padding: 20px;
  display: flex;
  gap: 20px;
}

.action-btn {
  flex: 1;
  padding: 15px;
  background: white;
  border: 1px solid #4CAF50;
  color: #4CAF50;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  cursor: pointer;
}

.action-btn:hover {
  background: #4CAF50;
  color: white;
}

.icon-scan, .icon-manual {
  font-size: 24px;
}
</style> 