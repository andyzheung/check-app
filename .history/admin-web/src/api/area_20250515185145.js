import request from '@/utils/request'

// 获取区域列表
export function getAreaList(params) {
  // Mock区域列表
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockAreas = [
        { id: 1, name: '机房A', description: '主机房', status: 'active' },
        { id: 2, name: '机房B', description: '备用机房', status: 'active' },
        { id: 3, name: '机房C', description: '测试机房', status: 'active' },
        { id: 4, name: '配电室', description: '主配电室', status: 'active' },
        { id: 5, name: '监控室', description: '安防监控室', status: 'active' }
      ]
      
      // 简单过滤
      let filteredAreas = [...mockAreas]
      if (params.name) {
        filteredAreas = filteredAreas.filter(area => area.name.includes(params.name))
      }
      if (params.status) {
        filteredAreas = filteredAreas.filter(area => area.status === params.status)
      }
      
      // 分页
      const pageSize = params.pageSize || 10
      const pageNum = params.pageNum || 1
      const total = filteredAreas.length
      const start = (pageNum - 1) * pageSize
      const end = start + pageSize
      const list = filteredAreas.slice(start, end)
      
      resolve({
        list,
        total
      })
    }, 300)
  })
}

// 获取区域详情
export function getAreaDetail(id) {
  // Mock区域详情
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockDetails = {
        1: { id: 1, name: '机房A', description: '主机房', status: 'active', checkItems: ['温度', '湿度', '噪音', '门禁', '监控', '消防设备'] },
        2: { id: 2, name: '机房B', description: '备用机房', status: 'active', checkItems: ['温度', '湿度', '噪音', '门禁', '监控', '消防设备'] },
        3: { id: 3, name: '机房C', description: '测试机房', status: 'active', checkItems: ['温度', '湿度', '噪音', '门禁', '监控', '消防设备'] },
        4: { id: 4, name: '配电室', description: '主配电室', status: 'active', checkItems: ['温度', '湿度', '电压', '电流', '监控', '消防设备'] },
        5: { id: 5, name: '监控室', description: '安防监控室', status: 'active', checkItems: ['温度', '湿度', '监控系统', '门禁系统', '报警系统'] }
      }
      
      resolve(mockDetails[id] || {})
    }, 300)
  })
}

// 添加区域
export function addArea(data) {
  return request({
    url: '/areas',
    method: 'post',
    data
  })
}

// 更新区域
export function updateArea(id, data) {
  return request({
    url: `/areas/${id}`,
    method: 'put',
    data
  })
}

// 删除区域
export function deleteArea(id) {
  return request({
    url: `/areas/${id}`,
    method: 'delete'
  })
}

// 获取区域二维码
export function getAreaQrCode(id) {
  return request({
    url: `/areas/${id}/qrcode`,
    method: 'get',
    responseType: 'blob'
  })
} 