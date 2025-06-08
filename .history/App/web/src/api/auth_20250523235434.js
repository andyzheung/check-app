import request from '../utils/request';

export async function login(username, password) {
  try {
    const res = await request.post('/v1/login', { username, password });
    if (res.data && res.data.token) {
      localStorage.setItem('token', res.data.token);
      return { success: true, data: res.data };
    } else {
      return { success: false, message: res.data?.message || '登录失败' };
    }
  } catch (err) {
    return { success: false, message: err.message || '网络异常' };
  }
} 