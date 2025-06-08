import request from '../utils/request'

/**
 * 根据编号获取区域信息
 * @param {string} code - 区域编号
 * @returns {Promise<any>}
 */
export function getAreaByCode(code) {
  return request.get(`/v1/areas/${code}`)
}

/**
 * 根据区域ID获取巡检模板
 * @param {number} areaId - 区域ID
 * @returns {Promise<any>}
 */
export function getTemplateByAreaId(areaId) {
  return request.get(`/v1/templates/${areaId}`)
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
  return request.post('/v1/records', data)
}

/**
 * 获取巡检记录列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页大小
 * @param {number} [params.areaId] - 区域ID
 * @param {string} [params.status] - 状态
 * @param {string} [params.startDate] - 开始日期
 * @param {string} [params.endDate] - 结束日期
 * @returns {Promise<any>}
 */
export function getRecordList(params) {
  return request.get('/v1/records', { params })
}

/**
 * 获取巡检记录详情
 * @param {number|string} id - 记录ID
 * @returns {Promise<any>}
 */
export function getRecordDetail(id) {
  return request.get(`/v1/records/${id}`)
}

/**
 * 获取巡检点信息
 * @param {string} code - 巡检点编号
 * @returns {Promise<any>}
 */
export function getInspectionPoint(code) {
  return request.get(`/v1/points/${code}`)
} 