<template>
  <div class="login-page">
    <div class="login-box">
      <h2>巡检系统</h2>
      <div class="input-group">
        <input type="text" v-model="username" placeholder="用户名" />
      </div>
      <div class="input-group">
        <input type="password" v-model="password" placeholder="密码" />
      </div>
      <button class="login-btn" @click="handleLogin">登录</button>
      <p class="login-tip">默认用户：admin / 密码：123456</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')

const handleLogin = () => {
  // 模拟登录验证
  if (username.value === 'admin' && password.value === '123456') {
    // 存储用户信息
    localStorage.setItem('user', JSON.stringify({
      username: username.value,
      role: '管理员'
    }))
    router.push('/home')
  } else {
    alert('用户名或密码错误')
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f7f7f7;
}

.login-box {
  width: 80%;
  max-width: 400px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 15px;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  margin-bottom: 10px;
}

.login-btn:hover {
  background: #45a049;
}

.login-tip {
  text-align: center;
  color: #666;
  font-size: 14px;
  margin: 0;
}
</style> 