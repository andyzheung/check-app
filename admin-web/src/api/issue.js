import request from '@/utils/request'

// 获取问题列表
export function getIssueList(params) {
  return request({
    url: '/issues',
    method: 'get',
    params
  })
}

// 获取问题详情
export function getIssueDetail(id) {
  return request({
    url: `/issues/${id}`,
    method: 'get'
  })
}

// 更新问题状态
export function updateIssueStatus(id, data) {
  return request({
    url: `/issues/${id}/status`,
    method: 'put',
    data
  })
}

// 添加问题处理记录
export function addIssueProcess(id, data) {
  return request({
    url: `/issues/${id}/process`,
    method: 'post',
    data
  })
}

// 处理问题（状态更新和添加处理记录）
export function processIssue(id, data) {
  return request({
    url: `/issues/${id}/process`,
    method: 'post',
    data
  })
}

// 导出问题列表
export function exportIssues(params) {
  return request({
    url: '/issues/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 获取问题统计数据
export function getIssueStatistics() {
  return request({
    url: '/statistics/issues',
    method: 'get'
  })
}

// 获取问题趋势数据
export function getIssueTrend(timeRange) {
  return request({
    url: '/statistics/issues/trend',
    method: 'get',
    params: { timeRange }
  })
}

// 获取问题区域分布
export function getIssueByArea() {
  return request({
    url: '/statistics/issues/by-area',
    method: 'get'
  })
}

// 获取问题处理人员排名
export function getIssueByHandler() {
  return request({
    url: '/statistics/issues/by-handler',
    method: 'get'
  })
}

// 获取本周问题
export function getWeeklyIssues() {
  return request({
    url: '/issues/weekly',
    method: 'get'
  })
} 