<template>
  <div class="about-container">
    <!-- 顶部导航 -->
    <div class="about-header">
      <a class="back-button" @click.prevent="router.push('/profile')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>关于</h1>
    </div>

    <!-- 应用信息 -->
    <div class="about-content">
      <div class="app-info">
        <div class="app-logo">
          <img src="@/assets/logo.png" alt="Logo">
        </div>
        <div class="app-name">巡检系统</div>
        <div class="app-version">版本 {{ version }}</div>
      </div>

      <!-- 功能介绍 -->
      <div class="feature-list">
        <div class="section-title">主要功能</div>
        <div class="feature-item">
          <span class="material-icons">qr_code_scanner</span>
          <div class="feature-info">
            <div class="feature-name">扫码巡检</div>
            <div class="feature-desc">扫描区域二维码，快速开始巡检</div>
          </div>
        </div>
        <div class="feature-item">
          <span class="material-icons">assignment</span>
          <div class="feature-info">
            <div class="feature-name">巡检记录</div>
            <div class="feature-desc">查看和管理巡检历史记录</div>
          </div>
        </div>
        <div class="feature-item">
          <span class="material-icons">notifications</span>
          <div class="feature-info">
            <div class="feature-name">消息通知</div>
            <div class="feature-desc">接收巡检任务和系统通知</div>
          </div>
        </div>
      </div>

      <!-- 帮助信息 -->
      <div class="help-list">
        <div class="section-title">帮助与支持</div>
        <div class="help-item" @click="handleHelp('guide')">
          <span class="material-icons">help_outline</span>
          <span>使用指南</span>
          <span class="material-icons">chevron_right</span>
        </div>
        <div class="help-item" @click="handleHelp('feedback')">
          <span class="material-icons">feedback</span>
          <span>问题反馈</span>
          <span class="material-icons">chevron_right</span>
        </div>
        <div class="help-item" @click="handleHelp('contact')">
          <span class="material-icons">support_agent</span>
          <span>联系我们</span>
          <span class="material-icons">chevron_right</span>
        </div>
      </div>

      <!-- 版权信息 -->
      <div class="copyright">
        © {{ new Date().getFullYear() }} 巡检系统
        <br>
        All Rights Reserved
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'

const router = useRouter()
const version = ref('1.0.0')

// 处理帮助项点击
function handleHelp(type) {
  switch (type) {
    case 'guide':
      showToast('正在开发中...')
      break
    case 'feedback':
      showToast('正在开发中...')
      break
    case 'contact':
      showToast('正在开发中...')
      break
  }
}
</script>

<style scoped>
.about-container {
  max-width: 420px;
  margin: 0 auto;
  min-height: 100vh;
  background: #f8f9fa;
}

.about-header {
  padding: 28px 0 18px 0;
  text-align: center;
  font-size: 22px;
  font-weight: 700;
  color: var(--primary-color, #2196F3);
  letter-spacing: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.about-header .back-button {
  position: absolute;
  left: 18px;
  top: 32px;
  color: #888;
  text-decoration: none;
}

.about-content {
  padding: 20px;
}

.app-info {
  text-align: center;
  margin-bottom: 30px;
}

.app-logo {
  width: 80px;
  height: 80px;
  margin: 0 auto 16px;
}

.app-logo img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.app-name {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.app-version {
  font-size: 14px;
  color: #666;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.feature-list {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.feature-item {
  display: flex;
  align-items: flex-start;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.feature-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.feature-item .material-icons {
  color: var(--primary-color, #2196F3);
  font-size: 24px;
  margin-right: 12px;
}

.feature-info {
  flex: 1;
}

.feature-name {
  font-size: 15px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.feature-desc {
  font-size: 13px;
  color: #666;
}

.help-list {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.help-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.help-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.help-item .material-icons:first-child {
  color: var(--primary-color, #2196F3);
  margin-right: 12px;
}

.help-item span:not(.material-icons) {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.help-item .material-icons:last-child {
  color: #999;
}

.copyright {
  text-align: center;
  font-size: 12px;
  color: #999;
  line-height: 1.6;
  margin-top: 40px;
}
</style> 