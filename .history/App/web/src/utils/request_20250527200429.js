import axios from 'axios';
import { useUserStore } from '@/stores/user';
import { showToast } from 'vant';
import router from '@/router';

// 创建axios实例
const request = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
    'Accept': 'application/json;charset=utf-8'
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
    if (res.code === 200) {
      return res;
    }
    
    // 未登录或token过期
    if (res.code === 401) {
      showToast('登录已过期，请重新登录');
      const userStore = useUserStore();
      userStore.logout();
      router.push({
        path: '/login',
        query: { redirect: router.currentRoute.value.fullPath }
      });
      return Promise.reject(new Error('未登录或登录已过期'));
    }
    
    // 其他错误
    showToast(res.message || '操作失败');
    return Promise.reject(new Error(res.message || '操作失败'));
  },
  error => {
    console.error('响应错误:', error);
    const message = error.response?.data?.message || '网络异常，请稍后重试';
    showToast(message);
    return Promise.reject(error);
  }
);

export default request; 