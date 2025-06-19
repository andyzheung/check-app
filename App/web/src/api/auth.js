import request from '../utils/request';
import { useUserStore } from '@/stores/user';

export async function login(username, password) {
  try {
    const res = await request.post('/auth/login', { username, password });
    console.log('登录响应:', res);
    
    // 检查不同的响应结构
    const data = res.data || res;
    const token = data.data?.token || data.token;
    
    if (token) {
      const userStore = useUserStore();
      userStore.setToken(token);
      
      // 设置用户信息
      const userData = data.data || data;
      userStore.setUserInfo({
        userId: userData.userId,
        username: userData.username,
        realName: userData.realName,
        role: userData.role,
        avatar: userData.avatar
      });
      
      console.log('登录成功，token已设置:', token);
      return { success: true, data: userData };
    } else {
      console.error('登录响应中没有token:', res);
      return { success: false, message: data.message || '登录失败，未返回token' };
    }
  } catch (err) {
    console.error('登录错误:', err);
    return { success: false, message: err.message || '网络异常' };
  }
} 