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
  // Mock用户列表
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockUsers = [
        { id: 1, username: 'admin', realname: '系统管理员', department: 'it', role: 'admin', status: 'active', createTime: '2024-01-01' },
        { id: 2, username: 'zhangsan', realname: '张三', department: 'ops', role: 'user', status: 'active', createTime: '2024-02-15' },
        { id: 3, username: 'lisi', realname: '李四', department: 'security', role: 'user', status: 'active', createTime: '2024-03-10' },
        { id: 4, username: 'wangwu', realname: '王五', department: 'ops', role: 'user', status: 'active', createTime: '2024-03-20' },
        { id: 5, username: 'zhaoliu', realname: '赵六', department: 'security', role: 'admin', status: 'active', createTime: '2024-04-05' }
      ]
      
      // 简单过滤
      let filteredUsers = [...mockUsers]
      if (params.username) {
        filteredUsers = filteredUsers.filter(user => user.username.includes(params.username))
      }
      if (params.department) {
        filteredUsers = filteredUsers.filter(user => user.department === params.department)
      }
      if (params.role) {
        filteredUsers = filteredUsers.filter(user => user.role === params.role)
      }
      if (params.status) {
        filteredUsers = filteredUsers.filter(user => user.status === params.status)
      }
      
      // 分页
      const pageSize = params.pageSize || 10
      const pageNum = params.pageNum || 1
      const total = filteredUsers.length
      const start = (pageNum - 1) * pageSize
      const end = start + pageSize
      const list = filteredUsers.slice(start, end)
      
      resolve({
        list,
        total
      })
    }, 300)
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
export function getUserPermissions(userId) {
  // Mock用户权限
  return new Promise((resolve) => {
    setTimeout(() => {
      const permissions = {
        1: ['dashboard', 'records_view', 'records_all', 'records_export', 'issues_view', 'issues_edit', 'user_manage', 'system_config'],
        2: ['dashboard', 'records_view', 'issues_view'],
        3: ['dashboard', 'records_view', 'issues_view'],
        4: ['dashboard', 'records_view', 'issues_view'],
        5: ['dashboard', 'records_view', 'records_all', 'records_export', 'issues_view', 'issues_edit', 'user_manage']
      }
      resolve(permissions[userId] || [])
    }, 300)
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