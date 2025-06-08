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
    const res = response.data;
    if (res.code === 0) {
      return res;
    }
    // 401: 未登录或token过期
    if (res.code === 401) {
      const userStore = useUserStore();
      userStore.clearToken();
      window.location.href = '/login';
    }
    return Promise.reject(new Error(res.message || '请求失败'));
  },
  error => {
    console.error('响应错误:', error);
    return Promise.reject(error);
  }
);

export default request; 