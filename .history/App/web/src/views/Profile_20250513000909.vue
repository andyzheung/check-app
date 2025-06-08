<template>
  <div class="min-h-screen bg-gray-100 flex flex-col">
    <div class="bg-blue-500 h-32 rounded-b-3xl flex items-end justify-center relative">
      <div class="absolute top-20 w-full flex justify-center">
        <div class="w-24 h-24 bg-white rounded-2xl shadow-lg flex flex-col items-center justify-center border-4 border-blue-100">
          <span class="material-icons text-blue-400 text-5xl">person</span>
        </div>
      </div>
    </div>
    <div class="flex-1 flex flex-col items-center mt-20 px-4">
      <div class="w-full max-w-md bg-white rounded-2xl shadow-lg p-6 mb-6">
        <div class="text-lg font-bold text-gray-900 mb-2 text-center">用户名</div>
        <div class="text-sm text-gray-500 text-center mb-4">普通用户</div>
        <div class="divide-y divide-gray-100">
          <div class="py-3 flex items-center justify-between">
            <span class="text-gray-700">我的记录</span>
            <span class="material-icons text-gray-400">chevron_right</span>
          </div>
          <div class="py-3 flex items-center justify-between">
            <span class="text-gray-700">设置</span>
            <span class="material-icons text-gray-400">chevron_right</span>
          </div>
        </div>
        <button class="w-full mt-6 py-3 bg-red-500 text-white rounded-lg font-semibold shadow-md hover:bg-red-600 transition-colors">退出登录</button>
      </div>
    </div>
  </div>
</template>

<script setup>
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