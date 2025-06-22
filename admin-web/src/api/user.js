import request from '@/utils/request'

// 用户登录
export function login(data) {
  return request({
    url: '/api/v1/auth/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/api/v1/users/current',
    method: 'get'
  })
}

// 退出登录
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/api/v1/users/page',
    method: 'get',
    params
  })
}

// 获取用户列表（别名，用于兼容）
export function getUsers(params) {
  return request({
    url: '/api/v1/users/page',
    method: 'get',
    params
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/api/v1/users',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(id, data) {
  return request({
    url: `/api/v1/users/${id}`,
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/api/v1/users/${id}`,
    method: 'delete'
  })
}

// 获取用户权限
export function getUserPermissions(userId) {
  return request({
    url: `/api/v1/users/${userId}/permissions`,
    method: 'get'
  })
}

// 更新用户权限
export function updateUserPermissions(id, data) {
  return request({
    url: `/api/v1/users/${id}/permissions`,
    method: 'put',
    data
  })
}

// 获取所有权限列表
export function getAllPermissions() {
  return request({
    url: '/api/v1/permissions',
    method: 'get'
  })
} 