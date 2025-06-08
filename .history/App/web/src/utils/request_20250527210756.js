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
    
    // 如果是文件流，直接返回
    if (response.config.responseType === 'blob') {
      return response;
    }
    
    // 正常响应
    if (res.code === 200 || res.code === 0) {
      return res;
    }
    
    // 未登录或token过期
    if (res.code === 401) {
      showToast('登录已过期，请重新登录');
      const userStore = useUserStore();
      userStore.clearToken();
      router.push('/login');
      return Promise.reject(new Error('未登录或登录已过期'));
    }
    
    // 其他错误
    const errorMsg = res.message || '操作失败';
    showToast(errorMsg);
    return Promise.reject(new Error(errorMsg));
  },
  error => {
    console.error('响应错误:', error);
    let message = '网络异常，请稍后重试';
    
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求参数错误';
          break;
        case 401:
          message = '未登录或登录已过期';
          const userStore = useUserStore();
          userStore.clearToken();
          router.push('/login');
          break;
        case 403:
          message = '没有权限访问';
          break;
        case 404:
          message = '请求的资源不存在';
          break;
        case 500:
          message = '服务器内部错误';
          break;
        default:
          message = error.response.data?.message || '操作失败';
      }
    }
    
    showToast(message);
    return Promise.reject(error);
  }
);

export default request; 