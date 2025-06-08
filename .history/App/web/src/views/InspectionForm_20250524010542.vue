<template>
  <div class="form-container">
    <div class="form-header">
      <a class="back-button" @click.prevent="router.push('/scan')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>机房巡检 - 区域A</h1>
    </div>
    <div class="form-content">
      <!-- 环境巡检 -->
      <div class="form-section">
        <div class="form-title">环境巡检</div>
        <div class="form-item" v-for="item in environmentItems" :key="item.name">
          <span>{{ item.name }}</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" :name="'env_' + item.name" value="正常" v-model="item.status" />
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" :name="'env_' + item.name" value="异常" v-model="item.status" />
              <span>异常</span>
            </label>
          </div>
        </div>
      </div>
      <!-- 安全巡检 -->
      <div class="form-section">
        <div class="form-title">安全巡检</div>
        <div class="form-item" v-for="item in securityItems" :key="item.name">
          <span>{{ item.name }}</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" :name="'sec_' + item.name" value="正常" v-model="item.status" />
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" :name="'sec_' + item.name" value="异常" v-model="item.status" />
              <span>异常</span>
            </label>
          </div>
        </div>
      </div>
      <!-- 备注信息 -->
      <div class="form-section">
        <div class="form-title">备注信息</div>
        <div class="form-item">
          <textarea class="remarks-input" v-model="remark" placeholder="请输入备注信息"></textarea>
        </div>
      </div>
      <!-- 提交按钮 -->
      <div class="form-actions">
        <button class="submit-button" @click="submitForm">提交</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { createRecord } from '@/api/inspection'

const router = useRouter()
const route = useRoute()

const environmentItems = ref([
  { name: '机房温度', status: '' },
  { name: '冷通道回风温度', status: '' },
  { name: '供电情况', status: '' }
])
const securityItems = ref([
  { name: '门禁状态', status: '' },
  { name: '声光报警', status: '' },
  { name: '漏水检测', status: '' },
  { name: '易燃物检查', status: '' }
])
const remark = ref('')

async function submitForm() {
  try {
    // 表单验证
    if (environmentItems.value.some(i => !i.status) || securityItems.value.some(i => !i.status)) {
      window.alert('请完成所有巡检项选择')
      return
    }

    // 构造提交数据
    const payload = {
      recordNo: route.query.code || '',
      areaId: Number(route.query.areaId) || 1,
      startTime: new Date().toISOString(),
      endTime: new Date().toISOString(),
      status: 'normal',
      environmentItems: environmentItems.value.map(i => ({
        name: i.name,
        status: i.status,
        type: 'environment',
        remark: i.remark || ''
      })),
      securityItems: securityItems.value.map(i => ({
        name: i.name,
        status: i.status,
        type: 'security',
        remark: i.remark || ''
      })),
      remark: remark.value || ''
    }

    // 提交数据
    const res = await createRecord(payload)
    if (res.code === 200 || res.code === 0) {
      router.push('/records')
    } else {
      throw new Error(res.message || '提交失败')
    }
  } catch (err) {
    console.error('提交巡检记录失败:', err)
    window.alert(err.message || '提交失败，请重试')
  }
}
</script>

<style scoped>
.form-container {
    max-width: 420px;
    margin: 0 auto;
    padding: 0 0 80px 0;
}
.form-header {
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
.form-header .back-button {
    position: absolute;
    left: 18px;
    top: 32px;
    color: #888;
    text-decoration: none;
}
.form-content {
    flex: 1;
    padding: 20px;
    padding-bottom: 70px;
    overflow-y: auto;
}
.form-section {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.06);
    margin-bottom: 22px;
    padding: 20px 18px 10px 18px;
}
.form-title {
    font-size: 17px;
    font-weight: 600;
    margin-bottom: 14px;
    color: #222;
}
.form-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
}
.form-item:last-child {
    border-bottom: none;
}
.form-label {
    font-size: 15px;
    color: #333;
}
.radio-group {
    display: flex;
    gap: 18px;
}
.radio-option {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 15px;
}
.remarks-input {
    width: 100%;
    min-height: 80px;
    padding: 10px;
    border: 1.5px solid #e5e5e5;
    border-radius: 10px;
    resize: none;
    font-size: 15px;
    margin-top: 8px;
    background: #fafbfc;
}
.form-actions {
    position: fixed;
    left: 0; right: 0; bottom: 0;
    background: #fff;
    box-shadow: 0 -2px 10px rgba(0,0,0,0.06);
    padding: 18px 20px 18px 20px;
    z-index: 10;
}
.submit-button {
    width: 100%;
    padding: 15px;
    background-color: var(--primary-color, #2196F3);
    color: #fff;
    border: none;
    border-radius: 12px;
    font-size: 17px;
    font-weight: 600;
    cursor: pointer;
    transition: background 0.2s;
}
.submit-button:active {
    background-color: #1976d2;
}
.loading {
    text-align: center;
    padding: 20px;
    color: #999;
}
</style> 