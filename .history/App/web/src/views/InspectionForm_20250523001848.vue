<template>
  <div class="inspection-form-container">
    <div class="form-header">
      <a class="back-button" @click.prevent="router.push('/scan')">
        <span class="material-icons">arrow_back</span>
      </a>
      <h2>机房巡检 - {{ areaName }}</h2>
    </div>
    <div class="form-section">
      <h3>环境巡检</h3>
      <div v-for="(item, idx) in environmentItems" :key="idx" class="form-item">
        <span>{{ item.name }}</span>
        <label><input type="radio" v-model="item.status" value="正常">正常</label>
        <label><input type="radio" v-model="item.status" value="异常">异常</label>
      </div>
    </div>
    <div class="form-section">
      <h3>安全巡检</h3>
      <div v-for="(item, idx) in securityItems" :key="idx" class="form-item">
        <span>{{ item.name }}</span>
        <label><input type="radio" v-model="item.status" value="正常">正常</label>
        <label><input type="radio" v-model="item.status" value="异常">异常</label>
      </div>
    </div>
    <div class="form-section">
      <h3>备注信息</h3>
      <textarea v-model="remark" placeholder="请输入备注信息"></textarea>
    </div>
    <button class="submit-btn" @click="submit" :disabled="loading">{{ loading ? '提交中...' : '提交' }}</button>
    <div v-if="msg" class="msg">{{ msg }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { createRecord } from '@/api/record'

const route = useRoute()
const router = useRouter()
const areaId = ref('')
const areaName = ref('')
const environmentItems = ref([])
const securityItems = ref([])
const remark = ref('')
const loading = ref(false)
const msg = ref('')

onMounted(async () => {
  // 获取扫码/输入编号
  const code = route.query.pointCode
  // 获取区域信息
  const areaRes = await request.get(`/areas/${code}`)
  areaId.value = areaRes.data.data.id
  areaName.value = areaRes.data.data.name
  // 获取巡检项模板
  const tplRes = await request.get('/inspection-template', { params: { areaId: areaId.value } })
  environmentItems.value = tplRes.data.data.environmentItems
  securityItems.value = tplRes.data.data.securityItems
})

async function submit() {
  loading.value = true
  try {
    await createRecord({
      areaId: areaId.value,
      areaName: areaName.value,
      environmentItems: environmentItems.value,
      securityItems: securityItems.value,
      remark: remark.value
    })
    msg.value = '提交成功！'
    setTimeout(() => router.push('/records'), 1000)
  } catch (e) {
    msg.value = '提交失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.inspection-form-container {
  max-width: 500px;
  margin: 30px auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px #eee;
  padding: 24px;
}
.form-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 18px;
}
.form-section {
  margin-bottom: 18px;
}
.form-item {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 10px;
}
textarea {
  width: 100%;
  min-height: 60px;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 8px;
  font-size: 15px;
}
.submit-btn {
  width: 100%;
  padding: 12px;
  background: #2196F3;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 18px;
  cursor: pointer;
}
.msg {
  color: #2196F3;
  font-size: 15px;
  margin-top: 8px;
  text-align: center;
}
</style> 