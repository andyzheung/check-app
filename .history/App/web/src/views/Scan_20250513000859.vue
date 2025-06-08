<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gray-100">
    <div class="w-full max-w-xs bg-white rounded-2xl shadow-lg p-8 border border-gray-100 flex flex-col items-center">
      <div class="w-32 h-32 bg-gray-200 rounded-2xl flex items-center justify-center mb-6">
        <span class="material-icons text-5xl text-gray-400">qr_code_scanner</span>
      </div>
      <div class="text-lg font-semibold text-gray-800 mb-2">请对准二维码进行扫描</div>
      <button class="w-full py-3 bg-blue-500 text-white rounded-lg font-semibold shadow-md hover:bg-blue-600 transition-colors mt-4">开始扫码</button>
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
.material-icons {
  font-family: 'Material Icons';
  font-style: normal;
  font-weight: normal;
  font-size: 24px;
  line-height: 1;
  letter-spacing: normal;
  text-transform: none;
  display: inline-block;
  direction: ltr;
  -webkit-font-feature-settings: 'liga';
  -webkit-font-smoothing: antialiased;
}
</style> 