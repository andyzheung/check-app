import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref({
    id: null,
    username: '',
    realName: '',
    role: '',
    permissions: []
  })

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value.role === 'admin')

  // 方法
  const setToken = (newToken) => {
    token.value = newToken
    if (newToken) {
      localStorage.setItem('token', newToken)
    } else {
      localStorage.removeItem('token')
    }
  }

  const setUserInfo = (info) => {
    userInfo.value = { ...userInfo.value, ...info }
  }

  const logout = () => {
    token.value = ''
    userInfo.value = {
      id: null,
      username: '',
      realName: '',
      role: '',
      permissions: []
    }
    localStorage.removeItem('token')
  }

  const hasPermission = (permission) => {
    if (isAdmin.value) return true
    return userInfo.value.permissions.includes(permission)
  }

  return {
    // 状态
    token,
    userInfo,
    // 计算属性
    isLoggedIn,
    isAdmin,
    // 方法
    setToken,
    setUserInfo,
    logout,
    hasPermission
  }
}) 