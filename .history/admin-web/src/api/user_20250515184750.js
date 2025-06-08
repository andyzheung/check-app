import request from '@/utils/request'

// 用户登录
export function login(data) {
  // Mock登录功能
  return new Promise((resolve) => {
    // 固定的用户名和密码
    if (data.username === 'admin' && data.password === '123456') {
      setTimeout(() => {
        resolve({
          token: 'mock-token-for-admin-user',
          user: {
            id: 1,
            username: 'admin',
            name: '管理员',
            role: 'admin'
          }
        })
      }, 500)
    } else {
      // 登录失败
      throw new Error('用户名或密码错误')
    }
  })
}

// 获取用户信息
export function getUserInfo() {
  // Mock用户信息
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        id: 1,
        username: 'admin',
        name: '管理员',
        role: 'admin',
        permissions: ['dashboard', 'records_view', 'records_all', 'records_export', 'issues_view', 'issues_edit', 'user_manage', 'system_config']
      })
    }, 300)
  })
}

// 退出登录
export function logout() {
  // Mock登出
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({})
    }, 300)
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(id, data) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

// 获取用户权限
export function getUserPermissions(id) {
  return request({
    url: `/users/${id}/permissions`,
    method: 'get'
  })
}

// 更新用户权限
export function updateUserPermissions(id, data) {
  return request({
    url: `/users/${id}/permissions`,
    method: 'put',
    data
  })
} 