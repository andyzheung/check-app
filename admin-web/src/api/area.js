import request from '@/utils/request'

/**
 * 获取区域列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAreas(params) {
  return request({
    url: '/api/v1/areas',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取区域详情
 * @param {number} id 区域ID
 * @returns {Promise}
 */
export function getAreaById(id) {
  return request({
    url: `/api/v1/areas/${id}`,
    method: 'get'
  })
}

/**
 * 根据编码获取区域详情
 * @param {string} code 区域编码
 * @returns {Promise}
 */
export function getAreaByCode(code) {
  return request({
    url: `/api/v1/areas/code/${code}`,
    method: 'get'
  })
}

/**
 * 更新区域配置
 * @param {number} id 区域ID
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function updateAreaConfig(id, data) {
  return request({
    url: `/api/v1/areas/${id}/config`,
    method: 'put',
    data
  })
}

/**
 * 创建新区域
 * @param {Object} data 区域数据
 * @returns {Promise}
 */
export function createArea(data) {
  return request({
    url: '/api/v1/areas',
    method: 'post',
    data
  })
}

/**
 * 更新区域
 * @param {number} id 区域ID
 * @param {Object} data 区域数据
 * @returns {Promise}
 */
export function updateArea(id, data) {
  return request({
    url: `/api/v1/areas/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除区域
 * @param {number} id 区域ID
 * @returns {Promise}
 */
export function deleteArea(id) {
  return request({
    url: `/api/v1/areas/${id}`,
    method: 'delete'
  })
}

/**
 * 生成区域二维码
 * @param {number} id 区域ID
 * @returns {Promise}
 */
export function generateQRCode(id) {
  return request({
    url: `/api/v1/areas/${id}/qrcode`,
    method: 'post'
  })
} 