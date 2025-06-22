<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-logo">巡检管理系统后台</div>
      <div class="login-title">账号登录</div>
      <div class="login-desc">请输入您的账号和密码</div>
      
      <!-- 登录方式选择 -->
      <div class="login-type-selector">
        <a-radio-group v-model:value="loginType" button-style="solid" size="small">
          <a-radio-button value="local">系统账号</a-radio-button>
          <a-radio-button value="ad">AD域账号</a-radio-button>
        </a-radio-group>
      </div>
      
      <div class="login-form">
        <!-- 本地登录表单 -->
        <div v-if="loginType === 'local'">
          <a-form :model="localForm" :rules="localRules" ref="localFormRef">
            <div class="form-item">
              <label class="form-label">账号</label>
              <a-form-item name="username">
                <a-input 
                  v-model:value="localForm.username" 
                  placeholder="请输入账号/邮箱"
                  size="large"
                />
              </a-form-item>
            </div>
            <div class="form-item">
              <label class="form-label">密码</label>
              <a-form-item name="password">
                <a-input-password 
                  v-model:value="localForm.password" 
                  placeholder="请输入密码"
                  size="large"
                />
              </a-form-item>
            </div>
            <div class="login-remember">
              <a-checkbox v-model:checked="rememberPassword">记住密码</a-checkbox>
              <a href="#" class="forgot-password">忘记密码？</a>
            </div>
            <a-form-item>
              <a-button 
                type="primary" 
                block 
                size="large"
                class="login-btn"
                @click="handleLocalLogin" 
                :loading="localLoading"
              >
                登录
              </a-button>
            </a-form-item>
          </a-form>
        </div>
        
        <!-- AD域登录表单 -->
        <div v-else>
          <a-form :model="adForm" :rules="adRules" ref="adFormRef">
            <div class="form-item">
              <label class="form-label">AD域账号</label>
              <a-form-item name="username">
                <a-input 
                  v-model:value="adForm.username" 
                  placeholder="请输入AD域账号"
                  size="large"
                />
              </a-form-item>
            </div>
            <div class="form-item">
              <label class="form-label">密码</label>
              <a-form-item name="password">
                <a-input-password 
                  v-model:value="adForm.password" 
                  placeholder="请输入AD域密码"
                  size="large"
                />
              </a-form-item>
            </div>
            <div class="login-remember">
              <a-checkbox v-model:checked="rememberPassword">记住密码</a-checkbox>
              <a href="#" class="forgot-password">联系管理员</a>
            </div>
            <a-form-item>
              <a-button 
                type="primary" 
                block 
                size="large"
                class="login-btn"
                @click="handleADLogin" 
                :loading="adLoading"
              >
                登录
              </a-button>
            </a-form-item>
          </a-form>
        </div>
      </div>
      
      <div class="login-footer">
        © 2025 巡检管理系统后台
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { login, adLogin } from '@/api/auth'
import { useUserStore } from '@/stores/user'

export default defineComponent({
  name: 'Login',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    
    // 登录类型
    const loginType = ref('local')
    
    // 记住密码
    const rememberPassword = ref(true)
    
    // 加载状态
    const localLoading = ref(false)
    const adLoading = ref(false)
    
    // 本地登录表单
    const localFormRef = ref(null)
    const localForm = reactive({
      username: 'admin',
      password: '123456'
    })
    
    // AD域登录表单
    const adFormRef = ref(null)
    const adForm = reactive({
      username: '',
      password: ''
    })
    
    // 表单验证规则
    const localRules = {
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    }
    
    const adRules = {
      username: [{ required: true, message: '请输入AD账号', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    }
    
    // 本地登录处理
    const handleLocalLogin = async () => {
      try {
        await localFormRef.value.validate()
        
        localLoading.value = true
        const result = await login(localForm)
        localLoading.value = false
        
        if (result.code === 200) {
          userStore.setToken(result.data.token)
          userStore.setUserInfo(result.data.user)
          message.success('登录成功')
          router.push('/')
        } else {
          message.error(result.message || '登录失败')
        }
      } catch (error) {
        localLoading.value = false
        message.error('登录失败，请检查网络连接')
        console.error('登录验证失败:', error)
      }
    }
    
    // AD域登录处理
    const handleADLogin = async () => {
      try {
        await adFormRef.value.validate()
        
        adLoading.value = true
        const result = await adLogin(adForm)
        adLoading.value = false
        
        if (result.code === 200) {
          userStore.setToken(result.data.token)
          userStore.setUserInfo(result.data.user)
          message.success('登录成功')
          router.push('/')
        } else {
          message.error(result.message || 'AD登录失败')
        }
      } catch (error) {
        adLoading.value = false
        message.error('AD登录失败，请检查网络连接')
        console.error('AD登录验证失败:', error)
      }
    }
    
    return {
      loginType,
      rememberPassword,
      localForm,
      adForm,
      localRules,
      adRules,
      localFormRef,
      adFormRef,
      localLoading,
      adLoading,
      handleLocalLogin,
      handleADLogin
    }
  }
})
</script>

<style lang="less" scoped>
.login-container {
  margin: 0;
  padding: 0;
  min-height: 100vh;
  background: linear-gradient(135deg, #6a82fb 0%, #fc5c7d 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.login-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  padding: 40px 32px 32px 32px;
  width: 400px;
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
  margin-bottom: 16px;
  font-size: 14px;
}

.login-type-selector {
  margin-bottom: 24px;
  
  :deep(.ant-radio-group) {
    display: flex;
    justify-content: center;
  }
  
  :deep(.ant-radio-button-wrapper) {
    border-radius: 4px !important;
    margin: 0 4px;
  }
}

.login-form {
  width: 100%;
}

.form-item {
  margin-bottom: 16px;
  
  :deep(.ant-form-item) {
    margin-bottom: 0;
  }
  
  :deep(.ant-form-item-explain-error) {
    margin-top: 4px;
  }
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}

:deep(.ant-input) {
  height: 40px;
  border-radius: 4px;
}

:deep(.ant-input-password) {
  height: 40px;
  border-radius: 4px;
}

.login-remember {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 8px 0 16px 0;
}

.forgot-password {
  color: #1890ff;
  text-decoration: none;
  font-size: 14px;
  
  &:hover {
    color: #40a9ff;
  }
}

.login-btn {
  height: 40px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  
  &:hover {
    background: #40a9ff;
  }
}

.login-footer {
  margin-top: 16px;
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style> 