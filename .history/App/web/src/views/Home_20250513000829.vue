<template>
  <div class="min-h-screen flex flex-col bg-gray-100">
    <div class="flex-1 flex flex-col items-center justify-start px-3 pt-6">
      <div class="w-full max-w-md bg-white rounded-2xl shadow-lg mb-5 p-5 border border-gray-100">
        <div class="text-xl font-bold text-gray-900 mb-2">欢迎使用巡检App</div>
        <div class="text-base text-gray-600 mb-1">扫码巡检，记录管理，权限分明</div>
      </div>
      <div class="flex flex-col items-center mt-8">
        <button class="w-20 h-20 bg-blue-500 rounded-full flex items-center justify-center shadow-lg mb-2 active:scale-95 transition-transform">
          <span class="material-icons text-white text-4xl">qr_code_scanner</span>
        </button>
        <div class="text-base font-semibold text-gray-800 mt-1 tracking-wide">扫码巡检</div>
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