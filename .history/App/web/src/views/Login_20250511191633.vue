<template>
  <div class="login-container">
    <div class="login-logo">巡检<br />App</div>
    <div class="login-form">
      <div class="input-group">
        <input type="text" class="input-field" v-model="username" placeholder="请输入AD账号" />
      </div>
      <div class="input-group">
        <input type="password" class="input-field" v-model="password" placeholder="请输入密码" />
      </div>
      <div class="remember-password">
        <label class="checkbox-container">
          <input type="checkbox" v-model="remember" />
          <span class="checkmark"></span>
          记住密码
        </label>
      </div>
      <button class="login-button" @click="handleLogin">登录</button>
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
</style> 