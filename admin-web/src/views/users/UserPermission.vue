<template>
  <div class="user-permission">
    <a-card title="用户权限管理" class="permission-card">
      <template #extra>
        <a-button type="primary" @click="refreshData">
          <template #icon><ReloadOutlined /></template>
          刷新
        </a-button>
      </template>

      <a-row :gutter="16">
        <!-- AD用户搜索 -->
        <a-col :span="10">
          <a-card title="AD用户搜索" size="small" class="search-card">
            <a-input-search 
              v-model:value="searchKeyword"
              placeholder="搜索AD用户（姓名、用户名、部门）"
              enter-button="搜索"
              @search="searchAdUsers"
              class="search-input" />
            
            <a-divider />
            
            <a-list 
              :dataSource="adUsers" 
              :loading="adUsersLoading"
              class="ad-user-list">
              <template #renderItem="{ item }">
                <a-list-item>
                  <a-list-item-meta>
                    <template #title>
                      <div class="user-title">
                        {{ item.displayName }}
                        <a-tag v-if="item.hasSystemRole" :color="getRoleColor(item.systemRole)">
                          {{ getRoleText(item.systemRole) }}
                        </a-tag>
                        <a-tag v-else color="default">未分配</a-tag>
                      </div>
                    </template>
                    <template #description>
                      <div class="user-desc">
                        <div>用户名: {{ item.username }}</div>
                        <div>部门: {{ item.department }}</div>
                        <div>邮箱: {{ item.email }}</div>
                      </div>
                    </template>
                  </a-list-item-meta>
                  
                  <template #actions>
                    <a-button 
                      type="primary" 
                      size="small"
                      @click="showAssignRole(item)"
                      :disabled="item.hasSystemRole">
                      分配权限
                    </a-button>
                    <a-button 
                      v-if="item.hasSystemRole"
                      type="default" 
                      size="small"
                      @click="showUpdateRole(item)">
                      修改权限
                    </a-button>
                  </template>
                </a-list-item>
              </template>
              
              <template #loadMore>
                <div v-if="adUsers.length === 0 && !adUsersLoading" class="empty-state">
                  <a-empty description="暂无AD用户数据" />
                </div>
              </template>
            </a-list>
          </a-card>
        </a-col>

        <!-- 系统用户管理 -->
        <a-col :span="14">
          <a-card title="系统用户管理" size="small" class="system-users-card">
            <template #extra>
              <a-select 
                v-model:value="filterRole" 
                placeholder="筛选角色"
                style="width: 120px"
                @change="loadSystemUsers">
                <a-select-option value="">全部</a-select-option>
                <a-select-option value="admin">管理员</a-select-option>
                <a-select-option value="inspector">巡检员</a-select-option>
                <a-select-option value="user">普通用户</a-select-option>
              </a-select>
            </template>

            <a-table 
              :dataSource="systemUsers" 
              :columns="userColumns"
              :loading="systemUsersLoading"
              :pagination="{ pageSize: 10 }"
              row-key="id"
              size="small">
              
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'userType'">
                  <a-tag :color="record.isAdUser ? 'blue' : 'green'">
                    {{ record.isAdUser ? 'AD用户' : '本地用户' }}
                  </a-tag>
                </template>
                
                <template v-if="column.key === 'role'">
                  <a-tag :color="getRoleColor(record.role)">
                    {{ getRoleText(record.role) }}
                  </a-tag>
                </template>
                
                <template v-if="column.key === 'status'">
                  <a-tag :color="record.status === 'active' ? 'green' : 'red'">
                    {{ record.status === 'active' ? '活跃' : '未激活' }}
                  </a-tag>
                </template>
                
                <template v-if="column.key === 'action'">
                  <a-space>
                    <a-button 
                      type="link" 
                      size="small"
                      @click="showUpdateRole(record)">
                      修改角色
                    </a-button>
                    <a-popconfirm 
                      title="确定要禁用该用户吗？"
                      @confirm="toggleUserStatus(record)">
                      <a-button 
                        type="link" 
                        size="small"
                        :disabled="record.username === 'admin'">
                        {{ record.status === 'active' ? '禁用' : '启用' }}
                      </a-button>
                    </a-popconfirm>
                  </a-space>
                </template>
              </template>
            </a-table>
          </a-card>
        </a-col>
      </a-row>
    </a-card>

    <!-- 分配角色弹窗 -->
    <a-modal 
      v-model:visible="roleModalVisible" 
      :title="modalTitle"
      @ok="handleAssignRole"
      @cancel="cancelAssignRole">
      
      <a-form :model="roleForm" layout="vertical">
        <a-form-item label="用户信息">
          <a-descriptions :column="1" size="small">
            <a-descriptions-item label="姓名">{{ selectedUser?.displayName }}</a-descriptions-item>
            <a-descriptions-item label="用户名">{{ selectedUser?.username }}</a-descriptions-item>
            <a-descriptions-item label="部门">{{ selectedUser?.department }}</a-descriptions-item>
            <a-descriptions-item label="邮箱">{{ selectedUser?.email }}</a-descriptions-item>
          </a-descriptions>
        </a-form-item>
        
        <a-form-item label="分配角色" required>
          <a-radio-group v-model:value="roleForm.role">
            <a-radio value="admin">管理员</a-radio>
            <a-radio value="inspector">巡检员</a-radio>
            <a-radio value="user">普通用户</a-radio>
          </a-radio-group>
          <div class="role-description">
            <p v-if="roleForm.role === 'admin'">管理员：拥有系统所有权限，可以管理用户、配置系统等</p>
            <p v-if="roleForm.role === 'inspector'">巡检员：可以执行巡检任务，查看和处理问题</p>
            <p v-if="roleForm.role === 'user'">普通用户：只能查看基本信息，无法执行巡检任务</p>
          </div>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { ReloadOutlined } from '@ant-design/icons-vue';
