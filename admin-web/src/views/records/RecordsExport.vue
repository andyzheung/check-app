<template>
  <div>
    <h2>导出巡检记录</h2>
    
    <a-card title="导出条件" class="export-card">
      <a-form :model="exportForm" :label-col="{ span: 4 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="时间范围">
          <a-range-picker v-model:value="exportForm.dateRange" style="width: 100%" />
        </a-form-item>
        
        <a-form-item label="巡检区域">
          <a-select
            v-model:value="exportForm.areaIds"
            mode="multiple"
            placeholder="请选择巡检区域"
            style="width: 100%"
            :options="areaOptions"
          ></a-select>
        </a-form-item>
        
        <a-form-item label="巡检人员">
          <a-select
            v-model:value="exportForm.inspectorIds"
            mode="multiple"
            placeholder="请选择巡检人员"
            style="width: 100%"
            :options="inspectorOptions"
          ></a-select>
        </a-form-item>
        
        <a-form-item label="巡检状态">
          <a-select
            v-model:value="exportForm.status"
            placeholder="请选择巡检状态"
            style="width: 100%"
          >
            <a-select-option value="">全部状态</a-select-option>
            <a-select-option value="normal">正常</a-select-option>
            <a-select-option value="abnormal">异常</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="导出格式">
          <a-radio-group v-model:value="exportForm.format">
            <a-radio value="excel">Excel</a-radio>
            <a-radio value="pdf">PDF</a-radio>
            <a-radio value="csv">CSV</a-radio>
          </a-radio-group>
        </a-form-item>
        
        <a-form-item :wrapper-col="{ offset: 4, span: 16 }">
          <a-space>
            <a-button type="primary" @click="handleExport" :loading="exporting">
              导出
            </a-button>
            <a-button @click="resetForm">
              重置
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
    
    <a-card title="导出历史" class="export-history-card">
      <a-table
        :columns="columns"
        :data-source="historyList"
        :pagination="{ pageSize: 5 }"
        :loading="loading"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a @click="downloadFile(record)">下载</a>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { exportRecords, getExportHistory } from '@/api/record'
import { getAreaList } from '@/api/area'
import { getUserList } from '@/api/user'

export default defineComponent({
  name: 'RecordsExport',
  setup() {
    const exportForm = reactive({
      dateRange: [],
      areaIds: [],
      inspectorIds: [],
      status: '',
      format: 'excel'
    })
    
    const areaOptions = ref([])
    const inspectorOptions = ref([])
    const historyList = ref([])
    const loading = ref(false)
    const exporting = ref(false)
    
    const columns = [
      { title: '导出时间', dataIndex: 'createTime', key: 'createTime' },
      { title: '文件名称', dataIndex: 'fileName', key: 'fileName', ellipsis: true },
      { title: '文件大小', dataIndex: 'fileSize', key: 'fileSize' },
      { title: '导出人', dataIndex: 'creatorName', key: 'creatorName' },
      { title: '操作', key: 'action', width: 100 }
    ]
    
    // 获取区域列表
    const fetchAreaList = async () => {
      try {
        const res = await getAreaList({ pageSize: 100 })
        areaOptions.value = res.list.map(item => ({
          label: item.name,
          value: item.id
        }))
      } catch (error) {
        console.error('Failed to get area list:', error)
      }
    }
    
    // 获取人员列表
    const fetchInspectorList = async () => {
      try {
        const res = await getUserList({ pageSize: 100 })
        inspectorOptions.value = res.list.map(item => ({
          label: item.realName,
          value: item.id
        }))
      } catch (error) {
        console.error('Failed to get user list:', error)
      }
    }
    
    // 获取导出历史
    const fetchExportHistory = async () => {
      loading.value = true
      try {
        const res = await getExportHistory()
        historyList.value = res
      } catch (error) {
        console.error('Failed to get export history:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 导出记录
    const handleExport = async () => {
      if (!exportForm.dateRange || exportForm.dateRange.length !== 2) {
        message.warning('请选择时间范围')
        return
      }
      
      exporting.value = true
      try {
        const params = {
          startTime: exportForm.dateRange[0].format('YYYY-MM-DD HH:mm:ss'),
          endTime: exportForm.dateRange[1].format('YYYY-MM-DD HH:mm:ss'),
          areaIds: exportForm.areaIds.length > 0 ? exportForm.areaIds.join(',') : null,
          inspectorIds: exportForm.inspectorIds.length > 0 ? exportForm.inspectorIds.join(',') : null,
          status: exportForm.status || null,
          format: exportForm.format
        }
        
        const res = await exportRecords(params)
        if (res) {
          message.success('导出成功')
          // 如果后端返回文件流，前端直接下载
          if (res.type && res.type.includes('application/')) {
            const blob = new Blob([res], { type: res.type })
            const link = document.createElement('a')
            link.href = URL.createObjectURL(blob)
            link.download = `巡检记录_${new Date().getTime()}.${exportForm.format}`
            link.click()
            URL.revokeObjectURL(link.href)
          }
          fetchExportHistory()
        }
      } catch (error) {
        console.error('Failed to export records:', error)
        message.error('导出失败')
      } finally {
        exporting.value = false
      }
    }
    
    // 重置表单
    const resetForm = () => {
      exportForm.dateRange = []
      exportForm.areaIds = []
      exportForm.inspectorIds = []
      exportForm.status = ''
      exportForm.format = 'excel'
    }
    
    // 下载文件
    const downloadFile = (record) => {
      window.open(record.filePath, '_blank')
    }
    
    onMounted(() => {
      fetchAreaList()
      fetchInspectorList()
      fetchExportHistory()
    })
    
    return {
      exportForm,
      areaOptions,
      inspectorOptions,
      historyList,
      columns,
      loading,
      exporting,
      handleExport,
      resetForm,
      downloadFile
    }
  }
})
</script>

<style scoped>
.export-card {
  margin-bottom: 16px;
}
.export-history-card {
  margin-top: 16px;
}
</style> 