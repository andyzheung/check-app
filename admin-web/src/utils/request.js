import axios from 'axios'
import { message } from 'ant-design-vue'
import router from '../router'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || '/api/v1', // 直接使用v1版本API
  timeout: 15000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// 清理过长的cookie，避免请求头过大
const cleanupCookies = () => {
  try {
    // 如果可能，完全移除JSESSIONID cookie，使用JWT token进行认证
    document.cookie = 'JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    
    // 移除其他不必要的cookie
    const cookies = document.cookie.split(';');
    for(let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i].trim();
      // 只保留必要的cookie
      if(cookie.length > 100 && !cookie.startsWith('token=')) {
        const name = cookie.split('=')[0];
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
      }
    }
  } catch (e) {
    console.error('清理cookie失败:', e);
  }
}

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 清理cookie以防止请求头过大
    cleanupCookies();
    
    // 设置token认证
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // 登录请求特殊处理，使用最简请求头
    if (config.url.includes('/auth/login')) {
      // 登录请求只保留必要的请求头
      config.headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      };
    }
    
    // 记录请求信息
    console.log('请求:', config.url, config.params || config.data || '无参数')
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    console.log('响应:', response.config.url, res)
    
    // 如果是登录接口的响应，确保响应处理正确
    if (response.config.url.includes('/auth/login')) {
      if (res.data && res.data.token) {
        return res.data;
      }
      if (res.token) {
        return res;
      }
    }
    
    // 检查响应中是否有code字段，按结构处理
    if (res.code !== undefined) {
      if (res.code !== 200) {
        message.error(res.message || '请求失败')
        
        // 401: 未登录或token过期
        if (res.code === 401) {
          localStorage.removeItem('token')
          router.push('/login')
        }
        
        return Promise.reject(new Error(res.message || '请求失败'))
      }
      return res.data || res
    }
    
    // 没有code字段，直接返回
    return res
  },
  error => {
    console.error('响应错误:', error.config?.url, error)
    
    // 处理431错误 - 请求头过大
    if (error.response?.status === 431) {
      console.error('请求头过大 (431)，尝试清理请求头并重试')
      message.error('请求数据过大，正在尝试优化...')
      
      // 强制清理cookie
      cleanupCookies();
      
      // 简化配置并重试
      const simpleConfig = { 
        ...error.config,
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      };
      
      // 只添加token
      const token = localStorage.getItem('token');
      if (token) {
        simpleConfig.headers['Authorization'] = `Bearer ${token}`;
      }
      
      return axios(simpleConfig);
    }
    
    // 其他错误处理
    const errorMsg = error.response?.data?.message || '请求失败，请稍后重试'
    message.error(errorMsg)
    
    // 401错误处理 - 未授权
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
    }
    
    return Promise.reject(error)
  }
)

export default service 