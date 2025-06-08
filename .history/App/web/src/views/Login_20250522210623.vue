<template>
  <div class="login">
    <input v-model="username" placeholder="用户名" />
    <input v-model="password" type="password" placeholder="密码" />
    <button @click="doLogin">登录</button>
    <div v-if="errorMsg" class="error">{{ errorMsg }}</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '@/api/auth'

const username = ref('')
const password = ref('')
const errorMsg = ref('')

async function doLogin() {
  errorMsg.value = ''
  const res = await login(username.value, password.value)
  if (res.success) {
    window.location.href = '/'
  } else {
    errorMsg.value = res.message
  }
}
</script>

<style scoped>
.login {
  max-width: 320px;
  margin: 100px auto;
  padding: 32px;
  border: 1px solid #eee;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 8px #eee;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
input {
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
.error {
  color: #e74c3c;
  font-size: 14px;
  margin-top: 8px;
}
</style> 