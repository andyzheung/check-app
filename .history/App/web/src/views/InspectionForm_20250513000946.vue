<template>
  <div class="min-h-screen bg-gray-100 flex flex-col items-center py-6">
    <div class="w-full max-w-md bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
      <div class="text-xl font-bold text-gray-900 mb-4">巡检表单</div>
      <form class="flex flex-col gap-4">
        <div>
          <label class="block text-gray-700 mb-1">设备名称</label>
          <input type="text" class="w-full px-4 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-400" />
        </div>
        <div>
          <label class="block text-gray-700 mb-1">巡检区域</label>
          <select class="w-full px-4 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-400">
            <option>A区机房</option>
            <option>B区机房</option>
            <option>C区机房</option>
          </select>
        </div>
        <div>
          <label class="block text-gray-700 mb-1">状态</label>
          <select class="w-full px-4 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-400">
            <option>正常</option>
            <option>异常</option>
          </select>
        </div>
        <div>
          <label class="block text-gray-700 mb-1">备注</label>
          <textarea class="w-full px-4 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-400" rows="3"></textarea>
        </div>
        <button type="submit" class="w-full py-3 bg-blue-500 text-white rounded-lg font-semibold shadow-md hover:bg-blue-600 transition-colors mt-2">提交</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const router = useRouter()
const route = useRoute()

// 动态区域名，优先取路由参数，否则显示"未识别区域"
const areaName = computed(() => route.query.area || '未识别区域')

const form = ref({
  temperature: '',
  cold_channel_temperature: '',
  power_supply: '',
  door_access: '',
  sound_light_alarm: '',
  leakage_detection: '',
  flammable_object_check: '',
  remarks: ''
})

const handleSubmit = () => {
  // TODO: 可扩展为本地存储或API提交
  alert('提交成功！')
  router.push('/records')
}
</script>

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/inspection-form.css');
@import url('https://fonts.googleapis.com/icon?family=Material+Icons');
</style> 