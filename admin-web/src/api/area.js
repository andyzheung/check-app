import request from '@/utils/request'

// 获取区域列表
export function getAreaList(params) {
  return request({
    url: '/areas',
    method: 'get',
    params
  })
}

// 获取区域详情
export function getAreaDetail(id) {
  return request({
    url: `/areas/${id}`,
    method: 'get'
  })
}

// 添加区域
export function addArea(data) {
  return request({
    url: '/areas',
    method: 'post',
    data
  })
}

// 更新区域
export function updateArea(id, data) {
  return request({
    url: `/areas/${id}`,
    method: 'put',
    data
  })
}

// 删除区域
export function deleteArea(id) {
  return request({
    url: `/areas/${id}`,
    method: 'delete'
  })
}

// 获取区域二维码
export function getAreaQrCode(id) {
  return request({
    url: `/areas/${id}/qrcode`,
    method: 'get',
    responseType: 'blob'
  })
}

// 生成区域二维码
export function generateAreaQrCode(id) {
  return request({
    url: `/areas/${id}/qrcode`,
    method: 'post'
  })
} 