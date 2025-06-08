import request from '../utils/request';
import { useUserStore } from '@/stores/user';

export async function login(username, password) {
  try {
    const res = await request.post('/auth/login', { username, password });
    if (res.data && res.data.token) {
      const userStore = useUserStore();
      userStore.setToken(res.data.token);
      userStore.setUserInfo({
        userId: res.data.userId,
        username: res.data.username,
        realName: res.data.realName,
        role: res.data.role,
        avatar: res.data.avatar
      });
      return { success: true, data: res.data };
    } else {
      return { success: false, message: '登录失败' };
    }
  } catch (err) {
    console.error('登录错误:', err);
    return { success: false, message: err.message || '网络异常' };
  }
} 