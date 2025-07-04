<template>
  <div>
    <!-- 筛选条件 -->
    <div class="card">
      <div class="card-title">筛选条件</div>
      <a-form layout="inline" :model="queryParams">
        <a-form-item label="时间范围">
          <a-range-picker
            v-model:value="dateRange"
            format="YYYY-MM-DD"
            :placeholder="['开始日期', '结束日期']"
            @change="onDateRangeChange"
          />
        </a-form-item>
        <a-form-item label="巡检区域">
          <a-select
            v-model:value="queryParams.areaId"
            placeholder="全部区域"
            style="width: 160px"
            allowClear
          >
            <a-select-option v-for="area in areaOptions" :key="area.id" :value="area.id">
              {{ area.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="巡检人员">
          <a-select
            v-model:value="queryParams.inspectorId"
            placeholder="全部人员"
            style="width: 160px"
            allowClear
          >
            <a-select-option v-for="user in userOptions" :key="user.id" :value="user.id">
              {{ user.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="巡检状态">
          <a-select
            v-model:value="queryParams.status"
            placeholder="全部状态"
            style="width: 160px"
            allowClear
          >
            <a-select-option value="normal">正常</a-select-option>
            <a-select-option value="abnormal">异常</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery">查询</a-button>
          <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
        </a-form-item>
      </a-form>
    </div>

    <!-- 记录列表 -->
    <div class="card">
      <div style="display: flex; justify-content: space-between; margin-bottom: 16px;">
        <div class="card-title">巡检记录</div>
        <div>
          <a-button type="primary" @click="handleExport" style="margin-right: 8px;">
            <template #icon><download-outlined /></template>
            导出
          </a-button>
        </div>
      </div>
      <a-table
        :columns="columns"
        :data-source="recordList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
        row-key="id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 'normal' ? 'green' : 'red'">
              {{ record.status === 'normal' ? '正常' : '异常' }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a @click="showDetail(record)">查看详情</a>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 详情弹窗 -->
    <a-modal
      v-model:visible="detailVisible"
      title="巡检详情"
      width="700px"
      :footer="null"
    >
      <a-descriptions bordered :column="2">
        <a-descriptions-item label="巡检ID" :span="2">{{ recordDetail.id }}</a-descriptions-item>
        <a-descriptions-item label="巡检区域">{{ recordDetail.areaName }}</a-descriptions-item>
        <a-descriptions-item label="巡检人员">{{ recordDetail.inspectorName }}</a-descriptions-item>
        <a-descriptions-item label="巡检时间">{{ recordDetail.inspectionTime }}</a-descriptions-item>
        <a-descriptions-item label="巡检状态">
          <a-tag :color="recordDetail.status === 'normal' ? 'green' : 'red'">
            {{ recordDetail.status === 'normal' ? '正常' : '异常' }}
          </a-tag>
        </a-descriptions-item>
      </a-descriptions>

      <a-divider orientation="left">环境巡检项</a-divider>
      <a-table
        :columns="environmentColumns"
        :data-source="recordDetail.environmentItems || []"
        :pagination="false"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 'normal' ? 'green' : 'red'">
              {{ record.status === 'normal' ? '正常' : '异常' }}
            </a-tag>
          </template>
        </template>
      </a-table>

      <a-divider orientation="left">安全巡检项</a-divider>
      <a-table
        :columns="securityColumns"
        :data-source="recordDetail.securityItems || []"
        :pagination="false"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 'normal' ? 'green' : 'red'">
              {{ record.status === 'normal' ? '正常' : '异常' }}
            </a-tag>
          </template>
        </template>
      </a-table>

      <a-divider orientation="left">备注信息</a-divider>
      <div style="padding: 8px;">{{ recordDetail.remark || '无' }}</div>

      <div style="margin-top: 24px; text-align: right;">
        <a-button @click="detailVisible = false">关闭</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { DownloadOutlined } from '@ant-design/icons-vue'
import { getRecordList, getRecordDetail, exportRecords } from '@/api/record'
import { getAreaList } from '@/api/area'
import { getUserList } from '@/api/user'
import moment from 'moment'

export default defineComponent({
  name: 'Records',
  components: {
    DownloadOutlined
  },
  setup() {
    // 表格列定义
    const columns = [
      { title: '巡检ID', dataIndex: 'id', key: 'id', width: 80 },
      { title: '巡检区域', dataIndex: 'areaName', key: 'areaName' },
      { title: '巡检人员', dataIndex: 'inspectorName', key: 'inspectorName' },
      { title: '巡检时间', dataIndex: 'inspectionTime', key: 'inspectionTime', sorter: true },
      { title: '状态', dataIndex: 'status', key: 'status' },
      { title: '操作', key: 'action', width: 120 }
    ]

    // 环境巡检项表格列
    const environmentColumns = [
      { title: '巡检项', dataIndex: 'name', key: 'name' },
      { title: '状态', dataIndex: 'status', key: 'status', width: 100 },
      { title: '备注', dataIndex: 'remark', key: 'remark' }
    ]

    // 安全巡检项表格列
    const securityColumns = [
      { title: '巡检项', dataIndex: 'name', key: 'name' },
      { title: '状态', dataIndex: 'status', key: 'status', width: 100 },
      { title: '备注', dataIndex: 'remark', key: 'remark' }
    ]

    // 查询参数
    const queryParams = reactive({
      startTime: '',
      endTime: '',
      areaId: undefined,
      inspectorId: undefined,
      status: undefined,
      pageNum: 1,
      pageSize: 10
    })

    // 日期范围
    const dateRange = ref([])

    // 日期范围变化处理
    const onDateRangeChange = (dates, dateStrings) => {
      if (dates) {
        queryParams.startTime = dateStrings[0]
        queryParams.endTime = dateStrings[1]
      } else {
        queryParams.startTime = ''
        queryParams.endTime = ''
      }
    }

    // 分页配置
    const pagination = reactive({
      current: 1,
      pageSize: 10,
      total: 0,
      showSizeChanger: true,
      showTotal: (total) => `共 ${total} 条`
    })

    // 记录列表数据
    const loading = ref(false)
    const recordList = ref([])

    // 区域选项
    const areaOptions = ref([])

    // 用户选项
    const userOptions = ref([])

    // 详情弹窗
    const detailVisible = ref(false)
    const recordDetail = reactive({
      id: '',
      areaName: '',
      inspectorName: '',
      inspectionTime: '',
      status: 'normal',
      environmentItems: [],
      securityItems: [],
      remark: ''
    })

    // 获取记录列表
    const getList = async () => {
      loading.value = true
      try {
        const { list, total } = await getRecordList({
          ...queryParams
        })
        recordList.value = list
        pagination.total = total
        pagination.current = queryParams.pageNum
        pagination.pageSize = queryParams.pageSize
      } catch (error) {
        console.error('Failed to get record list:', error)
      } finally {
        loading.value = false
      }
    }

    // 获取区域选项
    const getAreas = async () => {
      try {
        const data = await getAreaList({ pageSize: 100 })
        areaOptions.value = data.list || []
      } catch (error) {
        console.error('Failed to get area options:', error)
      }
    }

    // 获取用户选项
    const getUsers = async () => {
      try {
        const data = await getUserList({ pageSize: 100 })
        userOptions.value = data.list || []
      } catch (error) {
        console.error('Failed to get user options:', error)
      }
    }

    // 查询按钮点击
    const handleQuery = () => {
      queryParams.pageNum = 1
      getList()
    }

    // 重置查询
    const resetQuery = () => {
      dateRange.value = []
      Object.assign(queryParams, {
        startTime: '',
        endTime: '',
        areaId: undefined,
        inspectorId: undefined,
        status: undefined,
        pageNum: 1,
        pageSize: 10
      })
      getList()
    }

    // 表格变化事件
    const handleTableChange = (pag, filters, sorter) => {
      queryParams.pageNum = pag.current
      queryParams.pageSize = pag.pageSize
      
      // 处理排序
      if (sorter && sorter.field) {
        queryParams.sortField = sorter.field
        queryParams.sortOrder = sorter.order === 'ascend' ? 'asc' : 'desc'
      } else {
        queryParams.sortField = undefined
        queryParams.sortOrder = undefined
      }
      
      getList()
    }

    // 显示详情
    const showDetail = async (record) => {
      try {
        const data = await getRecordDetail(record.id)
        Object.assign(recordDetail, data)
        detailVisible.value = true
      } catch (error) {
        console.error('Failed to get record detail:', error)
      }
    }

    // 导出记录
    const handleExport = async () => {
      try {
        message.loading('正在导出数据...', 0)
        const blob = await exportRecords({
          ...queryParams,
          pageNum: undefined,
          pageSize: undefined
        })
        
        // 创建下载链接
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `巡检记录_${moment().format('YYYY-MM-DD')}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        
        message.destroy()
        message.success('导出成功')
      } catch (error) {
        message.destroy()
        message.error('导出失败')
        console.error('Failed to export records:', error)
      }
    }

    onMounted(() => {
      getList()
      getAreas()
      getUsers()
    })

    return {
      columns,
      environmentColumns,
      securityColumns,
      queryParams,
      dateRange,
      pagination,
      loading,
      recordList,
      areaOptions,
      userOptions,
      detailVisible,
      recordDetail,
      onDateRangeChange,
      handleQuery,
      resetQuery,
      handleTableChange,
      showDetail,
      handleExport
    }
  }
})
</script> 