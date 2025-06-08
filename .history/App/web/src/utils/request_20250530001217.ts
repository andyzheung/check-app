import axios from 'axios'
import { showToast } from 'vant'

// 创建axios实例
const request = axios.create({
  baseURL: '', // 移除baseURL，因为后端已经包含了/api/v1
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // 如果响应成功
    if (res.code === 200 || res.code === 0) {
      return res
    }
    // 如果响应失败
    showToast({
      message: res.message || '请求失败',
      type: 'fail'
    })
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    console.error('响应错误:', error)
    showToast({
      message: error.message || '网络错误',
      type: 'fail'
    })
    return Promise.reject(error)
  }
)

export default request 