import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/utils/request'

export const useUserStore = defineStore('user', () => {
  const token = ref('')
  const userInfo = ref(null)

  const login = async (username, password) => {
    try {
      const res = await request.post('/login', { username, password })
      token.value = res.token
      userInfo.value = res.user
      localStorage.setItem('token', res.token)
      return res
    } catch (error) {
      console.error('登录失败:', error)
      throw error
    }
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  const getUserInfo = async () => {
    try {
      const res = await request.get('/user/profile')
      userInfo.value = res
      return res
    } catch (error) {
      console.error('获取用户信息失败:', error)
      throw error
    }
  }

  // 初始化时从 localStorage 恢复 token
  const initToken = () => {
    const savedToken = localStorage.getItem('token')
    if (savedToken) {
      token.value = savedToken
    }
  }

  return {
    token,
    userInfo,
    login,
    logout,
    getUserInfo,
    initToken
  }
}) 