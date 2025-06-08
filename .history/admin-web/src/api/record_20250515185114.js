import request from '@/utils/request'

// 获取巡检记录列表
export function getRecordList(params) {
  // Mock巡检记录列表
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockRecords = [
        { id: 'R20240510001', areaName: '机房A', inspectorName: '张三', inspectionTime: '2024-05-10 09:00', status: 'normal' },
        { id: 'R20240509002', areaName: '机房B', inspectorName: '李四', inspectionTime: '2024-05-09 14:00', status: 'abnormal' },
        { id: 'R20240508003', areaName: '机房C', inspectorName: '王五', inspectionTime: '2024-05-08 10:00', status: 'abnormal' },
        { id: 'R20240507004', areaName: '机房A', inspectorName: '赵六', inspectionTime: '2024-05-07 16:00', status: 'normal' },
        { id: 'R20240506005', areaName: '机房B', inspectorName: '张三', inspectionTime: '2024-05-06 11:00', status: 'normal' }
      ]
      
      // 简单过滤
      let filteredRecords = [...mockRecords]
      if (params.startTime && params.endTime) {
        // 简单日期过滤
        filteredRecords = filteredRecords.filter(record => {
          const recordDate = record.inspectionTime.split(' ')[0]
          return recordDate >= params.startTime && recordDate <= params.endTime
        })
      }
      if (params.areaId) {
        const areaMap = {
          1: '机房A',
          2: '机房B',
          3: '机房C'
        }
        filteredRecords = filteredRecords.filter(record => record.areaName === areaMap[params.areaId])
      }
      if (params.inspectorId) {
        const inspectorMap = {
          2: '张三',
          3: '李四',
          4: '王五',
          5: '赵六'
        }
        filteredRecords = filteredRecords.filter(record => record.inspectorName === inspectorMap[params.inspectorId])
      }
      if (params.status) {
        filteredRecords = filteredRecords.filter(record => record.status === params.status)
      }
      
      // 分页
      const pageSize = params.pageSize || 10
      const pageNum = params.pageNum || 1
      const total = filteredRecords.length
      const start = (pageNum - 1) * pageSize
      const end = start + pageSize
      const list = filteredRecords.slice(start, end)
      
      resolve({
        list,
        total
      })
    }, 300)
  })
}

// 获取巡检记录详情
export function getRecordDetail(id) {
  // Mock巡检记录详情
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockDetails = {
        'R20240510001': {
          id: 'R20240510001',
          areaName: '机房A',
          inspectorName: '张三',
          inspectionTime: '2024-05-10 09:00',
          status: 'normal',
          remark: '巡检正常，设备运行良好。',
          environmentItems: [
            { name: '温度', status: 'normal', remark: '23℃，正常范围内' },
            { name: '湿度', status: 'normal', remark: '45%，正常范围内' },
            { name: '噪音', status: 'normal', remark: '65dB，正常范围内' }
          ],
          securityItems: [
            { name: '门禁', status: 'normal', remark: '正常工作' },
            { name: '监控', status: 'normal', remark: '正常工作' },
            { name: '消防设备', status: 'normal', remark: '正常工作' }
          ]
        },
        'R20240509002': {
          id: 'R20240509002',
          areaName: '机房B',
          inspectorName: '李四',
          inspectionTime: '2024-05-09 14:00',
          status: 'abnormal',
          remark: '发现门禁系统异常。',
          environmentItems: [
            { name: '温度', status: 'normal', remark: '24℃，正常范围内' },
            { name: '湿度', status: 'normal', remark: '48%，正常范围内' },
            { name: '噪音', status: 'normal', remark: '68dB，正常范围内' }
          ],
          securityItems: [
            { name: '门禁', status: 'abnormal', remark: '门禁系统无法正常读卡' },
            { name: '监控', status: 'normal', remark: '正常工作' },
            { name: '消防设备', status: 'normal', remark: '正常工作' }
          ]
        },
        'R20240508003': {
          id: 'R20240508003',
          areaName: '机房C',
          inspectorName: '王五',
          inspectionTime: '2024-05-08 10:00',
          status: 'abnormal',
          remark: '发现漏水检测系统报警。',
          environmentItems: [
            { name: '温度', status: 'normal', remark: '22℃，正常范围内' },
            { name: '湿度', status: 'abnormal', remark: '65%，高于正常范围' },
            { name: '噪音', status: 'normal', remark: '64dB，正常范围内' }
          ],
          securityItems: [
            { name: '门禁', status: 'normal', remark: '正常工作' },
            { name: '监控', status: 'normal', remark: '正常工作' },
            { name: '消防设备', status: 'normal', remark: '正常工作' }
          ]
        }
      }
      
      resolve(mockDetails[id] || {})
    }, 300)
  })
}

// 获取统计数据
export function getRecordStats() {
  // Mock统计数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        totalInspections: 1280,
        totalInspectors: 256,
        totalIssues: 80,
        activeInspectors: 48,
        weeklyInspections: 128,
        weeklyInspectors: 24,
        weeklyIssues: 8,
        weeklyActiveInspectors: 16,
        weeklyInspectionsRate: 12.5,
        weeklyInspectorsRate: 4.2,
        weeklyIssuesRate: -2.1,
        weeklyActiveInspectorsRate: 8.3
      })
    }, 300)
  })
}

// 获取周趋势数据
export function getWeeklyTrend() {
  // Mock周趋势数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        dates: ['第1周', '第2周', '第3周', '第4周', '第5周', '第6周', '第7周'],
        inspections: [120, 132, 101, 134, 90, 230, 210],
        issues: [20, 18, 15, 22, 10, 25, 18]
      })
    }, 300)
  })
}

// 获取问题分布数据
export function getIssueDistribution() {
  // Mock问题分布数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        { value: 10, name: '设备异常' },
        { value: 8, name: '环境异常' },
        { value: 6, name: '安全隐患' },
        { value: 5, name: '其他' }
      ])
    }, 300)
  })
}

// 获取巡检人员排名
export function getInspectorRanking() {
  // Mock巡检人员排名
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        { name: '张三', count: 120 },
        { name: '李四', count: 110 },
        { name: '王五', count: 98 },
        { name: '赵六', count: 80 },
        { name: '钱七', count: 75 },
        { name: '孙八', count: 66 },
        { name: '周九', count: 58 },
        { name: '吴十', count: 50 }
      ])
    }, 300)
  })
}

// 导出巡检记录
export function exportRecords(params) {
  // Mock导出功能
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ success: true })
    }, 300)
  })
} 