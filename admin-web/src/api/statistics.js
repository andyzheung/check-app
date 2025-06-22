import request from '@/utils/request'

// 获取每日巡检统计
export function getDailyInspectionStatistics(date) {
  return request({
    url: '/api/v1/statistics/inspection/daily',
    method: 'get',
    params: { date }
  })
}

// 获取每周巡检统计
export function getWeeklyInspectionStatistics(startDate, endDate) {
  return request({
    url: '/api/v1/statistics/inspection/weekly',
    method: 'get',
    params: { startDate, endDate }
  })
}

// 获取问题统计
export function getIssueStatistics() {
  return request({
    url: '/api/v1/statistics/issues',
    method: 'get'
  })
}

// 获取仪表盘数据
export function getDashboardData() {
  return request({
    url: '/api/v1/statistics/dashboard',
    method: 'get'
  })
}

// 刷新统计缓存
export function refreshStatisticsCache(type) {
  return request({
    url: `/api/v1/statistics/refresh/${type}`,
    method: 'post'
  })
} 