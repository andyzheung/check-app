import axios from 'axios';
import { useUserStore } from '@/stores/user';

const service = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
});

// 请求拦截器
service.interceptors.request.use(
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
service.interceptors.response.use(
  response => {
    // 添加调试日志
    console.log('Response:', {
      status: response.status,
      data: response.data,
      headers: response.headers
    });
    
    const res = response.data;
    if (res.code && res.code !== 200) {
      // 处理业务错误
      console.error('业务错误:', res.message);
      return Promise.reject(new Error(res.message || '未知错误'));
    }
    return res;
  },
  error => {
    // 添加错误调试日志
    console.error('响应错误:', error);
    if (error.response && error.response.status === 401) {
      // 处理未授权错误
      const userStore = useUserStore();
      userStore.logout();
    }
    return Promise.reject(error);
  }
);

export default service; 