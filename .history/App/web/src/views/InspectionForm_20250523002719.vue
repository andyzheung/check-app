<template>
  <div class="form-container">
    <div class="form-header">
      <a class="back-button" @click.prevent="router.push('/scan')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h1>机房巡检 - {{ area.name || '未识别' }}</h1>
    </div>
    <div class="form-content" v-if="templateLoaded">
      <!-- 环境巡检 -->
      <div class="form-section">
        <div class="form-title">环境巡检</div>
        <div class="form-item" v-for="item in environmentItems" :key="item.id">
          <span>{{ item.name }}</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" :name="'env_' + item.id" value="正常" v-model="item.status" />
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" :name="'env_' + item.id" value="异常" v-model="item.status" />
              <span>异常</span>
            </label>
          </div>
        </div>
      </div>

      <!-- 安全巡检 -->
      <div class="form-section">
        <div class="form-title">安全巡检</div>
        <div class="form-item" v-for="item in securityItems" :key="item.id">
          <span>{{ item.name }}</span>
          <div class="radio-group">
            <label class="radio-option">
              <input type="radio" :name="'sec_' + item.id" value="正常" v-model="item.status" />
              <span>正常</span>
            </label>
            <label class="radio-option">
              <input type="radio" :name="'sec_' + item.id" value="异常" v-model="item.status" />
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
    <div v-else class="loading">正在加载模板...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAreaByCode, getTemplateByAreaId, createRecord } from '@/api/inspection'
import { showToast } from 'vant'

const route = useRoute()
const router = useRouter()

const area = ref({})
const environmentItems = ref([])
const securityItems = ref([])
const remark = ref('')
const templateLoaded = ref(false)

onMounted(async () => {
  try {
    // 1. 获取扫码/输入编号
    const code = route.query.code
    if (!code) {
      showToast('未获取到区域编号')
      return
    }

    // 2. 获取区域信息
    const areaRes = await getAreaByCode(code)
    if (!areaRes.data) {
      showToast('未找到对应区域')
      return
    }
    area.value = areaRes.data

    // 3. 获取模板
    const tplRes = await getTemplateByAreaId(area.value.id)
    if (!tplRes.data) {
      showToast('未找到巡检模板')
      return
    }

    // 初始化表单项
    environmentItems.value = tplRes.data.environmentItems.map(i => ({
      ...i,
      status: ''
    }))
    securityItems.value = tplRes.data.securityItems.map(i => ({
      ...i,
      status: ''
    }))
    templateLoaded.value = true
  } catch (error) {
    showToast('加载失败：' + error.message)
  }
})

async function submitForm() {
  try {
    // 校验
    if (environmentItems.value.some(i => !i.status) || securityItems.value.some(i => !i.status)) {
      showToast('请完成所有巡检项选择')
      return
    }

    const data = {
      areaId: area.value.id,
      areaName: area.value.name,
      environmentItems: environmentItems.value.map(i => ({
        name: i.name,
        status: i.status
      })),
      securityItems: securityItems.value.map(i => ({
        name: i.name,
        status: i.status
      })),
      remark: remark.value
    }

    await createRecord(data)
    showToast('提交成功')
    router.push('/records')
  } catch (error) {
    showToast('提交失败：' + error.message)
  }
}
</script>

<style scoped>
.form-container {
  padding: 16px;
  background: #f5f5f5;
  min-height: 100vh;
}

.form-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.back-button {
  margin-right: 12px;
  color: #333;
}

.form-header h1 {
  font-size: 18px;
  margin: 0;
}

.form-section {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.form-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #333;
}

.form-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.radio-group {
  display: flex;
  gap: 16px;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 4px;
}

.remarks-input {
  width: 100%;
  height: 100px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: none;
}

.form-actions {
  margin-top: 24px;
}

.submit-button {
  width: 100%;
  height: 44px;
  background: #1989fa;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #999;
}
</style> 