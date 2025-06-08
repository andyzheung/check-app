import request from '../utils/request'

// 根据编号获取区域信息
export function getAreaByCode(code) {
  return request.get('/api/v1/areas/' + code)
}

// 根据区域ID获取巡检模板
export function getTemplateByAreaId(areaId) {
  return request.get('/api/v1/templates/' + areaId)
}

// 提交巡检记录
export function createRecord(data) {
  return request.post('/api/v1/records', data)
}

// 获取巡检记录列表
export function getRecordList(params) {
  return request.get('/api/v1/records', { params })
}

// 获取巡检记录详情
export function getRecordDetail(id) {
  return request.get('/api/v1/records/' + id)
}

export async function getInspectionPoint(code) {
  // mock数据，实际开发请替换为真实接口
  return Promise.resolve({
    data: {
      data: {
        code,
        id: 1,
        name: '测试巡检点'
      }
    }
  });
} 