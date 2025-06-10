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
        <a-form-item label="问题类型">
          <a-select
            v-model:value="queryParams.type"
            placeholder="全部类型"
            style="width: 160px"
            allowClear
          >
            <a-select-option value="">全部类型</a-select-option>
            <a-select-option value="device">设备异常</a-select-option>
            <a-select-option value="environment">环境异常</a-select-option>
            <a-select-option value="security">安全隐患</a-select-option>
            <a-select-option value="other">其他</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="记录人员">
          <a-select
            v-model:value="queryParams.recorderId"
            placeholder="全部人员"
            style="width: 160px"
            allowClear
          >
            <a-select-option v-for="user in userOptions" :key="user.id" :value="user.id">
              {{ user.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="问题状态">
          <a-select
            v-model:value="queryParams.status"
            placeholder="全部状态"
            style="width: 160px"
            allowClear
          >
            <a-select-option value="">全部状态</a-select-option>
            <a-select-option value="open">未闭环</a-select-option>
            <a-select-option value="closed">已闭环</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery">查询</a-button>
          <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
        </a-form-item>
      </a-form>
    </div>

    <!-- 问题列表 -->
    <div class="card">
      <div style="display: flex; justify-content: space-between; margin-bottom: 16px;">
        <div class="card-title">问题列表</div>
        <div>
          <a-button @click="handleExport">
            <template #icon><download-outlined /></template>
            导出
          </a-button>
        </div>
      </div>
      <a-table
        :columns="columns"
        :data-source="issueList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
        row-key="id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 'open' ? 'red' : 'green'">
              {{ record.status === 'open' ? '未闭环' : '已闭环' }}
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
      title="问题详情"
      width="700px"
      :footer="null"
    >
      <a-descriptions bordered :column="2">
        <a-descriptions-item label="问题编号" :span="2">{{ issueDetail.id }}</a-descriptions-item>
        <a-descriptions-item label="问题描述" :span="2">{{ issueDetail.description }}</a-descriptions-item>
        <a-descriptions-item label="问题类型">{{ getTypeText(issueDetail.type) }}</a-descriptions-item>
        <a-descriptions-item label="发生时间">{{ issueDetail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="记录人员">{{ issueDetail.recorderName }}</a-descriptions-item>
        <a-descriptions-item label="问题状态">
          <a-tag :color="issueDetail.status === 'open' ? 'red' : 'green'">
            {{ issueDetail.status === 'open' ? '未闭环' : '已闭环' }}
          </a-tag>
        </a-descriptions-item>
      </a-descriptions>

      <a-divider orientation="left">问题详情</a-divider>
      <div style="padding: 8px;">{{ issueDetail.detail || '无' }}</div>

      <a-divider orientation="left">处理记录</a-divider>
      <a-timeline v-if="issueDetail.processRecords && issueDetail.processRecords.length > 0">
        <a-timeline-item v-for="(record, index) in issueDetail.processRecords" :key="index" :color="getTimelineColor(record.action)">
          <p><strong>{{ record.action }}</strong> - {{ record.time }}</p>
          <p>处理人: {{ record.processor }}</p>
          <p>{{ record.content }}</p>
        </a-timeline-item>
      </a-timeline>
      <div v-else style="padding: 8px;">暂无处理记录</div>

      <a-divider orientation="left" v-if="issueDetail.status === 'open'">问题处理</a-divider>
      <div v-if="issueDetail.status === 'open'" style="padding: 8px;">
        <a-form :model="processForm" layout="vertical">
          <a-form-item label="处理意见" name="content">
            <a-textarea v-model:value="processForm.content" rows="4" placeholder="请输入处理意见" />
          </a-form-item>
          <a-form-item label="处理结果" name="action">
            <a-radio-group v-model:value="processForm.action">
              <a-radio value="process">处理中</a-radio>
              <a-radio value="close">关闭问题</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-form>
      </div>

      <div style="margin-top: 24px; text-align: right;">
        <a-button @click="detailVisible = false" style="margin-right: 8px;">取消</a-button>
        <a-button type="primary" @click="handleProcess" v-if="issueDetail.status === 'open'">提交</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { DownloadOutlined } from '@ant-design/icons-vue'
import { getIssueList, getIssueDetail, processIssue, exportIssues } from '@/api/issue'
import { getUserList } from '@/api/user'
import moment from 'moment'

export default defineComponent({
  name: 'Issues',
  components: {
    DownloadOutlined
  },
  setup() {
    // 表格列定义
    const columns = [
      { title: '问题编号', dataIndex: 'id', key: 'id', width: 140 },
      { title: '问题描述', dataIndex: 'description', key: 'description', ellipsis: true },
      { title: '问题类型', dataIndex: 'type', key: 'type', 
        customRender: ({ text }) => getTypeText(text) },
      { title: '发生时间', dataIndex: 'createTime', key: 'createTime', sorter: true },
      { title: '记录人员', dataIndex: 'recorderName', key: 'recorderName' },
      { title: '问题状态', dataIndex: 'status', key: 'status' },
      { title: '操作', key: 'action', width: 100 }
    ]

    // 查询参数
    const queryParams = reactive({
      startTime: '',
      endTime: '',
      type: '',
      recorderId: '',
      status: '',
      pageNum: 1,
      pageSize: 10
    })

    // 日期范围
    const dateRange = ref([])

    // 分页配置
    const pagination = reactive({
      current: 1,
      pageSize: 10,
      total: 0,
      showSizeChanger: true,
      showTotal: (total) => `共 ${total} 条`
    })

    // 问题列表数据
    const loading = ref(false)
    const issueList = ref([])
    const userOptions = ref([])

    // 详情相关
    const detailVisible = ref(false)
    const issueDetail = ref({})
    
    // 处理表单
    const processForm = reactive({
      content: '',
      action: 'process'
    })

    // 获取问题列表
    const getList = async () => {
      loading.value = true
      try {
        // 确保日期格式正确
        const params = {
          ...queryParams,
          startDate: queryParams.startTime,
          endDate: queryParams.endTime,
          page: queryParams.pageNum,
          size: queryParams.pageSize
        }
        
        // 移除后端不需要的参数
        delete params.startTime
        delete params.endTime
        delete params.pageNum
        delete params.pageSize
        
        const { list, total } = await getIssueList(params)
        issueList.value = list || []
        pagination.total = total || 0
        pagination.current = queryParams.pageNum
        pagination.pageSize = queryParams.pageSize
        console.log('获取问题列表结果:', list, total)
      } catch (error) {
        console.error('Failed to get issue list:', error)
      } finally {
        loading.value = false
      }
    }

    // 日期范围变化
    const onDateRangeChange = (dates, dateStrings) => {
      queryParams.startTime = dateStrings[0]
      queryParams.endTime = dateStrings[1]
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
        type: '',
        recorderId: '',
        status: '',
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
        queryParams.sortOrder = sorter.order
      } else {
        queryParams.sortField = undefined
        queryParams.sortOrder = undefined
      }
      
      getList()
    }

    // 获取类型文本
    const getTypeText = (type) => {
      switch (type) {
        case 'device': return '设备异常'
        case 'environment': return '环境异常'
        case 'security': return '安全隐患'
        case 'other': return '其他'
        default: return type
      }
    }

    // 获取时间线颜色
    const getTimelineColor = (action) => {
      switch (action) {
        case 'create': return 'blue'
        case 'process': return 'orange'
        case 'close': return 'green'
        default: return 'gray'
      }
    }

    // 查看详情
    const showDetail = async (record) => {
      try {
        const data = await getIssueDetail(record.id)
        issueDetail.value = data
        detailVisible.value = true
        
        // 重置处理表单
        processForm.content = ''
        processForm.action = 'process'
      } catch (error) {
        console.error('Failed to get issue detail:', error)
      }
    }

    // 处理问题
    const handleProcess = async () => {
      if (!processForm.content) {
        message.error('请输入处理意见')
        return
      }

      try {
        await processIssue(issueDetail.value.id, {
          content: processForm.content,
          action: processForm.action
        })
        
        message.success('问题处理成功')
        detailVisible.value = false
        getList() // 刷新列表
      } catch (error) {
        console.error('Failed to process issue:', error)
      }
    }

    // 导出
    const handleExport = async () => {
      try {
        await exportIssues(queryParams)
        message.success('导出成功')
      } catch (error) {
        console.error('Failed to export issues:', error)
      }
    }

    // 获取用户列表（用于筛选）
    const getUserOptions = async () => {
      try {
        const { list } = await getUserList({ pageSize: 100 })
        userOptions.value = list.map(item => ({
          id: item.id,
          name: item.realname
        }))
      } catch (error) {
        console.error('Failed to get user options:', error)
      }
    }

    onMounted(() => {
      getList()
      getUserOptions()
    })

    return {
      columns,
      queryParams,
      dateRange,
      pagination,
      loading,
      issueList,
      userOptions,
      detailVisible,
      issueDetail,
      processForm,
      onDateRangeChange,
      handleQuery,
      resetQuery,
      handleTableChange,
      getTypeText,
      getTimelineColor,
      showDetail,
      handleProcess,
      handleExport
    }
  }
})
</script>

<style scoped>
.issue-detail-content {
  margin-bottom: 16px;
}
</style> 