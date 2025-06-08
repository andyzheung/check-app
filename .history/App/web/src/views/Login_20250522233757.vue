<template>
  <div class="login-container">
    <div class="login-logo">巡检<br />App</div>
    <form class="login-form" @submit.prevent="doLogin">
      <div class="input-group">
        <input
          class="input-field"
          v-model="username"
          placeholder="请输入AD账号"
        />
      </div>
      <div class="input-group">
        <input
          class="input-field"
          v-model="password"
          type="password"
          placeholder="请输入密码"
        />
      </div>
      <div class="remember-password">
        <label class="checkbox-container">
          <input type="checkbox" v-model="remember" />
          <span class="checkmark"></span>
          记住密码
        </label>
      </div>
      <button class="login-button" type="submit">登录</button>
      <div v-if="errorMsg" class="error">{{ errorMsg }}</div>
      <div class="default-tip">默认账号：admin / 密码：123456</div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'

const username = ref('')
const password = ref('')
const remember = ref(false)
const errorMsg = ref('')
const router = useRouter()

onMounted(() => {
  const saved = localStorage.getItem('loginInfo')
  if (saved) {
    const { username: u, password: p } = JSON.parse(saved)
    username.value = u
    password.value = p
    remember.value = true
  }
})

async function doLogin() {
  errorMsg.value = ''
  const res = await login(username.value, password.value)
  if (res.success) {
    if (remember.value) {
      localStorage.setItem(
        'loginInfo',
        JSON.stringify({ username: username.value, password: password.value })
      )
    } else {
      localStorage.removeItem('loginInfo')
    }
    router.push('/home')
  } else {
    errorMsg.value = res.message
  }
}
</script>

<style src="@/assets/css/login.css"></style> 