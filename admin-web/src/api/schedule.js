import request from '@/utils/request'

/**
 * 获取任务列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTasks(params) {
  return request({
    url: '/api/v1/schedule/tasks',
    method: 'get',
    params
  })
}

/**
 * 创建任务
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function createTask(data) {
  return request({
    url: '/api/v1/schedule/tasks',
    method: 'post',
    data
  })
}

/**
 * 更新任务
 * @param {number} taskId 任务ID
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function updateTask(taskId, data) {
  return request({
    url: `/api/v1/schedule/tasks/${taskId}`,
    method: 'put',
    data
  })
}

/**
 * 删除任务
 * @param {number} taskId 任务ID
 * @returns {Promise}
 */
export function deleteTask(taskId) {
  return request({
    url: `/api/v1/schedule/tasks/${taskId}`,
    method: 'delete'
  })
}

/**
 * 获取任务详情
 * @param {number} taskId 任务ID
 * @returns {Promise}
 */
export function getTaskDetail(taskId) {
  return request({
    url: `/api/v1/schedule/tasks/${taskId}`,
    method: 'get'
  })
}

/**
 * 检查排班冲突
 * @param {Object} params 检查参数
 * @returns {Promise}
 */
export function checkScheduleConflict(params) {
  return request({
    url: '/api/v1/schedule/conflict-check',
    method: 'get',
    params
  })
}

/**
 * 获取日历数据
 * @param {Object} params 日期范围参数
 * @returns {Promise}
 */
export function getCalendarData(params) {
  return request({
    url: '/api/v1/schedule/calendar',
    method: 'get',
    params
  })
}

/**
 * 批量创建任务
 * @param {Array} data 任务列表
 * @returns {Promise}
 */
export function batchCreateTasks(data) {
  return request({
    url: '/api/v1/schedule/tasks/batch',
    method: 'post',
    data
  })
}

/**
 * 获取巡检员统计
 * @param {Object} params 统计参数
 * @returns {Promise}
 */
export function getInspectorStats(params) {
  return request({
    url: '/api/v1/schedule/inspector-stats',
    method: 'get',
    params
  })
} 