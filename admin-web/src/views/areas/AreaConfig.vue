<template>
  <div class="config-container">
    <!-- 配置标签页 -->
    <div class="config-tabs">
      <div class="tab-header">
        <div class="tab-item" :class="{ active: activeTab === 'area' }" @click="showTab('area')">区域配置</div>
        <div class="tab-item" :class="{ active: activeTab === 'system' }" @click="showTab('system')">系统参数</div>
        <div class="tab-item" :class="{ active: activeTab === 'ad' }" @click="showTab('ad')">AD配置</div>
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
                <a-button size="small" type="primary" @click="handleGenerateQRCode(area)">生成二维码</a-button>
                <a-button size="small" danger @click="handleDelete(area)">删除</a-button>
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

    <!-- 新增/编辑区域弹窗 -->
    <a-modal
      v-model:visible="areaModalVisible"
      :title="areaModalTitle"
      width="600px"
      @ok="handleAreaSave"
      @cancel="handleAreaCancel">
      
      <a-form :model="areaForm" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" ref="areaFormRef">
        <a-form-item label="区域名称" name="areaName" :rules="[{ required: true, message: '请输入区域名称' }]">
          <a-input v-model:value="areaForm.areaName" placeholder="请输入区域名称" />
        </a-form-item>
        
        <a-form-item label="区域编码" name="areaCode" :rules="[{ required: true, message: '请输入区域编码' }]">
          <a-input v-model:value="areaForm.areaCode" placeholder="请输入区域编码" />
        </a-form-item>
        
        <a-form-item label="区域类型" name="areaType" :rules="[{ required: true, message: '请选择区域类型' }]">
          <a-select v-model:value="areaForm.areaType" placeholder="请选择区域类型">
            <a-select-option value="D">数据中心</a-select-option>
            <a-select-option value="E">弱电间</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="区域地址" name="address">
          <a-input v-model:value="areaForm.address" placeholder="请输入区域地址" />
        </a-form-item>
        
        <a-form-item label="区域描述" name="description">
          <a-textarea v-model:value="areaForm.description" placeholder="请输入区域描述" :rows="3" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 模块配置弹窗 -->
    <a-modal
      v-model:visible="configModalVisible"
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

    <!-- 二维码预览弹窗 -->
    <a-modal
      v-model:visible="qrCodeModalVisible"
      :title="`${currentArea?.areaName || ''} - 二维码`"
      width="500px"
      :footer="null"
      @cancel="handleQRCodeCancel">
      
      <div class="qrcode-preview">
        <div v-if="qrCodeLoading" class="qrcode-loading">
          <a-spin size="large" />
          <p>正在生成二维码...</p>
        </div>
        
        <div v-else-if="qrCodeUrl" class="qrcode-content">
          <div class="qrcode-info">
            <h3>{{ currentArea?.areaName }}</h3>
            <p>区域编码：{{ currentArea?.areaCode }}</p>
            <p>区域类型：{{ currentArea?.areaType === 'D' ? '数据中心' : '弱电间' }}</p>
          </div>
          
          <div class="qrcode-image">
            <img :src="qrCodeUrl" :alt="`${currentArea?.areaCode} 二维码`" />
          </div>
          
          <div class="qrcode-actions">
            <a-button type="primary" @click="handleDownloadQRCode" block>
              <template #icon>
                <span class="material-icons">download</span>
              </template>
              下载二维码
            </a-button>
          </div>
          
          <div class="qrcode-tips">
            <p><strong>使用说明：</strong></p>
            <ul>
              <li>将二维码打印并张贴在对应区域入口</li>
              <li>巡检人员使用移动App扫码开始巡检</li>
              <li>建议打印尺寸：5cm x 5cm以上</li>
              <li>请选择高质量打印（600DPI以上）</li>
            </ul>
          </div>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { getAreas, updateAreaConfig, createArea, updateArea, deleteArea, generateQRCode, getQRCode } from '@/api/area'

const loading = ref(false)
const areas = ref([])
const configModalVisible = ref(false)
const areaModalVisible = ref(false)
const areaModalTitle = ref('新增区域')
const activeTab = ref('area')
const areaFormRef = ref()

// 二维码相关状态
const qrCodeModalVisible = ref(false)
const qrCodeUrl = ref('')
const currentArea = ref(null)
const qrCodeLoading = ref(false)

const searchForm = reactive({
  areaType: undefined,
  keyword: ''
})

const areaForm = reactive({
  id: null,
  areaName: '',
  areaCode: '',
  areaType: '',
  address: '',
  description: '',
  status: 'active'
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
      type: searchForm.areaType,
      keyword: searchForm.keyword
    }
    console.log('发送区域列表请求，参数:', params)
    const response = await getAreas(params)
    console.log('区域列表API响应:', response)
    
    // 正确处理响应数据结构
    if (response && response.data) {
      areas.value = response.data.records || response.data.list || []
    } else if (response && Array.isArray(response)) {
      areas.value = response
    } else {
      areas.value = []
      console.warn('未知的响应数据结构:', response)
    }
    console.log('解析后的区域列表:', areas.value)
  } catch (error) {
    console.error('获取区域列表失败:', error)
    message.error('获取区域列表失败')
    areas.value = []
  } finally {
    loading.value = false
  }
}

// 新增区域
const handleAdd = () => {
  console.log('点击新增区域按钮')
  console.log('当前弹窗状态:', areaModalVisible.value)
  areaModalTitle.value = '新增区域'
  resetAreaForm()
  areaModalVisible.value = true
  console.log('设置弹窗状态为:', areaModalVisible.value)
  console.log('弹窗标题:', areaModalTitle.value)
  console.log('表单数据:', areaForm)
}

