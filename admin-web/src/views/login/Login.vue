<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-logo">巡检管理系统后台</div>
      <div class="login-title">账号登录</div>
      <div class="login-desc">请输入您的账号和密码</div>
      
      <a-form
        class="login-form"
        :model="formState"
        :rules="rules"
        @finish="handleSubmit"
      >
        <a-form-item name="username">
          <a-input
            v-model:value="formState.username"
            placeholder="请输入账号/邮箱"
            size="large"
          >
            <template #prefix>
              <user-outlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item name="password">
          <a-input-password
            v-model:value="formState.password"
            placeholder="请输入密码"
            size="large"
          >
            <template #prefix>
              <lock-outlined />
            </template>
          </a-input-password>
        </a-form-item>
        <div class="login-options">
          <a-checkbox v-model:checked="formState.remember">记住密码</a-checkbox>
          <a class="forgot-link">忘记密码？</a>
        </div>
        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            class="login-button"
            size="large"
            :loading="loading"
          >
            登录
          </a-button>
        </a-form-item>
      </a-form>
      
      <div class="login-footer">
        © 2025 巡检管理系统后台
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, reactive, ref } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/user'

export default defineComponent({
  name: 'Login',
  components: {
    UserOutlined,
    LockOutlined
  },
  setup() {
    const router = useRouter()
    const loading = ref(false)
    
    const formState = reactive({
      username: '',
      password: '',
      remember: true
    })
    
    const rules = {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }
    
    const handleSubmit = async (values) => {
      loading.value = true
      try {
        // 清理所有cookie，防止请求头过大
        document.cookie.split(';').forEach(cookie => {
          const name = cookie.trim().split('=')[0]
          document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`
        })
        
        console.log('登录请求数据:', { username: values.username, password: values.password })
        const response = await login({
          username: values.username,
          password: values.password
        })
        
        console.log('登录响应:', response)
        
        // 处理不同格式的响应数据
        let userData = response
        // 如果响应是嵌套的数据结构
        if (response && response.data && response.data.token) {
          userData = response.data
        }
        
        // 确保返回数据中包含token
        if (userData && userData.token) {
          localStorage.setItem('token', userData.token)
          
          // 存储用户信息
          if (userData.userId) localStorage.setItem('userId', userData.userId)
          if (userData.username) localStorage.setItem('username', userData.username)
          if (userData.realName) localStorage.setItem('realName', userData.realName || userData.username)
          if (userData.role) localStorage.setItem('role', userData.role || 'user')
          
          // 记住账号
          if (values.remember) {
            localStorage.setItem('rememberedUsername', values.username)
          } else {
            localStorage.removeItem('rememberedUsername')
          }
          
          message.success('登录成功')
          router.push('/')
        } else {
          message.error('登录失败：未获取到有效Token')
          console.error('登录响应缺少token:', response)
        }
      } catch (error) {
        console.error('登录失败:', error)
        message.error('登录失败：' + (error.message || '服务器错误'))
      } finally {
        loading.value = false
      }
    }
    
    // 检查是否有记住的用户名
    const initRememberedUsername = () => {
      const rememberedUsername = localStorage.getItem('rememberedUsername')
      if (rememberedUsername) {
        formState.username = rememberedUsername
        formState.remember = true
      }
    }
    
    // 页面加载时初始化
    initRememberedUsername()
    
    return {
      formState,
      rules,
      loading,
      handleSubmit
    }
  }
})
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #6a82fb 0%, #fc5c7d 100%);
}

.login-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  padding: 40px 32px 32px 32px;
  width: 360px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-logo {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #1890ff;
}

.login-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #2d3a4b;
}

.login-desc {
  color: #888;
  margin-bottom: 24px;
  font-size: 14px;
}

.login-form {
  width: 100%;
}

.login-options {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24px;
}

.forgot-link {
  color: #1890ff;
  cursor: pointer;
}

.login-button {
  width: 100%;
}

.login-footer {
  margin-top: 24px;
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style> 