<template>
  <div class="config-container">
    <!-- 配置标签页 -->
    <div class="config-tabs">
      <div class="tab-header">
        <div class="tab-item active" @click="showTab('area')">区域配置</div>
        <div class="tab-item" @click="showTab('system')">系统参数</div>
        <div class="tab-item" @click="showTab('ad')">AD配置</div>
      </div>
      
      <!-- 区域配置标签页 -->
      <div class="tab-content" v-show="activeTab === 'area'">
        <div class="config-section">
          <div class="section-title">区域管理</div>
          <div class="search-toolbar">
            <div class="search-left">
              <a-select v-model:value="searchForm.areaType" placeholder="全部类型" style="width: 150px" allowClear>
                <a-select-option value="D">数据中心</a-select-option>
                <a-select-option value="E">弱电间</a-select-option>
              </a-select>
              <a-input v-model:value="searchForm.keyword" placeholder="搜索区域名称" style="width: 200px;" />
              <a-button type="primary" @click="fetchAreas">搜索</a-button>
            </div>
            <a-button type="primary" @click="handleAdd">+ 新增区域</a-button>
          </div>
          
          <div class="area-list">
            <div v-for="area in areas" :key="area.id" class="area-card">
              <div class="area-header">
                <div class="area-name">{{ area.areaName }}</div>
                <div class="area-type" :class="area.areaType === 'D' ? 'type-datacenter' : 'type-weakroom'">
                  {{ area.areaType === 'D' ? '数据中心' : '弱电间' }}
                </div>
              </div>
              <div class="area-info">编码: {{ area.areaCode }}</div>
              <div class="area-info">模块数量: {{ area.areaType === 'D' ? (area.moduleCount || 0) + '个' : '-' }}</div>
              <div class="area-info">状态: 启用</div>
              <div class="area-actions">
                <a-button size="small" v-if="area.areaType === 'D'" @click="handleEdit(area)">配置模块</a-button>
                <a-button size="small" @click="handleView(area)">编辑</a-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 系统参数标签页 -->
      <div class="tab-content" v-show="activeTab === 'system'">
        <div class="config-section">
          <div class="section-title">基础设置</div>
          <div class="config-form">
            <div class="form-row">
              <div class="form-label">系统名称:</div>
              <a-input v-model:value="systemConfig.systemName" style="flex: 1;" />
            </div>
            <div class="form-row">
              <div class="form-label">系统版本:</div>
              <a-input v-model:value="systemConfig.version" style="flex: 1;" readonly />
            </div>
            <div class="form-row">
              <div class="form-label">会话超时(分钟):</div>
              <a-input-number v-model:value="systemConfig.sessionTimeout" style="flex: 1;" />
            </div>
            <div class="form-row">
              <div class="form-label"></div>
              <a-button type="primary" @click="saveSystemConfig">保存设置</a-button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- AD配置标签页 -->
      <div class="tab-content" v-show="activeTab === 'ad'">
        <div class="config-section">
          <div class="section-title">Active Directory 配置</div>
          <div class="config-form">
            <div class="form-row">
              <div class="form-label">AD服务器地址:</div>
              <a-input v-model:value="adConfig.serverUrl" style="flex: 1;" />
            </div>
            <div class="form-row">
              <div class="form-label">域名:</div>
              <a-input v-model:value="adConfig.domain" style="flex: 1;" />
            </div>
            <div class="form-row">
              <div class="form-label">启用AD认证:</div>
              <a-switch v-model:checked="adConfig.enabled" />
            </div>
            <div class="form-row">
              <div class="form-label"></div>
              <div style="display: flex; gap: 12px;">
                <a-button @click="testAdConnection">测试连接</a-button>
                <a-button type="primary" @click="saveAdConfig">保存配置</a-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 模块配置弹窗 -->
    <a-modal
      v-model:open="configModalVisible"
      title="数据中心模块配置"
      width="800px"
      @ok="handleConfigSave"
      @cancel="handleConfigCancel">
      
      <a-form :model="configForm" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="区域名称">
          <a-input v-model:value="configForm.areaName" disabled />
        </a-form-item>
        
        <a-form-item label="模块数量">
          <a-input-number 
            v-model:value="configForm.moduleCount" 
            :min="1" 
            :max="20"
            @change="handleModuleCountChange" />
        </a-form-item>
        
        <a-form-item label="模块配置">
          <div class="module-config-list">
            <div v-for="(module, index) in configForm.modules" :key="index" class="module-item">
              <a-row :gutter="16">
                <a-col :span="8">
                  <a-input 
                    v-model:value="module.name" 
                    :placeholder="`模块${index + 1}名称`" />
                </a-col>
                <a-col :span="8">
                  <a-select 
                    v-model:value="module.type" 
                    placeholder="选择模块类型"
                    style="width: 100%">
                    <a-select-option value="compute">计算模块</a-select-option>
                    <a-select-option value="storage">存储模块</a-select-option>
                    <a-select-option value="network">网络模块</a-select-option>
                  </a-select>
                </a-col>
                <a-col :span="8">
                  <a-button type="text" danger @click="removeModule(index)" v-if="configForm.modules.length > 1">
                    删除
                  </a-button>
                </a-col>
              </a-row>
            </div>
          </div>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { getAreas, updateAreaConfig } from '@/api/area'

