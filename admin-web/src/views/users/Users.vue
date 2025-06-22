<template>
  <div>
    <!-- 筛选条件 -->
    <div class="card">
      <div class="card-title">筛选条件</div>
      <a-form layout="inline" :model="queryParams">
        <a-form-item label="用户名">
          <a-input v-model:value="queryParams.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="部门">
          <a-select v-model:value="queryParams.department" placeholder="全部部门" style="width: 160px">
            <a-select-option value="">全部部门</a-select-option>
            <a-select-option value="ops">运维部</a-select-option>
            <a-select-option value="security">安全部</a-select-option>
            <a-select-option value="it">IT部</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="角色">
          <a-select v-model:value="queryParams.role" placeholder="全部角色" style="width: 160px">
            <a-select-option value="">全部角色</a-select-option>
            <a-select-option value="admin">管理员</a-select-option>
            <a-select-option value="inspector">巡检员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="queryParams.status" placeholder="全部状态" style="width: 160px">
            <a-select-option value="">全部状态</a-select-option>
            <a-select-option value="active">活跃</a-select-option>
            <a-select-option value="inactive">未激活</a-select-option>
            <a-select-option value="locked">已锁定</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery">查询</a-button>
          <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
        </a-form-item>
      </a-form>
    </div>

    <!-- 用户列表 -->
    <div class="card">
      <div style="display: flex; justify-content: space-between; margin-bottom: 16px;">
        <div class="card-title">用户列表</div>
        <a-button type="primary" @click="handleAdd">
          <template #icon><plus-outlined /></template>
          添加用户
        </a-button>
      </div>
      <a-table
        :columns="columns"
        :data-source="userList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
        row-key="id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'role'">
            <a-tag :color="getRoleColor(record.role)">
              {{ getRoleText(record.role) }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'status'">
            <a-tag :color="getStatusColor(record.status)">
              {{ getStatusText(record.status) }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a @click="handleEdit(record)">编辑</a>
              <a @click="handlePermission(record)">权限</a>
              <a-popconfirm
                title="确定要删除此用户吗？"
                ok-text="确定"
                cancel-text="取消"
                @confirm="handleDelete(record)"
              >
                <a style="color: #ff4d4f">删除</a>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 添加/编辑用户弹窗 -->
    <a-modal
      v-model:visible="userModal.visible"
      :title="userModal.isEdit ? '编辑用户' : '添加用户'"
      @ok="handleUserSubmit"
      @cancel="cancelUserModal"
      :confirmLoading="userModal.loading"
    >
      <a-form
        :model="userForm"
        :rules="rules"
        ref="userFormRef"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="用户名" name="username">
          <a-input v-model:value="userForm.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="姓名" name="realname">
          <a-input v-model:value="userForm.realname" placeholder="请输入姓名" />
        </a-form-item>
        <a-form-item 
          label="密码" 
          name="password"
          :rules="userModal.isEdit ? [{ required: false }] : [{ required: true, message: '请输入密码' }]"
        >
          <a-input-password v-model:value="userForm.password" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item label="部门" name="department">
          <a-select v-model:value="userForm.department" placeholder="请选择部门">
            <a-select-option value="ops">运维部</a-select-option>
            <a-select-option value="security">安全部</a-select-option>
            <a-select-option value="it">IT部</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="角色" name="role">
          <a-select v-model:value="userForm.role" placeholder="请选择角色">
            <a-select-option value="user">普通用户</a-select-option>
            <a-select-option value="inspector">巡检员</a-select-option>
            <a-select-option value="admin">管理员</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-select v-model:value="userForm.status" placeholder="请选择状态">
            <a-select-option value="active">活跃</a-select-option>
            <a-select-option value="inactive">未激活</a-select-option>
            <a-select-option value="locked">已锁定</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 权限设置弹窗 -->
    <a-modal
      v-model:visible="permissionModal.visible"
      title="权限设置"
      @ok="handlePermissionSubmit"
      @cancel="cancelPermissionModal"
      :confirmLoading="permissionModal.loading"
    >
      <template #title>权限设置 - {{ permissionModal.username }}</template>
      <div>
        <div style="margin-bottom: 16px; font-weight: bold;">权限分配</div>
        <a-checkbox-group v-model:value="permissionForm.permissions">
          <div style="margin-bottom: 8px;">
            <a-checkbox value="dashboard">仪表盘查看权限</a-checkbox>
          </div>
          <div style="margin-bottom: 8px;">
            <a-checkbox value="records_view">巡检记录查看权限</a-checkbox>
          </div>
          <div style="margin-bottom: 8px;">
            <a-checkbox value="records_all">查看所有巡检记录（否则只能查看自己的记录）</a-checkbox>
          </div>
          <div style="margin-bottom: 8px;">
            <a-checkbox value="records_export">巡检记录导出权限</a-checkbox>
          </div>
          <div style="margin-bottom: 8px;">
            <a-checkbox value="issues_view">问题列表查看权限</a-checkbox>
          </div>
          <div style="margin-bottom: 8px;">
            <a-checkbox value="issues_edit">问题处理权限</a-checkbox>
          </div>
          <div style="margin-bottom: 8px;">
            <a-checkbox value="user_manage">用户管理权限</a-checkbox>
          </div>
          <div style="margin-bottom: 8px;">
            <a-checkbox value="system_config">系统配置权限</a-checkbox>
          </div>
        </a-checkbox-group>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted, toRaw } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getUserList, addUser, updateUser, deleteUser, getUserPermissions, updateUserPermissions } from '@/api/user'