import request from '@/utils/request';

// 响应式数据
const searchKeyword = ref('');
const adUsers = ref([]);
const adUsersLoading = ref(false);
const systemUsers = ref([]);
const systemUsersLoading = ref(false);
const filterRole = ref('');

const roleModalVisible = ref(false);
const selectedUser = ref(null);
const modalTitle = ref('');
const roleForm = reactive({
  role: 'inspector'
});

// 表格列定义
const userColumns = [
  {
    title: '姓名',
    dataIndex: 'realName',
    key: 'realName',
  },
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: '用户类型',
    key: 'userType',
  },
  {
    title: '角色',
    key: 'role',
  },
  {
    title: '状态',
    key: 'status',
  },
  {
    title: '最后登录',
    dataIndex: 'lastLoginTime',
    key: 'lastLoginTime',
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
  },
];

// 方法
const searchAdUsers = async () => {
  adUsersLoading.value = true;
  try {
    const response = await request.get('/api/admin/ad-users/search', {
      params: { keyword: searchKeyword.value }
    });
    adUsers.value = response.data || [];
  } catch (error) {
    message.error('搜索AD用户失败');
    console.error('搜索AD用户失败:', error);
  } finally {
    adUsersLoading.value = false;
  }
};

const loadAllAdUsers = async () => {
  adUsersLoading.value = true;
  try {
    const response = await request.get('/api/admin/ad-users');
    adUsers.value = response.data || [];
  } catch (error) {
    message.error('加载AD用户失败');
    console.error('加载AD用户失败:', error);
  } finally {
    adUsersLoading.value = false;
  }
};

const loadSystemUsers = async () => {
  systemUsersLoading.value = true;
  try {
    const response = await request.get('/api/users', {
      params: { 
        role: filterRole.value,
        page: 1,
        size: 100
      }
    });
    systemUsers.value = response.data?.records || [];
  } catch (error) {
    message.error('加载系统用户失败');
    console.error('加载系统用户失败:', error);
  } finally {
    systemUsersLoading.value = false;
  }
};

const showAssignRole = (user) => {
  selectedUser.value = user;
  modalTitle.value = '分配角色';
  roleForm.role = 'inspector';
  roleModalVisible.value = true;
};

const showUpdateRole = (user) => {
  selectedUser.value = user;
  modalTitle.value = '修改角色';
  roleForm.role = user.systemRole || user.role;
  roleModalVisible.value = true;
};

const handleAssignRole = async () => {
  try {
    await request.post(`/api/admin/ad-users/${selectedUser.value.username}/assign-role`, null, {
      params: { role: roleForm.role }
    });
    
    message.success('角色分配成功');
    roleModalVisible.value = false;
    
    // 刷新数据
    await Promise.all([loadAllAdUsers(), loadSystemUsers()]);
  } catch (error) {
    message.error('角色分配失败');
    console.error('角色分配失败:', error);
  }
};

const cancelAssignRole = () => {
  roleModalVisible.value = false;
  selectedUser.value = null;
};

const toggleUserStatus = async (user) => {
  try {
    const newStatus = user.status === 'active' ? 'inactive' : 'active';
    await request.put(`/api/users/${user.id}`, {
      ...user,
      status: newStatus
    });
    
    message.success('用户状态更新成功');
    await loadSystemUsers();
  } catch (error) {
    message.error('更新用户状态失败');
    console.error('更新用户状态失败:', error);
  }
};

const refreshData = async () => {
  await Promise.all([loadAllAdUsers(), loadSystemUsers()]);
};

const getRoleColor = (role) => {
  const colors = {
    admin: 'red',
    inspector: 'blue',
    user: 'green'
  };
  return colors[role] || 'default';
};

const getRoleText = (role) => {
  const texts = {
    admin: '管理员',
    inspector: '巡检员',
    user: '普通用户'
  };
  return texts[role] || role;
};

// 生命周期
onMounted(() => {
  refreshData();
});
</script>

<style scoped>
.user-permission {
  padding: 24px;
}

.permission-card {
  margin-bottom: 24px;
}

.search-card,
.system-users-card {
  height: 600px;
  overflow: auto;
}

.search-input {
  margin-bottom: 16px;
}

.ad-user-list {
  max-height: 480px;
  overflow-y: auto;
}

.user-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-desc {
  font-size: 12px;
  color: #666;
}

.user-desc div {
  margin-bottom: 2px;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
}

.role-description {
  margin-top: 8px;
  padding: 8px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
}

.role-description p {
  margin: 0;
}
</style> 