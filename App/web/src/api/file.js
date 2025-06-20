import request from '../utils/request'

/**
 * 上传文件
 * @param {FormData} formData - 包含文件的表单数据
 * @param {string} businessType - 业务类型
 * @param {string|number} businessId - 业务ID
 * @returns {Promise<any>}
 */
export function uploadFile(formData, businessType, businessId) {
  formData.append('businessType', businessType || '');
  formData.append('businessId', businessId || '');
  
  return request.post('/files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/**
 * 获取文件列表
 * @param {string} businessType - 业务类型
 * @param {string|number} businessId - 业务ID
 * @returns {Promise<any>}
 */
export function getFileList(businessType, businessId) {
  return request.get('/files/list', {
    params: {
      businessType,
      businessId
    }
  });
}

/**
 * 获取文件信息
 * @param {string|number} id - 文件ID
 * @returns {Promise<any>}
 */
export function getFileInfo(id) {
  return request.get(`/files/${id}`);
}

/**
 * 删除文件
 * @param {string|number} id - 文件ID
 * @returns {Promise<any>}
 */
export function deleteFile(id) {
  return request.delete(`/files/${id}`);
} 