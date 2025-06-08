import request from '@/utils/request'

// 获取问题列表
export function getIssueList(params) {
  // Mock问题列表
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockIssues = [
        { id: 'ISS20240510001', description: '机房温度异常', type: 'environment', createTime: '2024-05-10 09:30', recorderName: '张三', status: 'open' },
        { id: 'ISS20240509002', description: '门禁系统异常', type: 'security', createTime: '2024-05-09 14:20', recorderName: '李四', status: 'closed' },
        { id: 'ISS20240508003', description: '漏水检测报警', type: 'environment', createTime: '2024-05-08 10:15', recorderName: '王五', status: 'open' },
        { id: 'ISS20240507004', description: '空调系统异常', type: 'device', createTime: '2024-05-07 16:45', recorderName: '赵六', status: 'closed' },
        { id: 'ISS20240506005', description: 'UPS电池老化', type: 'device', createTime: '2024-05-06 11:30', recorderName: '张三', status: 'open' }
      ]
      
      // 简单过滤
      let filteredIssues = [...mockIssues]
      if (params.startTime && params.endTime) {
        // 简单日期过滤，实际应该使用日期对象比较
        filteredIssues = filteredIssues.filter(issue => {
          const issueDate = issue.createTime.split(' ')[0]
          return issueDate >= params.startTime && issueDate <= params.endTime
        })
      }
      if (params.type) {
        filteredIssues = filteredIssues.filter(issue => issue.type === params.type)
      }
      if (params.recorderId) {
        const recorderMap = {
          2: '张三',
          3: '李四',
          4: '王五',
          5: '赵六'
        }
        filteredIssues = filteredIssues.filter(issue => issue.recorderName === recorderMap[params.recorderId])
      }
      if (params.status) {
        filteredIssues = filteredIssues.filter(issue => issue.status === params.status)
      }
      
      // 分页
      const pageSize = params.pageSize || 10
      const pageNum = params.pageNum || 1
      const total = filteredIssues.length
      const start = (pageNum - 1) * pageSize
      const end = start + pageSize
      const list = filteredIssues.slice(start, end)
      
      resolve({
        list,
        total
      })
    }, 300)
  })
}

// 获取问题详情
export function getIssueDetail(id) {
  // Mock问题详情
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockDetails = {
        'ISS20240510001': {
          id: 'ISS20240510001',
          description: '机房温度异常',
          type: 'environment',
          createTime: '2024-05-10 09:30',
          recorderName: '张三',
          status: 'open',
          detail: '机房温度达到28℃，超过设定阈值25℃，需要检查空调系统。',
          processRecords: [
            { action: 'create', time: '2024-05-10 09:30', processor: '张三', content: '巡检发现机房温度异常，已通知运维人员。' }
          ]
        },
        'ISS20240509002': {
          id: 'ISS20240509002',
          description: '门禁系统异常',
          type: 'security',
          createTime: '2024-05-09 14:20',
          recorderName: '李四',
          status: 'closed',
          detail: '门禁系统无法正常读取卡片信息，需要检查读卡器。',
          processRecords: [
            { action: 'create', time: '2024-05-09 14:20', processor: '李四', content: '发现门禁系统异常，无法正常读卡。' },
            { action: 'process', time: '2024-05-09 16:30', processor: '王五', content: '已检查门禁系统，发现读卡器接触不良。' },
            { action: 'close', time: '2024-05-10 10:15', processor: '王五', content: '已更换门禁读卡器，系统恢复正常。' }
          ]
        },
        'ISS20240508003': {
          id: 'ISS20240508003',
          description: '漏水检测报警',
          type: 'environment',
          createTime: '2024-05-08 10:15',
          recorderName: '王五',
          status: 'open',
          detail: '机房漏水检测系统报警，需要检查是否有漏水情况。',
          processRecords: [
            { action: 'create', time: '2024-05-08 10:15', processor: '王五', content: '漏水检测系统报警，已通知相关人员。' },
            { action: 'process', time: '2024-05-08 11:30', processor: '张三', content: '初步检查未发现明显漏水，可能是传感器误报。' }
          ]
        },
        'ISS20240507004': {
          id: 'ISS20240507004',
          description: '空调系统异常',
          type: 'device',
          createTime: '2024-05-07 16:45',
          recorderName: '赵六',
          status: 'closed',
          detail: '空调系统运行异常，无法正常调节温度。',
          processRecords: [
            { action: 'create', time: '2024-05-07 16:45', processor: '赵六', content: '空调系统无法正常工作，室内温度升高。' },
            { action: 'process', time: '2024-05-08 09:20', processor: '李四', content: '已联系空调维修人员。' },
            { action: 'close', time: '2024-05-08 14:30', processor: '李四', content: '空调系统已修复，恢复正常运行。' }
          ]
        },
        'ISS20240506005': {
          id: 'ISS20240506005',
          description: 'UPS电池老化',
          type: 'device',
          createTime: '2024-05-06 11:30',
          recorderName: '张三',
          status: 'open',
          detail: 'UPS系统电池老化，备用时间不足，需要更换电池。',
          processRecords: [
            { action: 'create', time: '2024-05-06 11:30', processor: '张三', content: 'UPS系统测试显示电池备用时间不足30分钟，低于标准要求。' },
            { action: 'process', time: '2024-05-07 10:15', processor: '王五', content: '已申请采购新电池，等待审批。' }
          ]
        }
      }
      
      resolve(mockDetails[id] || {})
    }, 300)
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
  // Mock处理问题
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ success: true })
    }, 300)
  })
}

// 导出问题列表
export function exportIssues(params) {
  // Mock导出功能
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ success: true })
    }, 300)
  })
}

// 获取本周问题列表
export function getWeeklyIssues() {
  // Mock本周问题
  return new Promise((resolve) => {
    setTimeout(() => {
      const weeklyIssues = [
        { id: 'ISS20240510001', description: '机房温度异常', type: 'environment', createTime: '2024-05-10', recorderName: '张三', status: 'open' },
        { id: 'ISS20240509002', description: '门禁系统异常', type: 'security', createTime: '2024-05-09', recorderName: '李四', status: 'closed' },
        { id: 'ISS20240508003', description: '漏水检测报警', type: 'environment', createTime: '2024-05-08', recorderName: '王五', status: 'open' },
        { id: 'ISS20240507004', description: '空调系统异常', type: 'device', createTime: '2024-05-07', recorderName: '赵六', status: 'closed' }
      ]
      resolve(weeklyIssues)
    }, 300)
  })
} 