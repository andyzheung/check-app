import axios from 'axios';
import { useUserStore } from '@/stores/user';

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
request.interceptors.request.use(
  config => {
    const userStore = useUserStore();
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`;
    }
    // 添加调试日志
    console.log('Request Config:', {
      url: config.url,
      method: config.method,
      baseURL: config.baseURL,
      headers: config.headers,
      data: config.data,
      params: config.params
    });
    return config;
  },
  error => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 添加调试日志
    console.log('Response:', {
      status: response.status,
      data: response.data,
      headers: response.headers
    });
    
    const res = response.data;
    // Spring Boot 后端返回的数据结构可能不同，需要调整
    if (res.code === 200 || res.code === 0) {
      return res;
    }
    // 401: 未登录或token过期
    if (res.code === 401) {
      const userStore = useUserStore();
      userStore.clearToken();
      window.location.href = '/login';
      return Promise.reject(new Error('未登录或登录已过期'));
    }
    return Promise.reject(new Error(res.message || '请求失败'));
  },
  error => {
    // 添加错误调试日志
    console.error('Response Error:', {
      message: error.message,
      config: error.config,
      response: error.response?.data
    });
    
    if (error.response) {
      // 服务器返回了错误状态码
      const status = error.response.status;
      switch (status) {
        case 400:
          return Promise.reject(new Error('请求参数错误'));
        case 401:
          const userStore = useUserStore();
          userStore.clearToken();
          window.location.href = '/login';
          return Promise.reject(new Error('未登录或登录已过期'));
        case 403:
          return Promise.reject(new Error('没有权限执行此操作'));
        case 404:
          return Promise.reject(new Error('请求的资源不存在'));
        case 500:
          return Promise.reject(new Error('服务器内部错误'));
        default:
          return Promise.reject(new Error('请求失败，请稍后重试'));
      }
    }
    return Promise.reject(new Error('网络异常，请检查网络连接'));
  }
);

export default request; 