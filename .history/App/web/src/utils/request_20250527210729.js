import axios from 'axios';
import { useUserStore } from '@/stores/user';
import { showToast } from 'vant';
import router from '@/router';

// 创建axios实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
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
    if (res.code !== 0) {
      console.error('接口错误:', res.message);
      // 如果是未登录，跳转到登录页
      if (res.code === 401) {
        const userStore = useUserStore();
        userStore.clearToken();
        window.location.href = '/login';
      }
      return Promise.reject(new Error(res.message || '接口错误'));
    }
    return response;
  },
  error => {
    console.error('响应错误:', error);
    return Promise.reject(error);
  }
);

export default request; 