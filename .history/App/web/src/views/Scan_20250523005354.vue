<template>
  <div class="scan-container">
    <div class="scan-header">
      <h2>扫码巡检</h2>
    </div>
    <div class="scan-content">
      <div class="input-group">
        <input 
          type="text" 
          v-model="code" 
          placeholder="请输入巡检点编号"
          @keyup.enter="handleCode"
        />
        <button @click="handleCode">确定</button>
      </div>
      <div class="scan-tip">
        <p>请扫描巡检点二维码或输入编号</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const code = ref('')

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
</script>

<style scoped>
.scan-container {
  padding: 16px;
  min-height: 100vh;
  background: #f5f5f5;
}

.scan-header {
  text-align: center;
  margin-bottom: 24px;
}

.scan-header h2 {
  font-size: 20px;
  color: #333;
  margin: 0;
}

.scan-content {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}

.input-group {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.input-group input {
  flex: 1;
  height: 44px;
  padding: 0 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.input-group button {
  width: 80px;
  background: #1989fa;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
}

.scan-tip {
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style> 