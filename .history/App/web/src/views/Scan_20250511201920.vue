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
    <div class="scan-bottom-btn">
      <span class="material-icons">emoji_objects</span>
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

<style>
@import url('@/assets/css/common.css');
@import url('@/assets/css/scan.css');
@import url('https://fonts.googleapis.com/icon?family=Material+Icons');
</style> 