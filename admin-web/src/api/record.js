import request from '@/utils/request'

// 获取巡检记录列表
export function getRecordList(params) {
  return request({
    url: '/records',
    method: 'get',
    params
  })
}

// 获取巡检记录详情
export function getRecordDetail(id) {
  return request({
    url: `/records/${id}`,
    method: 'get'
  })
}

// 获取统计数据
export function getRecordStats() {
  return request({
    url: '/statistics/dashboard',
    method: 'get'
  })
}

// 获取周趋势数据
export function getWeeklyTrend() {
  return request({
    url: '/statistics/inspection/weekly',
    method: 'get'
  })
}

// 获取问题分布数据
export function getIssueDistribution() {
  return request({
    url: '/statistics/issues',
    method: 'get'
  })
}

// 获取巡检人员排名
export function getInspectorRanking() {
  return request({
    url: '/statistics/inspectors/ranking',
    method: 'get'
  })
}

// 导出记录
export function exportRecords(params) {
  return request({
    url: '/records/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 获取导出历史
export function getExportHistory() {
  return request({
    url: '/records/export/history',
    method: 'get'
  })
} 