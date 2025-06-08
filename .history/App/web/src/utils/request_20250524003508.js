import axios from 'axios';
import { useUserStore } from '@/stores/user';

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000,
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
    console.log('Request:', {
      url: config.url,
      method: config.method,
      data: config.data,
      params: config.params
    });
    return config;
  },
  error => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data;
    console.log('Response:', {
      url: response.config.url,
      status: response.status,
      data: res
    });

    // 统一处理未登录情况
    if (res.code === 401) {
      const userStore = useUserStore();
      userStore.clearToken();
      window.location.href = '/login';
      return Promise.reject(new Error('未登录或登录已过期'));
    }

    // 统一返回格式：{ code, data, message }
    return {
      code: res.code,
      data: res.data,
      message: res.message
    };
  },
  error => {
    console.error('Response error:', error);
    const message = error.response?.data?.message || error.message || '网络错误';
    return Promise.reject(new Error(message));
  }
);

export default request; 