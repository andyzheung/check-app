<template>
  <div class="login-container">
    <div class="login-form">
      <div class="login-title">巡检App管理系统</div>
      <div style="margin-bottom: 24px; text-align: center;">
        <a-radio-group v-model:value="loginType" size="large">
          <a-radio-button value="local">系统账号登录</a-radio-button>
          <a-radio-button value="ad">AD域账号登录</a-radio-button>
        </a-radio-group>
      </div>
      <div v-if="loginType === 'local'">
        <a-form :model="localForm" :rules="localRules" ref="localFormRef">
        <a-form-item name="username">
            <a-input v-model:value="localForm.username" placeholder="用户名" />
        </a-form-item>
        <a-form-item name="password">
            <a-input-password v-model:value="localForm.password" placeholder="密码" />
          </a-form-item>
          <a-form-item>
            <a-button type="primary" block @click="handleLocalLogin" :loading="localLoading">登录</a-button>
        </a-form-item>
        </a-form>
        </div>
      <div v-else>
        <a-form :model="adForm" :rules="adRules" ref="adFormRef">
          <a-form-item name="username">
            <a-input v-model:value="adForm.username" placeholder="AD账号" />
          </a-form-item>
          <a-form-item name="password">
            <a-input-password v-model:value="adForm.password" placeholder="密码" />
          </a-form-item>
        <a-form-item>
            <a-button type="primary" block @click="handleADLogin" :loading="adLoading">登录</a-button>
        </a-form-item>
      </a-form>
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
    
    // 加载状态
    const localLoading = ref(false)
    const adLoading = ref(false)
    
    // 本地登录表单
    const localFormRef = ref(null)
    const localForm = reactive({
      username: '',
      password: ''
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
          userStore.setUser(result.data.user)
          message.success('登录成功')
          router.push('/')
        } else {
          message.error(result.message || '登录失败')
        }
      } catch (error) {
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
          userStore.setUser(result.data.user)
          message.success('登录成功')
          router.push('/')
        } else {
          message.error(result.message || '登录失败')
      }
      } catch (error) {
        console.error('AD登录验证失败:', error)
      }
    }
    
    return {
      loginType,
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
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
  // background-image: url('@/assets/images/login-bg.jpg');
  background-size: cover;
  background-position: center;
}

.login-form {
  width: 380px;
  padding: 30px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.login-title {
  font-size: 24px;
  color: #333;
  text-align: center;
  margin-bottom: 30px;
}
</style> 