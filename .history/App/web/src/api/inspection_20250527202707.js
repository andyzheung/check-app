import request from '../utils/request'

/**
 * 根据编号获取区域信息
 * @param {string} code - 区域编号
 * @returns {Promise<any>}
 */
export function getAreaByCode(code) {
  return request.get(`/areas/${code}`)
}

/**
 * 根据区域ID获取巡检模板
 * @param {number} areaId - 区域ID
 * @returns {Promise<any>}
 */
export function getTemplateByAreaId(areaId) {
  return request.get(`/templates/${areaId}`)
}

/**
 * 提交巡检记录
 * @param {Object} data - 巡检记录数据
 * @param {string} data.recordNo - 记录编号
 * @param {number} data.areaId - 区域ID
 * @param {string} data.status - 状态
 * @param {Array} data.environmentItems - 环境巡检项
 * @param {Array} data.securityItems - 安全巡检项
 * @param {string} data.remark - 备注
 * @returns {Promise<any>}
 */
export function createRecord(data) {
  return request.post('/records', data)
}

/**
 * 获取巡检记录列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页大小
 * @returns {Promise<any>}
 */
export function getRecordList(params) {
  return request({
    url: '/records',
    method: 'get',
    params
  });
}

/**
 * 获取巡检记录详情
 * @param {string|number} id - 记录ID
 * @returns {Promise<any>}
 */
export function getRecordDetail(id) {
  return request({
    url: `/records/${id}`,
    method: 'get'
  });
}

/**
 * 获取巡检点信息
 * @param {string} code - 巡检点编号
 * @returns {Promise<any>}
 */
export function getInspectionPoint(code) {
  return request.get(`/points/${code}`)
}

/**
 * 更新巡检记录
 * @param {string|number} id - 记录ID
 * @param {Object} data - 更新数据
 * @returns {Promise<any>}
 */
export function updateRecord(id, data) {
  return request({
    url: `/records/${id}`,
    method: 'put',
    data
  });
}

/**
 * 删除巡检记录
 * @param {string|number} id - 记录ID
 * @returns {Promise<any>}
 */
export function deleteRecord(id) {
  return request({
    url: `/records/${id}`,
    method: 'delete'
  });
}

/**
 * 导出巡检记录
 * @param {Object} params - 查询参数
 * @returns {Promise<Blob>}
 */
export function exportRecords(params) {
  return request({
    url: '/records/export',
    method: 'get',
    params,
    responseType: 'blob'
  });
}

/**
 * 根据区域编码获取区域信息
 * @param {string} areaCode - 区域编码
 * @returns {Promise<any>}
 */
export function getAreaByCode(areaCode) {
  return request({
    url: `/areas/code/${areaCode}`,
    method: 'get'
  });
}

/**
 * 验证区域二维码
 * @param {Object} data - 二维码数据
 * @returns {Promise<any>}
 */
export function verifyQRCode(data) {
  return request({
    url: '/areas/verify',
    method: 'post',
    data
  });
} 