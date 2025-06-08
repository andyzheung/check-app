import { defineStore } from 'pinia'
import { login as loginApi } from '@/api/auth'
import router from '@/router'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null')
  }),
  
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    
    setUserInfo(info) {
      this.userInfo = info
      localStorage.setItem('userInfo', JSON.stringify(info))
    },
    
    async login(username, password) {
      try {
        const res = await loginApi(username, password)
        if (res.code === 200 || res.code === 0) {
          this.setToken(res.data.token)
          this.setUserInfo(res.data.userInfo)
          
          // 跳转到首页或重定向页面
          const redirect = router.currentRoute.value.query?.redirect || '/'
          router.push(redirect)
          return true
        }
        return false
      } catch (error) {
        console.error('登录失败:', error)
        return false
      }
    },
    
    logout() {
      this.clearToken()
      router.push('/login')
    },
    
    clearToken() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
  },
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.userInfo?.role === 'admin',
    username: (state) => state.userInfo?.username || '',
    realName: (state) => state.userInfo?.realName || '',
    role: (state) => state.userInfo?.role || ''
  }
}) 