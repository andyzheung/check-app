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

// 获取本周问题列表
export function getWeeklyIssues() {
  return request({
    url: '/issues/weekly',
    method: 'get'
  })
} 