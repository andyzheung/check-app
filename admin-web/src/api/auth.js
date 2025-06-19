import request from '@/utils/request'

/**
 * 本地账号登录
 * @param {Object} data - 登录信息
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 * @returns {Promise} - 返回登录结果
 */
export function login(data) {
  return request({
    url: '/api/v1/auth/login',
    method: 'post',
    data
  })
}

/**
 * AD域账号登录
 * @param {Object} data - 登录信息
 * @param {string} data.username - AD账号
 * @param {string} data.password - 密码
 * @returns {Promise} - 返回登录结果
 */
export function adLogin(data) {
  return request({
    url: '/api/v1/auth/ad-login',
    method: 'post',
    data
  })
}

/**
 * 登出
 * @returns {Promise} - 返回登出结果
 */
export function logout() {
  return request({
    url: '/api/v1/auth/logout',
    method: 'post'
  })
}

/**
 * 获取当前用户信息
 * @returns {Promise} - 返回用户信息
 */
export function getUserInfo() {
  return request({
    url: '/api/v1/auth/user-info',
    method: 'get'
  })
}

/**
 * 刷新Token
 * @returns {Promise} - 返回新的Token
 */
export function refreshToken() {
  return request({
    url: '/api/v1/auth/refresh-token',
    method: 'post'
  })
} 