import axios from 'axios';

const service = axios.create({
  baseURL: '/api',
  timeout: 10000
});

service.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token;
  }
  return config;
});

service.interceptors.response.use(
  response => {
    if (response.data && response.data.code && response.data.code !== 200) {
      if (response.data.code === 401) {
        handleLogout('登录已过期，请重新登录');
      }
      return Promise.reject(response.data.message || '请求失败');
    }
    return response;
  },
  error => {
    if (error.response && error.response.status === 401) {
      handleLogout('登录已过期，请重新登录');
    }
    return Promise.reject(error);
  }
);

function handleLogout(msg) {
  localStorage.removeItem('token');
  alert(msg || '登录已过期，请重新登录');
  window.location.href = '/login';
}

export default service; 