const loading = ref(false)
const areas = ref([])
const configModalVisible = ref(false)
const activeTab = ref('area')

const searchForm = reactive({
  areaType: undefined,
  keyword: ''
})

const configForm = reactive({
  id: null,
  areaName: '',
  moduleCount: 4,
  modules: []
})

const systemConfig = reactive({
  systemName: '巡检管理系统',
  version: 'v1.0.0',
  sessionTimeout: 30
})

const adConfig = reactive({
  serverUrl: 'ldap://192.168.1.100:389',
  domain: 'company.local',
  enabled: false
})

// 标签页切换
const showTab = (tabName) => {
  activeTab.value = tabName
}

const fetchAreas = async () => {
  loading.value = true
  try {
    const params = {
      page: 1,
      size: 100,
      type: searchForm.areaType
    }
    const response = await getAreas(params)
    areas.value = response.data.records || response.data.list || []
  } catch (error) {
    message.error('获取区域列表失败')
  } finally {
    loading.value = false
  }
}

// 系统配置保存
const saveSystemConfig = () => {
  message.success('系统配置保存成功')
}

// AD配置相关
const testAdConnection = () => {
  message.info('正在测试AD连接...')
  setTimeout(() => {
    message.success('AD连接测试成功')
  }, 2000)
}

const saveAdConfig = () => {
  message.success('AD配置保存成功')
}

const handleEdit = (record) => {
  configForm.id = record.id
  configForm.areaName = record.areaName
  configForm.moduleCount = record.moduleCount || 4
  
  // 解析现有配置或创建默认配置
  if (record.configJson) {
    try {
      const config = JSON.parse(record.configJson)
      configForm.modules = config.modules || []
    } catch (e) {
      configForm.modules = []
    }
  } else {
    configForm.modules = []
  }
  
  // 确保模块数量匹配
  handleModuleCountChange(configForm.moduleCount)
  configModalVisible.value = true
}

const handleModuleCountChange = (count) => {
  const currentLength = configForm.modules.length
  
  if (count > currentLength) {
    // 添加模块
    for (let i = currentLength; i < count; i++) {
      configForm.modules.push({
        id: i + 1,
        name: `模块${i + 1}`,
        type: 'compute'
      })
    }
  } else if (count < currentLength) {
    // 删除多余模块
    configForm.modules = configForm.modules.slice(0, count)
  }
}

const removeModule = (index) => {
  configForm.modules.splice(index, 1)
  configForm.moduleCount = configForm.modules.length
}

const handleConfigSave = async () => {
  try {
    const configJson = JSON.stringify({
      modules: configForm.modules
    })
    
    await updateAreaConfig(configForm.id, {
      moduleCount: configForm.moduleCount,
      configJson: configJson
    })
    
    message.success('模块配置保存成功')
    configModalVisible.value = false
    fetchAreas()
  } catch (error) {
    message.error('保存失败')
  }
}

const handleConfigCancel = () => {
  configModalVisible.value = false
}

const resetSearch = () => {
  searchForm.areaType = undefined
  searchForm.keyword = ''
  fetchAreas()
}

const handleAdd = () => {
  // TODO: 实现新增区域功能
  message.info('新增功能待实现')
}

const handleView = (record) => {
  // TODO: 实现查看详情功能
  message.info('查看功能待实现')
}

const handleDelete = (record) => {
  // TODO: 实现删除功能
  message.info('删除功能待实现')
}

onMounted(() => {
  fetchAreas()
})
</script>

<style scoped>
.config-container {
  background: #f0f2f5;
}

.config-tabs {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(31, 38, 135, 0.08);
  margin-bottom: 24px;
}

.tab-header {
  display: flex;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 16px;
}

.tab-item {
  padding: 16px 24px;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  color: #666;
  font-weight: 500;
  transition: all 0.2s;
}

.tab-item:hover, .tab-item.active {
  color: #1890ff;
  border-bottom-color: #1890ff;
}

.tab-content {
  padding: 24px;
  min-height: 400px;
}

.config-section {
  margin-bottom: 32px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-title::before {
  content: '';
  width: 4px;
  height: 18px;
  background: #1890ff;
  border-radius: 2px;
}

.search-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.search-left {
  display: flex;
  gap: 12px;
  align-items: center;
}

.area-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
  margin-top: 16px;
}

.area-card {
  background: white;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  padding: 16px;
  transition: all 0.2s;
}

.area-card:hover {
  border-color: #1890ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.1);
}

.area-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.area-name {
  font-weight: bold;
  font-size: 16px;
  color: #333;
}

.area-type {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.type-datacenter {
  background: #e6f7ff;
  color: #1890ff;
}

.type-weakroom {
  background: #f6ffed;
  color: #52c41a;
}

.area-info {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.area-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.config-form {
  background: #fafafa;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #e8e8e8;
}

.form-row {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  align-items: center;
}

.form-label {
  min-width: 120px;
  font-weight: 500;
  color: #333;
}

.module-config-list {
  max-height: 400px;
  overflow-y: auto;
}

.module-item {
  margin-bottom: 12px;
  padding: 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  background: #fafafa;
}
</style> 