export default defineComponent({
  name: 'Users',
  components: {
    PlusOutlined
  },
  setup() {
    // 表格列定义
    const columns = [
      { title: '用户名', dataIndex: 'username', key: 'username' },
      { title: '姓名', dataIndex: 'realname', key: 'realname' },
      { title: '部门', dataIndex: 'department', key: 'department' },
      { title: '角色', dataIndex: 'role', key: 'role' },
      { title: '状态', dataIndex: 'status', key: 'status' },
      { title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
      { title: '操作', key: 'action' }
    ]

    // 查询参数
    const queryParams = reactive({
      username: '',
      department: '',
      role: '',
      status: '',
      pageNum: 1,
      pageSize: 10
    })

    // 分页配置
    const pagination = reactive({
      current: 1,
      pageSize: 10,
      total: 0,
      showSizeChanger: true,
      showTotal: (total) => `共 ${total} 条`
    })

    // 用户列表数据
    const loading = ref(false)
    const userList = ref([])

    // 用户表单
    const userFormRef = ref(null)
    const userForm = reactive({
      id: null,
      username: '',
      realname: '',
      password: '',
      department: 'ops',
      role: 'user',
      status: 'active'
    })

    // 用户表单验证规则
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      realname: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      department: [
        { required: true, message: '请选择部门', trigger: 'change' }
      ],
      role: [
        { required: true, message: '请选择角色', trigger: 'change' }
      ],
      status: [
        { required: true, message: '请选择状态', trigger: 'change' }
      ]
    }

    // 用户弹窗状态
    const userModal = reactive({
      visible: false,
      loading: false,
      isEdit: false
    })

    // 权限弹窗状态
    const permissionModal = reactive({
      visible: false,
      loading: false,
      userId: null,
      username: ''
    })

    // 权限表单
    const permissionForm = reactive({
      permissions: []
    })

    // 获取用户列表
    const getList = async () => {
      loading.value = true
      try {
        const { list, total } = await getUserList({
          ...queryParams
        })
        userList.value = list
        pagination.total = total
        pagination.current = queryParams.pageNum
        pagination.pageSize = queryParams.pageSize
      } catch (error) {
        console.error('Failed to get user list:', error)
      } finally {
        loading.value = false
      }
    }

    // 查询按钮点击
    const handleQuery = () => {
      queryParams.pageNum = 1
      getList()
    }

    // 重置查询
    const resetQuery = () => {
      Object.assign(queryParams, {
        username: '',
        department: '',
        role: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      })
      getList()
    }

    // 表格变化事件
    const handleTableChange = (pag) => {
      queryParams.pageNum = pag.current
      queryParams.pageSize = pag.pageSize
      getList()
    }

    // 获取状态颜色
    const getStatusColor = (status) => {
      switch (status) {
        case 'active': return 'green'
        case 'inactive': return 'orange'
        case 'locked': return 'red'
        default: return 'default'
      }
    }

    // 获取状态文本
    const getStatusText = (status) => {
      switch (status) {
        case 'active': return '活跃'
        case 'inactive': return '未激活'
        case 'locked': return '已锁定'
        default: return status
      }
    }

    // 添加用户
    const handleAdd = () => {
      userModal.isEdit = false
      userModal.visible = true
      userForm.id = null
      userForm.username = ''
      userForm.realname = ''
      userForm.password = ''
      userForm.department = 'ops'
      userForm.role = 'user'
      userForm.status = 'active'
    }

    // 编辑用户
    const handleEdit = (record) => {
      userModal.isEdit = true
      userModal.visible = true
      Object.assign(userForm, {
        id: record.id,
        username: record.username,
        realname: record.realname,
        password: '',
        department: record.department,
        role: record.role,
        status: record.status
      })
    }

    // 用户表单提交
    const handleUserSubmit = async () => {
      try {
        await userFormRef.value.validate()
        userModal.loading = true
        
        const formData = toRaw(userForm)
        if (userModal.isEdit) {
          await updateUser(formData.id, formData)
          message.success('更新用户成功')
        } else {
          await addUser(formData)
          message.success('添加用户成功')
        }
        
        userModal.visible = false
        getList()
      } catch (error) {
        console.error('Form validation failed:', error)
      } finally {
        userModal.loading = false
      }
    }

    // 取消用户弹窗
    const cancelUserModal = () => {
      userModal.visible = false
      userFormRef.value?.resetFields()
    }

    // 删除用户
    const handleDelete = async (record) => {
      try {
        await deleteUser(record.id)
        message.success('删除用户成功')
        getList()
      } catch (error) {
        console.error('Failed to delete user:', error)
      }
    }

    // 打开权限设置弹窗
    const handlePermission = async (record) => {
      permissionModal.userId = record.id
      permissionModal.username = record.username
      permissionModal.visible = true
      permissionModal.loading = true
      
      try {
        const permissions = await getUserPermissions(record.id)
        permissionForm.permissions = permissions || []
      } catch (error) {
        console.error('Failed to get user permissions:', error)
      } finally {
        permissionModal.loading = false
      }
    }

    // 权限表单提交
    const handlePermissionSubmit = async () => {
      permissionModal.loading = true
      try {
        await updateUserPermissions(permissionModal.userId, {
          permissions: toRaw(permissionForm.permissions)
        })
        message.success('权限设置成功')
        permissionModal.visible = false
      } catch (error) {
        console.error('Failed to update permissions:', error)
      } finally {
        permissionModal.loading = false
      }
    }

    // 取消权限弹窗
    const cancelPermissionModal = () => {
      permissionModal.visible = false
    }

    // 获取角色颜色
    const getRoleColor = (role) => {
      const colors = {
        admin: 'red',
        inspector: 'blue', 
        user: 'green'
      }
      return colors[role] || 'default'
    }

    // 获取角色文本
    const getRoleText = (role) => {
      const texts = {
        admin: '管理员',
        inspector: '巡检员',
        user: '普通用户'
      }
      return texts[role] || role
    }

    onMounted(() => {
      getList()
    })

    return {
      columns,
      queryParams,
      pagination,
      loading,
      userList,
      userFormRef,
      userForm,
      rules,
      userModal,
      permissionModal,
      permissionForm,
      handleQuery,
      resetQuery,
      handleTableChange,
      getStatusColor,
      getStatusText,
      handleAdd,
      handleEdit,
      handleUserSubmit,
      cancelUserModal,
      handleDelete,
      handlePermission,
      handlePermissionSubmit,
      cancelPermissionModal,
      getRoleColor,
      getRoleText
    }
  }
})
</script> 