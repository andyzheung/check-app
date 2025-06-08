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

// 导出巡检记录
export function exportRecords(params) {
  return request({
    url: '/records/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 获取巡检统计数据
export function getRecordStats() {
  return request({
    url: '/records/stats',
    method: 'get'
  })
}

// 获取本周巡检趋势
export function getWeeklyTrend() {
  return request({
    url: '/records/weekly-trend',
    method: 'get'
  })
}

// 获取问题类型分布
export function getIssueDistribution() {
  return request({
    url: '/records/issue-distribution',
    method: 'get'
  })
}

// 获取巡检人员排名
export function getInspectorRanking() {
  return request({
    url: '/records/inspector-ranking',
    method: 'get'
  })
} 