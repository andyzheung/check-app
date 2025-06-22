<template>
  <div class="task-schedule">
    <a-card title="巡检任务排班" class="schedule-card">
      <template #extra>
        <a-space>
          <a-button type="primary" @click="showCreateTask">
            <template #icon><PlusOutlined /></template>
            新建任务
          </a-button>
          <a-button @click="refreshSchedule">
            <template #icon><ReloadOutlined /></template>
            刷新
          </a-button>
        </a-space>
      </template>

      <div class="schedule-toolbar">
        <a-space>
          <a-select v-model:value="filterInspector" placeholder="筛选巡检员" style="width: 150px" allowClear @change="loadScheduleData">
            <a-select-option v-for="inspector in inspectors" :key="inspector.id" :value="inspector.id">
              {{ inspector.realName }}
            </a-select-option>
          </a-select>
          <a-select v-model:value="filterArea" placeholder="筛选区域" style="width: 150px" allowClear @change="loadScheduleData">
            <a-select-option v-for="area in areas" :key="area.id" :value="area.id">
              {{ area.areaName }}
            </a-select-option>
          </a-select>
          <a-range-picker v-model:value="dateRange" @change="onDateRangeChange" />
        </a-space>
      </div>

      <!-- 日历视图 -->
      <div class="calendar-container">
        <a-calendar v-model:value="selectedDate" :fullscreen="false" @select="onDateSelect">
          <template #dateCellRender="{ current }">
            <div class="calendar-cell">
              <div class="cell-date">{{ current.date() }}</div>
              <div class="cell-tasks">
                <div 
                  v-for="task in getTasksByDate(current)" 
                  :key="task.id" 
                  class="task-item"
                  :class="getTaskStatusClass(task)"
                  @click="showTaskDetail(task)">
                  <div class="task-info">
                    <span class="task-area">{{ task.areaName }}</span>
                    <span class="task-inspector">{{ task.inspectorName }}</span>
                  </div>
                  <div class="task-time">{{ formatTime(task.scheduledTime) }}</div>
                </div>
              </div>
            </div>
          </template>
        </a-calendar>
      </div>

      <!-- 任务列表视图 -->
      <div class="task-list-container" style="margin-top: 20px;">
        <a-table 
          :dataSource="filteredTasks" 
          :columns="taskColumns"
          :loading="loading"
          :pagination="{ pageSize: 10 }"
          row-key="id"
          size="small">
          
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'status'">
              <a-tag :color="getStatusColor(record.status)">
                {{ getStatusText(record.status) }}
              </a-tag>
            </template>
            
            <template v-if="column.key === 'priority'">
              <a-tag :color="getPriorityColor(record.priority)">
                {{ getPriorityText(record.priority) }}
              </a-tag>
            </template>
            
            <template v-if="column.key === 'action'">
              <a-space>
                <a-button type="link" size="small" @click="showTaskDetail(record)">查看</a-button>
                <a-button type="link" size="small" @click="editTask(record)" :disabled="record.status === 'completed'">编辑</a-button>
                <a-popconfirm title="确定要删除这个任务吗？" @confirm="deleteTaskHandler(record.id)">
                  <a-button type="link" size="small" danger :disabled="record.status === 'completed'">删除</a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </template>
        </a-table>
      </div>
    </a-card>

    <!-- 创建/编辑任务弹窗 -->
    <a-modal 
      v-model:visible="taskModalVisible" 
      :title="taskModalTitle"
      width="600px"
      @ok="handleTaskSave"
      @cancel="handleTaskCancel">
      
      <a-form :model="taskForm" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" ref="taskFormRef">
        <a-form-item label="任务名称" name="taskName" :rules="[{ required: true, message: '请输入任务名称' }]">
          <a-input v-model:value="taskForm.taskName" placeholder="请输入任务名称" />
        </a-form-item>
        
        <a-form-item label="巡检区域" name="areaId" :rules="[{ required: true, message: '请选择巡检区域' }]">
          <a-select v-model:value="taskForm.areaId" placeholder="请选择巡检区域" @change="onAreaChange">
            <a-select-option v-for="area in areas" :key="area.id" :value="area.id">
              {{ area.areaName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="分配巡检员" name="inspectorId" :rules="[{ required: true, message: '请选择巡检员' }]">
          <a-select v-model:value="taskForm.inspectorId" placeholder="请选择巡检员">
            <a-select-option v-for="inspector in inspectors" :key="inspector.id" :value="inspector.id">
              {{ inspector.realName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="计划时间" name="scheduledTime" :rules="[{ required: true, message: '请选择计划时间' }]">
          <a-date-picker 
            v-model:value="taskForm.scheduledTime" 
            show-time 
            format="YYYY-MM-DD HH:mm"
            placeholder="请选择计划时间"
            style="width: 100%"
            @change="checkScheduleConflictHandler" />
        </a-form-item>
        
        <a-form-item label="任务优先级" name="priority">
          <a-select v-model:value="taskForm.priority" placeholder="请选择优先级">
            <a-select-option value="low">低</a-select-option>
            <a-select-option value="normal">普通</a-select-option>
            <a-select-option value="high">高</a-select-option>
            <a-select-option value="urgent">紧急</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="任务描述" name="description">
          <a-textarea v-model:value="taskForm.description" placeholder="请输入任务描述" :rows="3" />
        </a-form-item>
        
        <!-- 冲突检测提示 -->
        <a-alert 
          v-if="conflictWarning" 
          :message="conflictWarning" 
          type="warning" 
          show-icon 
          style="margin-bottom: 16px;" />
      </a-form>
    </a-modal>

    <!-- 任务详情弹窗 -->
    <a-modal 
      v-model:visible="detailModalVisible" 
      title="任务详情"
      width="500px"
      :footer="null">
      
      <a-descriptions :column="1" v-if="selectedTask">
        <a-descriptions-item label="任务名称">{{ selectedTask.taskName }}</a-descriptions-item>
        <a-descriptions-item label="巡检区域">{{ selectedTask.areaName }}</a-descriptions-item>
        <a-descriptions-item label="巡检员">{{ selectedTask.inspectorName }}</a-descriptions-item>
        <a-descriptions-item label="计划时间">{{ formatDateTime(selectedTask.scheduledTime) }}</a-descriptions-item>
        <a-descriptions-item label="任务状态">
          <a-tag :color="getStatusColor(selectedTask.status)">
            {{ getStatusText(selectedTask.status) }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="优先级">
          <a-tag :color="getPriorityColor(selectedTask.priority)">
            {{ getPriorityText(selectedTask.priority) }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ formatDateTime(selectedTask.createTime) }}</a-descriptions-item>
        <a-descriptions-item label="任务描述" v-if="selectedTask.description">
          {{ selectedTask.description }}
        </a-descriptions-item>
      </a-descriptions>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'
import { getTasks, createTask, updateTask, deleteTask, checkScheduleConflict } from '@/api/schedule'
import { getUsers } from '@/api/user'
import { getAreas } from '@/api/area'

// 响应式数据
const loading = ref(false)
const selectedDate = ref(dayjs())
const dateRange = ref([])
const filterInspector = ref(undefined)
const filterArea = ref(undefined)

const tasks = ref([])
const inspectors = ref([])
const areas = ref([])

const taskModalVisible = ref(false)
const detailModalVisible = ref(false)
const taskModalTitle = ref('新建任务')
const selectedTask = ref(null)
const conflictWarning = ref('')
const taskFormRef = ref()

const taskForm = reactive({
  id: null,
  taskName: '',
  areaId: null,
  inspectorId: null,
  scheduledTime: null,
  priority: 'normal',
  description: ''
})

// 计算属性
const filteredTasks = computed(() => {
  let filtered = tasks.value
  
  if (filterInspector.value) {
    filtered = filtered.filter(task => task.inspectorId === filterInspector.value)
  }
  
  if (filterArea.value) {
    filtered = filtered.filter(task => task.areaId === filterArea.value)
  }
  
  if (dateRange.value && dateRange.value.length === 2) {
    const [start, end] = dateRange.value
    filtered = filtered.filter(task => {
      const taskDate = dayjs(task.scheduledTime)
      return taskDate.isAfter(start) && taskDate.isBefore(end.add(1, 'day'))
    })
  }
  
  return filtered
})

// 表格列定义
const taskColumns = [
  { title: '任务名称', dataIndex: 'taskName', key: 'taskName' },
  { title: '巡检区域', dataIndex: 'areaName', key: 'areaName' },
  { title: '巡检员', dataIndex: 'inspectorName', key: 'inspectorName' },
  { title: '计划时间', dataIndex: 'scheduledTime', key: 'scheduledTime', 
    customRender: ({ text }) => formatDateTime(text) },
  { title: '状态', key: 'status' },
  { title: '优先级', key: 'priority' },
  { title: '操作', key: 'action', width: 200 }
]

// 方法定义
const loadScheduleData = async () => {
  loading.value = true
  try {
    const params = {
      page: 1,
      size: 100
    }
    
    if (filterInspector.value) params.inspectorId = filterInspector.value
    if (filterArea.value) params.areaId = filterArea.value
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0].format('YYYY-MM-DD')
      params.endDate = dateRange.value[1].format('YYYY-MM-DD')
    }
    
    console.log('发送任务列表请求，参数:', params)
    const res = await getTasks(params)
    console.log('任务列表API响应:', res)
    
    // 正确处理响应数据结构
    if (res && res.data) {
      // 后端返回的数据结构是 { list: [...], total: number }
      tasks.value = res.data.list || res.data.records || []
      console.log('解析后的任务列表:', tasks.value)
    } else if (res && Array.isArray(res)) {
      // 如果直接返回数组
      tasks.value = res
    } else {
      tasks.value = []
      console.warn('未知的响应数据结构:', res)
    }
  } catch (error) {
    console.error('加载排班数据失败:', error)
    message.error('加载排班数据失败')
    tasks.value = []
  } finally {
    loading.value = false
  }
}

const loadInspectors = async () => {
  try {
    const res = await getUsers({ 
      role: 'inspector', 
      status: 'active', 
      page: 1,
      size: 100 
    })
    console.log('巡检员API响应:', res)
    
    // 正确处理响应数据结构
    if (res && res.data) {
      inspectors.value = res.data.list || res.data.records || []
    } else if (res && Array.isArray(res)) {
      inspectors.value = res
    } else {
      inspectors.value = []
    }
    console.log('解析后的巡检员列表:', inspectors.value)
  } catch (error) {
    console.error('加载巡检员失败:', error)
    message.error('加载巡检员失败')
    inspectors.value = []
  }
}

const loadAreas = async () => {
  try {
    const res = await getAreas({ page: 1, size: 100 })
    console.log('区域API响应:', res)
    
    // 正确处理响应数据结构
    if (res && res.data) {
      areas.value = res.data.list || res.data.records || []
    } else if (res && Array.isArray(res)) {
      areas.value = res
    } else {
      areas.value = []
    }
    console.log('解析后的区域列表:', areas.value)
  } catch (error) {
    console.error('加载区域失败:', error)
    message.error('加载区域失败')
    areas.value = []
  }
}

const getTasksByDate = (date) => {
  const dateStr = date.format('YYYY-MM-DD')
  return tasks.value.filter(task => {
    return dayjs(task.scheduledTime).format('YYYY-MM-DD') === dateStr
  })
}

const showCreateTask = () => {
  console.log('点击新建任务按钮')
  console.log('当前模态框状态:', taskModalVisible.value)
  taskModalTitle.value = '新建任务'
  resetTaskForm()
  taskModalVisible.value = true
  console.log('设置模态框状态为:', taskModalVisible.value)
  console.log('模态框标题:', taskModalTitle.value)
}

const editTask = (task) => {
  taskModalTitle.value = '编辑任务'
  Object.assign(taskForm, {
    id: task.id,
    taskName: task.taskName,
    areaId: task.areaId,
    inspectorId: task.inspectorId,
    scheduledTime: dayjs(task.scheduledTime),
    priority: task.priority,
    description: task.description
  })
  taskModalVisible.value = true
}

const resetTaskForm = () => {
  Object.assign(taskForm, {
    id: null,
    taskName: '',
    areaId: null,
    inspectorId: null,
    scheduledTime: null,
    priority: 'normal',
    description: ''
  })
  conflictWarning.value = ''
}

const handleTaskSave = async () => {
  try {
    await taskFormRef.value.validate()
    
    const formData = {
      ...taskForm,
      scheduledTime: taskForm.scheduledTime ? taskForm.scheduledTime.format('YYYY-MM-DD HH:mm:ss') : null
    }
    
    if (taskForm.id) {
      await updateTask(taskForm.id, formData)
      message.success('任务更新成功')
    } else {
      await createTask(formData)
      message.success('任务创建成功')
    }
    
    taskModalVisible.value = false
    loadScheduleData()
  } catch (error) {
    console.error('保存任务失败:', error)
    message.error('保存任务失败')
  }
}

const handleTaskCancel = () => {
  taskModalVisible.value = false
  resetTaskForm()
}

const deleteTaskHandler = async (taskId) => {
  try {
    await deleteTask(taskId)
    message.success('任务删除成功')
    loadScheduleData()
  } catch (error) {
    console.error('删除任务失败:', error)
    message.error('删除任务失败')
  }
}

const showTaskDetail = (task) => {
  selectedTask.value = task
  detailModalVisible.value = true
}

const checkScheduleConflictHandler = async () => {
  if (!taskForm.inspectorId || !taskForm.scheduledTime) {
    conflictWarning.value = ''
    return
  }
  
  try {
    const res = await checkScheduleConflict({
      inspectorId: taskForm.inspectorId,
      scheduledTime: taskForm.scheduledTime.format('YYYY-MM-DD HH:mm:ss'),
      excludeTaskId: taskForm.id
    })
    
    if (res.data && res.data.hasConflict) {
      conflictWarning.value = `时间冲突：${res.data.conflictMessage}`
    } else {
      conflictWarning.value = ''
    }
  } catch (error) {
    console.error('冲突检测失败:', error)
  }
}

const refreshSchedule = () => {
  loadScheduleData()
}

const onDateRangeChange = (dates) => {
  dateRange.value = dates
  loadScheduleData()
}

const onDateSelect = (date) => {
  selectedDate.value = date
}

const onAreaChange = () => {
  // 区域变更时可以做一些处理
}

// 格式化方法
const formatTime = (time) => {
  return dayjs(time).format('HH:mm')
}

const formatDateTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const getTaskStatusClass = (task) => {
  return {
    'task-pending': task.status === 'pending',
    'task-in-progress': task.status === 'in-progress', 
    'task-completed': task.status === 'completed',
    'task-cancelled': task.status === 'cancelled'
  }
}

const getStatusColor = (status) => {
  const colors = {
    'pending': 'orange',
    'in-progress': 'blue',
    'completed': 'green',
    'cancelled': 'red'
  }
  return colors[status] || 'default'
}

const getStatusText = (status) => {
  const texts = {
    'pending': '待执行',
    'in-progress': '执行中',
    'completed': '已完成',
    'cancelled': '已取消'
  }
  return texts[status] || status
}

const getPriorityColor = (priority) => {
  const colors = {
    'low': 'default',
    'normal': 'blue',
    'high': 'orange',
    'urgent': 'red'
  }
  return colors[priority] || 'default'
}

const getPriorityText = (priority) => {
  const texts = {
    'low': '低',
    'normal': '普通',
    'high': '高',
    'urgent': '紧急'
  }
  return texts[priority] || priority
}

// 生命周期
onMounted(() => {
  loadScheduleData()
  loadInspectors()
  loadAreas()
})
</script>

<style scoped>
.task-schedule {
  padding: 20px;
}

.schedule-toolbar {
  margin-bottom: 16px;
}

.calendar-container {
  margin-bottom: 20px;
}

.calendar-cell {
  height: 100%;
  position: relative;
}

.cell-date {
  font-weight: bold;
  margin-bottom: 4px;
}

.cell-tasks {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.task-item {
  padding: 2px 4px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.task-item:hover {
  opacity: 0.8;
}

.task-pending {
  background: #fff7e6;
  border: 1px solid #ffd591;
  color: #d46b08;
}

.task-in-progress {
  background: #e6f7ff;
  border: 1px solid #91d5ff;
  color: #0958d9;
}

.task-completed {
  background: #f6ffed;
  border: 1px solid #b7eb8f;
  color: #389e0d;
}

.task-cancelled {
  background: #fff2f0;
  border: 1px solid #ffccc7;
  color: #cf1322;
}

.task-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.task-area {
  font-weight: 500;
}

.task-inspector {
  font-size: 11px;
  opacity: 0.8;
}

.task-time {
  font-size: 11px;
  opacity: 0.7;
  text-align: center;
}

.schedule-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.task-list-container {
  background: white;
  border-radius: 8px;
  padding: 16px;
}
</style> 