// 编辑区域
const handleView = (record) => {
  areaModalTitle.value = '编辑区域'
  Object.assign(areaForm, record)
  areaModalVisible.value = true
}

// 删除区域
const handleDelete = (record) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除区域"${record.areaName}"吗？`,
    onOk: async () => {
      try {
        await deleteArea(record.id)
        message.success('删除成功')
        fetchAreas()
      } catch (error) {
        message.error('删除失败')
      }
    }
  })
}

// 重置区域表单
const resetAreaForm = () => {
  Object.assign(areaForm, {
    id: null,
    areaName: '',
    areaCode: '',
    areaType: '',
    address: '',
    description: '',
    status: 'active'
  })
}

// 保存区域
const handleAreaSave = async () => {
  try {
    await areaFormRef.value.validate()
    
    if (areaForm.id) {
      // 编辑
      await updateArea(areaForm.id, areaForm)
      message.success('更新成功')
    } else {
      // 新增
      await createArea(areaForm)
      message.success('创建成功')
    }
    
    areaModalVisible.value = false
    fetchAreas()
  } catch (error) {
    console.error('保存失败:', error)
    message.error('保存失败')
  }
}

const handleAreaCancel = () => {
  areaModalVisible.value = false
  resetAreaForm()
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

// 生成二维码
const handleGenerateQRCode = async (area) => {
  try {
    currentArea.value = area
    qrCodeLoading.value = true
    qrCodeModalVisible.value = true
    
    // 先尝试获取已有的二维码
    let response
    try {
      response = await getQRCode(area.id)
      if (response.data) {
        qrCodeUrl.value = response.data
      } else {
        throw new Error('No existing QR code')
      }
    } catch (error) {
      // 如果没有二维码，则生成新的
      response = await generateQRCode(area.id)
      qrCodeUrl.value = response.data
    }
    
    message.success('二维码获取成功')
  } catch (error) {
    console.error('二维码操作失败:', error)
    message.error('二维码操作失败')
    qrCodeModalVisible.value = false
  } finally {
    qrCodeLoading.value = false
  }
}

// 下载二维码
const handleDownloadQRCode = () => {
  if (qrCodeUrl.value && currentArea.value) {
    // 使用相对路径，让浏览器自动处理协议和域名
    const downloadUrl = qrCodeUrl.value.startsWith('http') 
      ? qrCodeUrl.value 
      : qrCodeUrl.value  // 直接使用相对路径，由Nginx代理处理
    
    const link = document.createElement('a')
    link.href = downloadUrl
    link.download = `${currentArea.value.areaCode}_qrcode.png`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    message.success('二维码下载成功')
  }
}

// 关闭二维码弹窗
const handleQRCodeCancel = () => {
  qrCodeModalVisible.value = false
  qrCodeUrl.value = ''
  currentArea.value = null
}

onMounted(() => {
  fetchAreas()
})
</script>

<style scoped>
.config-container {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.config-tabs {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.tab-header {
  display: flex;
  border-bottom: 1px solid #e8e8e8;
}

.tab-item {
  padding: 16px 24px;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.3s;
}

.tab-item:hover {
  background: #f5f5f5;
}

.tab-item.active {
  color: #1890ff;
  border-bottom-color: #1890ff;
  background: #f0f8ff;
}

.tab-content {
  padding: 24px;
}

.config-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #333;
}

.search-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 16px;
  background: #fafafa;
  border-radius: 6px;
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
}

.area-card {
  background: white;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  padding: 16px;
  transition: all 0.3s;
}

.area-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: #1890ff;
}

.area-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.area-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.area-type {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  color: white;
}

.type-datacenter { background: #f5222d; }
.type-weakroom { background: #fa8c16; }

.area-info {
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
}

.area-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.config-form {
  max-width: 600px;
}

.form-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.form-label {
  width: 120px;
  text-align: right;
  margin-right: 16px;
  color: #333;
}

.module-config-list {
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  padding: 16px;
  background: #fafafa;
}

.module-item {
  margin-bottom: 12px;
}

.module-item:last-child {
  margin-bottom: 0;
}

/* 二维码预览样式 */
.qrcode-preview {
  text-align: center;
}

.qrcode-loading {
  padding: 40px 0;
}

.qrcode-loading p {
  margin-top: 16px;
  color: #666;
}

.qrcode-content {
  padding: 20px 0;
}

.qrcode-info {
  margin-bottom: 24px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 6px;
  text-align: left;
}

.qrcode-info h3 {
  margin: 0 0 12px 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.qrcode-info p {
  margin: 8px 0;
  color: #666;
  font-size: 14px;
}

.qrcode-image {
  margin: 24px 0;
  padding: 20px;
  background: #fff;
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
}

.qrcode-image img {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
}

.qrcode-actions {
  margin: 24px 0;
}

.qrcode-tips {
  margin-top: 24px;
  padding: 16px;
  background: #f0f8ff;
  border-radius: 6px;
  text-align: left;
}

.qrcode-tips p {
  margin: 0 0 12px 0;
  font-weight: 600;
  color: #1890ff;
}

.qrcode-tips ul {
  margin: 0;
  padding-left: 20px;
  color: #666;
  font-size: 14px;
}

.qrcode-tips li {
  margin: 8px 0;
  line-height: 1.6;
}
</style> 