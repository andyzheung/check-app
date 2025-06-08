import request from '@/utils/request'

/**
 * 用户登录
 * @param {string} username 用户名
 * @param {string} password 密码
 * @returns {Promise} 请求结果
 */
export function login(username, password) {
  return request({
    url: '/auth/login',
    method: 'post',
    data: { username, password }
  })
}

/**
 * 用户登出
 * @returns {Promise} 请求结果
 */
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

/**
 * 获取当前用户信息
 * @returns {Promise} 请求结果
 */
export function getCurrentUser() {
  return request({
    url: '/users/current',
    method: 'get'
  })
}

/**
 * 修改密码
 * @param {Object} data 密码数据
 * @param {string} data.oldPassword 旧密码
 * @param {string} data.newPassword 新密码
 * @returns {Promise} 请求结果
 */
export function changePassword(data) {
  return request({
    url: '/users/password',
    method: 'put',
    data
  })
} 