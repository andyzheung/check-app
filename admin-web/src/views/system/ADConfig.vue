<template>
  <div class="ad-config-container">
    <a-card title="AD域配置" :bordered="false">
      <a-form
        :model="configForm"
        :rules="rules"
        ref="configFormRef"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="LDAP服务器地址" name="ldapUrl">
          <a-input v-model:value="configForm.ldapUrl" placeholder="ldap://example.com:389" />
        </a-form-item>
        
        <a-form-item label="域名" name="domain">
          <a-input v-model:value="configForm.domain" placeholder="example.com" />
        </a-form-item>
        
        <a-form-item label="管理员DN" name="bindDn">
          <a-input v-model:value="configForm.bindDn" placeholder="cn=admin,dc=example,dc=com" />
        </a-form-item>
        
        <a-form-item label="管理员密码" name="bindPassword">
          <a-input-password v-model:value="configForm.bindPassword" placeholder="密码" />
        </a-form-item>
        
        <a-form-item label="用户搜索基础" name="userSearchBase">
          <a-input v-model:value="configForm.userSearchBase" placeholder="ou=users,dc=example,dc=com" />
        </a-form-item>
        
        <a-form-item label="用户搜索过滤器" name="userSearchFilter">
          <a-input v-model:value="configForm.userSearchFilter" placeholder="(sAMAccountName={0})" />
        </a-form-item>
        
        <a-form-item label="用户对象类" name="userObjectClass">
          <a-input v-model:value="configForm.userObjectClass" placeholder="person" />
        </a-form-item>
        
        <a-form-item label="用户名属性" name="usernameAttribute">
          <a-input v-model:value="configForm.usernameAttribute" placeholder="sAMAccountName" />
        </a-form-item>
        
        <a-form-item label="姓名属性" name="nameAttribute">
          <a-input v-model:value="configForm.nameAttribute" placeholder="cn" />
        </a-form-item>
        
        <a-form-item label="邮箱属性" name="emailAttribute">
          <a-input v-model:value="configForm.emailAttribute" placeholder="mail" />
        </a-form-item>
        
        <a-form-item label="部门属性" name="departmentAttribute">
          <a-input v-model:value="configForm.departmentAttribute" placeholder="department" />
        </a-form-item>
        
        <a-form-item label="启用AD集成" name="enabled">
          <a-switch v-model:checked="configForm.enabled" />
        </a-form-item>
        
        <a-form-item label="同步间隔(分钟)" name="syncInterval">
          <a-input-number v-model:value="configForm.syncInterval" :min="5" :max="1440" />
        </a-form-item>
        
        <a-form-item :wrapper-col="{ offset: 6, span: 14 }">
          <a-space>
            <a-button type="primary" @click="handleSave" :loading="saveLoading">保存配置</a-button>
            <a-button @click="handleTest" :loading="testLoading">测试连接</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
    
    <a-card title="AD用户同步" :bordered="false" style="margin-top: 16px;">
      <div style="display: flex; justify-content: space-between; margin-bottom: 16px;">
        <span>用户同步操作</span>
        <a-space>
          <a-button @click="handleSyncIncremental" :loading="incrementalLoading">增量同步</a-button>
          <a-button type="primary" @click="handleSyncAll" :loading="fullSyncLoading">全量同步</a-button>
        </a-space>
      </div>
      
      <a-table
        :columns="columns"
        :data-source="syncLogs"
        :loading="tableLoading"
        :pagination="pagination"
        @change="handleTableChange"
        row-key="id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="getStatusColor(record.status)">
              {{ getStatusText(record.status) }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'duration'">
            {{ calculateDuration(record.startTime, record.endTime) }}
          </template>
        </template>
      </a-table>
    </a-card>
    
    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      :footer="null"
      @cancel="modalVisible = false"
    >
      <p>{{ modalMessage }}</p>
      <div style="text-align: right; margin-top: 16px;">
        <a-button type="primary" @click="modalVisible = false">确定</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, reactive, ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'

export default defineComponent({
  name: 'ADConfig',
  setup() {
    // 加载状态
    const saveLoading = ref(false)
    const testLoading = ref(false)
    const incrementalLoading = ref(false)
    const fullSyncLoading = ref(false)
    const tableLoading = ref(false)
    
    // 表单引用
    const configFormRef = ref(null)
    
    // AD配置表单
    const configForm = reactive({
      ldapUrl: '',
      domain: '',
      bindDn: '',
      bindPassword: '',
      userSearchBase: '',
      userSearchFilter: '',
      userObjectClass: '',
      usernameAttribute: '',
      nameAttribute: '',
      emailAttribute: '',
      departmentAttribute: '',
      enabled: false,
      syncInterval: 60
    })
    
    // 表单验证规则
    const rules = {
      ldapUrl: [{ required: true, message: '请输入LDAP服务器地址', trigger: 'blur' }],
      domain: [{ required: true, message: '请输入域名', trigger: 'blur' }],
      bindDn: [{ required: true, message: '请输入管理员DN', trigger: 'blur' }],
      bindPassword: [{ required: true, message: '请输入管理员密码', trigger: 'blur' }],
      userSearchBase: [{ required: true, message: '请输入用户搜索基础', trigger: 'blur' }],
      userSearchFilter: [{ required: true, message: '请输入用户搜索过滤器', trigger: 'blur' }],
      usernameAttribute: [{ required: true, message: '请输入用户名属性', trigger: 'blur' }],
      syncInterval: [{ required: true, message: '请输入同步间隔', trigger: 'blur' }]
    }
    
    // 同步日志表格相关
    const columns = [
      { title: '同步类型', dataIndex: 'syncType', key: 'syncType', 
        customRender: ({ text }) => {
          const types = {
            full: '全量同步',
            incremental: '增量同步',
            single: '单用户同步'
          }
          return types[text] || text
        }
      },
      { title: '开始时间', dataIndex: 'startTime', key: 'startTime' },
      { title: '结束时间', dataIndex: 'endTime', key: 'endTime' },
      { title: '总数量', dataIndex: 'totalCount', key: 'totalCount' },
      { title: '成功数量', dataIndex: 'successCount', key: 'successCount' },
      { title: '失败数量', dataIndex: 'failCount', key: 'failCount' },
      { title: '状态', dataIndex: 'status', key: 'status' },
      { title: '耗时', key: 'duration' }
    ]
    
    const syncLogs = ref([])
    const pagination = reactive({
      current: 1,
      pageSize: 10,
      total: 0,
      showSizeChanger: true,
      showTotal: (total) => `共 ${total} 条记录`
    })
    
    // 模态框
    const modalVisible = ref(false)
    const modalTitle = ref('')
    const modalMessage = ref('')
    
    // 获取AD配置
    const loadADConfig = async () => {
      try {
        // TODO: 实际API调用
        // const result = await getADConfig()
        const result = { code: 200, data: {
          ldapUrl: 'ldap://company.com:389',
          domain: 'company.com',
          bindDn: 'cn=admin,dc=company,dc=com',
          bindPassword: '******',
          userSearchBase: 'ou=users,dc=company,dc=com',
          userSearchFilter: '(sAMAccountName={0})',
          userObjectClass: 'person',
          usernameAttribute: 'sAMAccountName',
          nameAttribute: 'cn',
          emailAttribute: 'mail',
          departmentAttribute: 'department',
          enabled: true,
          syncInterval: 60
        }}
        
        if (result.code === 200) {
          Object.assign(configForm, result.data)
        }
      } catch (error) {
        console.error('加载AD配置失败:', error)
        message.error('加载AD配置失败')
      }
    }
    
    // 获取同步日志
    const loadSyncLogs = async () => {
      try {
        tableLoading.value = true
        // TODO: 实际API调用
        // const result = await getSyncLogs(pagination.current, pagination.pageSize)
        const result = { code: 200, data: {
          total: 2,
          list: [
            {
              id: 1,
              syncType: 'full',
              startTime: '2025-01-01 10:00:00',
              endTime: '2025-01-01 10:05:23',
              totalCount: 100,
              successCount: 98,
              failCount: 2,
              status: 'success',
              errorMessage: ''
            },
            {
              id: 2,
              syncType: 'incremental',
              startTime: '2025-01-02 15:00:00',
              endTime: '2025-01-02 15:01:10',
              totalCount: 5,
              successCount: 5,
              failCount: 0,
              status: 'success',
              errorMessage: ''
            }
          ]
        }}
        
        if (result.code === 200) {
          syncLogs.value = result.data.list
          pagination.total = result.data.total
        }
      } catch (error) {
        console.error('加载同步日志失败:', error)
        message.error('加载同步日志失败')
      } finally {
        tableLoading.value = false
      }
    }
    
    // 保存AD配置
    const handleSave = async () => {
      try {
        await configFormRef.value.validate()
        
        saveLoading.value = true
        // TODO: 实际API调用
        // const result = await updateADConfig(configForm)
        const result = { code: 200 }
        
        if (result.code === 200) {
          message.success('配置保存成功')
        } else {
          message.error(result.message || '配置保存失败')
        }
      } catch (error) {
        console.error('保存AD配置失败:', error)
      } finally {
        saveLoading.value = false
      }
    }
    
    // 测试AD连接
    const handleTest = async () => {
      try {
        await configFormRef.value.validate()
        
        testLoading.value = true
        // TODO: 实际API调用
        // const result = await testConnection(configForm)
        const result = { code: 200, data: { success: true, message: '连接成功' } }
        
        modalTitle.value = '连接测试结果'
        
        if (result.code === 200 && result.data.success) {
          modalMessage.value = '连接测试成功'
        } else {
          modalMessage.value = result.data.message || '连接测试失败'
        }
        
        modalVisible.value = true
      } catch (error) {
        console.error('测试AD连接失败:', error)
      } finally {
        testLoading.value = false
      }
    }
    
    // 增量同步
    const handleSyncIncremental = async () => {
      try {
        incrementalLoading.value = true
        // TODO: 实际API调用
        // const result = await syncIncrementalUsers()
        const result = { code: 200, data: { syncId: 3 } }
        
        if (result.code === 200) {
          message.success('增量同步任务已提交')
          // 刷新日志列表
          loadSyncLogs()
        } else {
          message.error(result.message || '增量同步失败')
        }
      } catch (error) {
        console.error('增量同步失败:', error)
        message.error('增量同步失败')
      } finally {
        incrementalLoading.value = false
      }
    }
    
    // 全量同步
    const handleSyncAll = async () => {
      try {
        fullSyncLoading.value = true
        // TODO: 实际API调用
        // const result = await syncAllUsers()
        const result = { code: 200, data: { syncId: 4 } }
        
        if (result.code === 200) {
          message.success('全量同步任务已提交')
          // 刷新日志列表
          loadSyncLogs()
        } else {
          message.error(result.message || '全量同步失败')
        }
      } catch (error) {
        console.error('全量同步失败:', error)
        message.error('全量同步失败')
      } finally {
        fullSyncLoading.value = false
      }
    }
    
    // 表格分页变化
    const handleTableChange = (pag) => {
      pagination.current = pag.current
      pagination.pageSize = pag.pageSize
      loadSyncLogs()
    }
    
    // 获取状态颜色
    const getStatusColor = (status) => {
      const colors = {
        processing: 'blue',
        success: 'green',
        fail: 'red'
      }
      return colors[status] || 'default'
    }
    
    // 获取状态文本
    const getStatusText = (status) => {
      const texts = {
        processing: '处理中',
        success: '成功',
        fail: '失败'
      }
      return texts[status] || status
    }
    
    // 计算耗时
    const calculateDuration = (startTime, endTime) => {
      if (!startTime || !endTime) return '-'
      
      const start = new Date(startTime)
      const end = new Date(endTime)
      const diff = (end - start) / 1000 // 秒
      
      if (diff < 60) {
        return `${diff.toFixed(0)}秒`
      } else if (diff < 3600) {
        const minutes = Math.floor(diff / 60)
        const seconds = Math.floor(diff % 60)
        return `${minutes}分${seconds}秒`
      } else {
        const hours = Math.floor(diff / 3600)
        const minutes = Math.floor((diff % 3600) / 60)
        const seconds = Math.floor(diff % 60)
        return `${hours}小时${minutes}分${seconds}秒`
      }
    }
    
    // 页面加载时初始化
    onMounted(() => {
      loadADConfig()
      loadSyncLogs()
    })
    
    return {
      configForm,
      configFormRef,
      rules,
      saveLoading,
      testLoading,
      incrementalLoading,
      fullSyncLoading,
      tableLoading,
      columns,
      syncLogs,
      pagination,
      modalVisible,
      modalTitle,
      modalMessage,
      handleSave,
      handleTest,
      handleSyncIncremental,
      handleSyncAll,
      handleTableChange,
      getStatusColor,
      getStatusText,
      calculateDuration
    }
  }
})
</script>

<style scoped>
.ad-config-container {
  padding: 16px;
}
</style> 