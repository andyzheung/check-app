<template>
  <div class="min-h-screen flex flex-col justify-center items-center bg-gray-100">
    <div class="w-full max-w-xs bg-white rounded-2xl shadow-lg p-8 border border-gray-100">
      <div class="text-2xl font-bold text-center text-blue-600 mb-6">巡检App登录</div>
      <form class="flex flex-col gap-4">
        <input type="text" placeholder="AD账号" class="px-4 py-3 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-400" />
        <input type="password" placeholder="密码" class="px-4 py-3 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-400" />
        <button type="submit" class="w-full py-3 bg-blue-500 text-white rounded-lg font-semibold shadow-md hover:bg-blue-600 transition-colors">登录</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const remember = ref(false)

onMounted(() => {
  // 自动填充记住的账号密码
  const saved = JSON.parse(localStorage.getItem('login-remember') || '{}')
  if (saved.username && saved.password) {
    username.value = saved.username
    password.value = saved.password
    remember.value = true
  }
})

const handleLogin = () => {
  // 这里依然用admin/123456做模拟
  if (username.value === 'admin' && password.value === '123456') {
    if (remember.value) {
      localStorage.setItem('login-remember', JSON.stringify({ username: username.value, password: password.value }))
    } else {
      localStorage.removeItem('login-remember')
    }
    localStorage.setItem('user', JSON.stringify({ username: username.value, role: '管理员' }))
    router.push('/home')
  } else {
    alert('账号或密码错误')
  }
}
</script>

<style scoped>
@import url('@/assets/css/common.css');
@import url('@/assets/css/login.css');
.login-tip {
  text-align: center;
  color: #888;
  font-size: 14px;
  margin-top: 10px;
}
</style> 