<template>
  <div class="min-h-screen bg-gray-100 flex flex-col">
    <div class="bg-white shadow-md py-4 px-6 text-lg font-bold text-gray-800">巡检记录</div>
    <div class="flex-1 flex flex-col items-center px-3 py-4">
      <div class="w-full max-w-md">
        <input type="text" placeholder="搜索记录" class="w-full mb-4 px-4 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-400" />
        <div class="space-y-4">
          <div v-for="i in 3" :key="i" class="bg-white rounded-2xl shadow p-4 border border-gray-100 flex flex-col gap-2">
            <div class="flex justify-between items-center">
              <div class="font-semibold text-gray-800">设备名称 {{ i }}</div>
              <span class="text-xs text-gray-400">2024-05-12</span>
            </div>
            <div class="text-sm text-gray-500">巡检人：张三</div>
            <div class="text-sm text-gray-500">状态：已完成</div>
          </div>
        </div>
      </div>
    </div>
    <nav class="flex justify-around items-center bg-white py-3 shadow-md">
      <router-link to="/" class="flex flex-col items-center text-gray-400 text-xs w-1/3" active-class="text-blue-500">
        <span class="material-icons text-lg mb-1">home</span>
        首页
      </router-link>
      <router-link to="/records" class="flex flex-col items-center text-gray-400 text-xs w-1/3" active-class="text-blue-500">
        <span class="material-icons text-lg mb-1">list_alt</span>
        记录
      </router-link>
      <router-link to="/profile" class="flex flex-col items-center text-gray-400 text-xs w-1/3" active-class="text-blue-500">
        <span class="material-icons text-lg mb-1">person</span>
        我的
      </router-link>
    </nav>
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