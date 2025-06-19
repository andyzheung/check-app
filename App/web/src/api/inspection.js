import request from '../utils/request'

/**
 * 获取所有巡检区域
 * @returns {Promise<any>}
 */
export function getAllAreas() {
  return request.get('/areas')
}

/**
 * 根据编号获取区域信息
 * @param {string} code - 区域编号
 * @returns {Promise<any>}
 */
export function getAreaByCode(code) {
  return request.get(`/areas/code/${code}`)
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
 * @param {number} params.size - 每页大小
 * @param {string} params.areaId - 区域ID
 * @param {string} params.status - 状态
 * @param {string} params.startDate - 开始日期
 * @param {string} params.endDate - 结束日期
 * @param {string} params.keyword - 关键字
 * @returns {Promise<any>}
 */
export function getRecordList(params) {
  console.log('调用记录列表API, 参数:', params);
  return request.get('/records', { params }).then(res => {
    console.log('记录列表API原始响应:', res);
    return res;
  }).catch(err => {
    console.error('记录列表API错误:', err);
    throw err;
  });
}

/**
 * 获取巡检记录详情
 * @param {string|number} id - 记录ID
 * @returns {Promise<any>}
 */
export function getRecordDetail(id) {
  return request.get(`/records/${id}`)
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
 * 生成区域二维码
 * @param {number} areaId - 区域ID
 * @returns {Promise<any>}
 */
export function generateQRCode(areaId) {
  return request.get(`/areas/${areaId}/qrcode`)
}

/**
 * 验证区域二维码
 * @param {string} qrData - 二维码数据
 * @returns {Promise<any>}
 */
export function verifyQRCode(qrData) {
  return request.post('/areas/verify', qrData)
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
 * 提交巡检路径
 * @param {string|number} id - 记录ID
 * @param {Array} routeData - 路径数据
 * @returns {Promise<any>}
 */
export function submitRecordRoute(id, routeData) {
  return request({
    url: `/records/${id}/route`,
    method: 'post',
    data: routeData
  });
}

/**
 * 获取巡检路径
 * @param {string|number} id - 记录ID
 * @returns {Promise<any>}
 */
export function getRecordRoute(id) {
  return request({
    url: `/records/${id}/route`,
    method: 'get'
  });
} 