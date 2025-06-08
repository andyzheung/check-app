<template>
  <div class="inspection-form">
    <!-- 区域信息 -->
    <div class="area-info">
      <h2>{{ areaInfo.areaName || '未知区域' }}</h2>
      <p class="area-code">编号：{{ areaCode }}</p>
      <p class="area-type">类型：{{ areaInfo.areaTypeName || getAreaTypeName(areaInfo.areaType) }}</p>
      <p class="area-desc" v-if="areaInfo.description">{{ areaInfo.description }}</p>
    </div>
    
    <!-- 巡检表单 -->
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
          <textarea 
            v-if="item.status === '异常'" 
            v-model="item.remark" 
            class="item-remark" 
            placeholder="请输入异常情况说明"
          ></textarea>
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
          <textarea 
            v-if="item.status === '异常'" 
            v-model="item.remark" 
            class="item-remark" 
            placeholder="请输入异常情况说明"
          ></textarea>
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
        <button class="submit-button" @click="submitForm" :disabled="submitting">
          {{ submitting ? '提交中...' : '提交' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { createRecord, getAreaByCode } from '@/api/inspection'

const router = useRouter()
const route = useRoute()
const areaCode = ref('')
const areaInfo = ref({})
const submitting = ref(false)

// 区域类型名称映射
const areaTypeMap = {
  'A': '机房',
  'B': '办公区',
  'C': '设备区'
}

// 获取区域类型名称
function getAreaTypeName(type) {
  return areaTypeMap[type] || '未知类型'
}

const environmentItems = ref([
  { name: '机房温度', status: '', remark: '' },
  { name: '冷通道回风温度', status: '', remark: '' },
  { name: '供电情况', status: '', remark: '' }
])

const securityItems = ref([
  { name: '门禁状态', status: '', remark: '' },
  { name: '声光报警', status: '', remark: '' },
  { name: '漏水检测', status: '', remark: '' },
  { name: '易燃物检查', status: '', remark: '' }
])

const remark = ref('')

onMounted(async () => {
  areaCode.value = route.query.code
  if (areaCode.value) {
    try {
      const res = await getAreaByCode(areaCode.value)
      if (res.code === 200 && res.data) {
        areaInfo.value = res.data
      } else {
        window.alert('获取区域信息失败：' + (res.message || '未知错误'))
        router.push('/scan')
      }
    } catch (error) {
      console.error('获取区域信息失败:', error)
      window.alert('获取区域信息失败，请重试')
      router.push('/scan')
    }
  } else {
    window.alert('无效的区域编码')
    router.push('/scan')
  }
})

async function submitForm() {
  try {
    // 表单验证
    if (environmentItems.value.some(i => !i.status) || securityItems.value.some(i => !i.status)) {
      window.alert('请完成所有巡检项选择')
      return
    }

    // 异常项必须填写说明
    const hasEmptyAbnormalRemark = [...environmentItems.value, ...securityItems.value].some(
      item => item.status === '异常' && !item.remark
    )
    if (hasEmptyAbnormalRemark) {
      window.alert('请为所有异常项填写说明')
      return
    }

    submitting.value = true

    // 构造提交数据
    const payload = {
      areaCode: areaCode.value,
      areaId: areaInfo.value.id,
      startTime: new Date().toISOString(),
      endTime: new Date().toISOString(),
      status: 'normal',
      environmentItems: environmentItems.value.map(i => ({
        name: i.name,
        status: i.status === '正常' ? 'normal' : 'abnormal',
        type: 'environment',
        remark: i.remark || ''
      })),
      securityItems: securityItems.value.map(i => ({
        name: i.name,
        status: i.status === '正常' ? 'normal' : 'abnormal',
        type: 'security',
        remark: i.remark || ''
      })),
      remark: remark.value || ''
    }

    // 如果有异常项，设置整体状态为异常
    if ([...payload.environmentItems, ...payload.securityItems].some(i => i.status === 'abnormal')) {
      payload.status = 'abnormal'
    }

    // 提交数据
    const res = await createRecord(payload)
    if (res.code === 200 || res.code === 0) {
      window.alert('提交成功')
      router.push('/records')
    } else {
      throw new Error(res.message || '提交失败')
    }
  } catch (err) {
    console.error('提交巡检记录失败:', err)
    window.alert(err.message || '提交失败，请重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.inspection-form {
  padding: 16px;
  max-width: 800px;
  margin: 0 auto;
}

.area-info {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.area-info h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
}

.area-code {
  margin: 8px 0 0;
  color: #666;
  font-size: 14px;
}

.area-type {
  margin: 4px 0 0;
  color: #666;
  font-size: 14px;
}

.area-desc {
  margin: 8px 0 0;
  color: #666;
  font-size: 14px;
  line-height: 1.4;
}

.form-content {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.form-section {
  margin-bottom: 24px;
}

.form-title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.form-item {
  margin-bottom: 16px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 4px;
}

.form-item > span {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-size: 15px;
}

.radio-group {
  display: flex;
  gap: 24px;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
}

.radio-option input[type="radio"] {
  margin: 0;
}

.item-remark {
  margin-top: 8px;
  width: 100%;
  min-height: 60px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  resize: vertical;
}

.remarks-input {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  resize: vertical;
}

.form-actions {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

.submit-button {
  min-width: 120px;
  height: 40px;
  background: #1989fa;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background: #1677dd;
}

.submit-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style> 