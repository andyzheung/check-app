import request from '../utils/request'

/**
 * 获取问题列表
 * @param {Object} params - 查询参数
 * @returns {Promise<any>}
 */
export function getIssueList(params) {
  return request.get('/issues', { params });
}

/**
 * 获取问题详情
 * @param {string|number} id - 问题ID
 * @returns {Promise<any>}
 */
export function getIssueDetail(id) {
  return request.get(`/issues/${id}`);
}

/**
 * 创建问题
 * @param {Object} data - 问题数据
 * @returns {Promise<any>}
 */
export function createIssue(data) {
  return request.post('/issues', data);
}

/**
 * 更新问题
 * @param {string|number} id - 问题ID
 * @param {Object} data - 更新数据
 * @returns {Promise<any>}
 */
export function updateIssue(id, data) {
  return request.put(`/issues/${id}`, data);
}

/**
 * 处理问题
 * @param {string|number} id - 问题ID
 * @param {Object} data - 处理数据
 * @returns {Promise<any>}
 */
export function processIssue(id, data) {
  return request.post(`/issues/${id}/process`, data);
}

/**
 * 关闭问题
 * @param {string|number} id - 问题ID
 * @param {Object} data - 关闭数据
 * @returns {Promise<any>}
 */
export function closeIssue(id, data) {
  return request.post(`/issues/${id}/close`, data);
}

/**
 * 上传问题处理图片
 * @param {string|number} id - 问题处理ID
 * @param {FormData} formData - 包含文件的表单数据
 * @returns {Promise<any>}
 */
export function uploadIssueProcessImages(id, formData) {
  return request.post(`/issue-processes/${id}/images`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/**
 * 获取问题处理图片
 * @param {string|number} id - 问题处理ID
 * @returns {Promise<any>}
 */
export function getIssueProcessImages(id) {
  return request.get(`/issue-processes/${id}/images`);
}

/**
 * 删除问题处理图片
 * @param {string|number} id - 问题处理ID
 * @param {string|number} imageId - 图片ID
 * @returns {Promise<any>}
 */
export function deleteIssueProcessImage(id, imageId) {
  return request.delete(`/issue-processes/${id}/images/${imageId}`);
} 