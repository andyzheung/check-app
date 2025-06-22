import request from '@/utils/request'

// 获取所有系统参数
export function getAllParams() {
  return request({
    url: '/api/v1/system/params',
    method: 'get'
  })
}

// 获取指定系统参数
export function getParam(key) {
  return request({
    url: `/api/v1/system/params/${key}`,
    method: 'get'
  })
}

// 设置系统参数
export function setParam(data) {
  return request({
    url: '/api/v1/system/params',
    method: 'post',
    data
  })
}

// 删除系统参数
export function deleteParam(key) {
  return request({
    url: `/api/v1/system/params/${key}`,
    method: 'delete'
  })
} 