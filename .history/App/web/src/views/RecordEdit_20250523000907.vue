<template>
  <div class="record-edit-container">
    <h2>巡检记录填写</h2>
    <div>巡检点：{{ pointName || pointCode }}</div>
    <textarea v-model="remark" placeholder="请输入巡检内容或备注"></textarea>
    <button @click="submitRecord">提交记录</button>
    <div v-if="msg" class="msg">{{ msg }}</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createRecord } from '@/api/record'

const route = useRoute()
const router = useRouter()
const pointCode = route.query.pointCode || ''
const pointId = route.query.pointId || ''
const pointName = route.query.pointName || ''
const remark = ref('')
const msg = ref('')

async function submitRecord() {
  try {
    await createRecord({
      pointCode,
      pointId,
      pointName,
      remark: remark.value
    })
    msg.value = '提交成功！'
    setTimeout(() => router.push('/records'), 1000)
  } catch (e) {
    msg.value = '提交失败，请重试'
  }
}
</script>

<style scoped>
.record-edit-container {
  max-width: 400px;
  margin: 40px auto;
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px #eee;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
textarea {
  min-height: 80px;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  padding: 10px;
  font-size: 16px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.msg {
  color: #2196F3;
  font-size: 15px;
  margin-top: 8px;
}
</style> 