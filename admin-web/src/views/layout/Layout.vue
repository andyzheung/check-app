<template>
  <a-layout class="layout">
    <a-layout-header class="header">
      <div class="logo">巡检管理系统后台</div>
      <div class="header-right">
        <a-dropdown>
          <a class="dropdown-link" @click.prevent>
            {{ userInfo.realName || '管理员' }}
            <down-outlined />
          </a>
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="logout">
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </a-layout-header>
    <a-layout>
      <a-layout-sider width="200" class="sider">
        <a-menu
          mode="inline"
          :selected-keys="[selectedKey]"
          :open-keys="openKeys"
          style="height: 100%"
          @click="handleMenuClick"
        >
          <a-menu-item key="dashboard">
            <dashboard-outlined />
            <span>仪表盘</span>
          </a-menu-item>
          <a-sub-menu key="records">
            <template #title>
              <span>
                <file-outlined />
                <span>巡检记录</span>
              </span>
            </template>
            <a-menu-item key="records-list">巡检记录列表</a-menu-item>
            <a-menu-item key="records-export">导出记录</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="issues">
            <template #title>
              <span>
                <exception-outlined />
                <span>问题列表</span>
              </span>
            </template>
            <a-menu-item key="issues-list">问题列表</a-menu-item>
            <a-menu-item key="issues-statistics">问题统计</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="users">
            <template #title>
              <span>
                <user-outlined />
                <span>用户管理</span>
              </span>
            </template>
            <a-menu-item key="users-list">用户列表</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout class="content-layout">
        <a-layout-content class="content">
          <router-view></router-view>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script>
import { defineComponent, ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  DashboardOutlined, 
  FileOutlined, 
  ExceptionOutlined, 
  UserOutlined,
  DownOutlined 
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

export default defineComponent({
  name: 'Layout',
  components: {
    DashboardOutlined,
    FileOutlined,
    ExceptionOutlined,
    UserOutlined,
    DownOutlined
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    
    // 当前选中的菜单项
    const selectedKey = ref('dashboard')
    // 当前展开的子菜单
    const openKeys = ref(['dashboard'])
    
    // 用户信息
    const userInfo = reactive({
      id: 1,
      username: 'admin',
      realName: '系统管理员'
    })
    
    // 监听路由变化，更新选中的菜单项
    watch(() => route.path, (path) => {
      if (path.includes('/dashboard')) {
        selectedKey.value = 'dashboard'
        openKeys.value = ['dashboard']
      } else if (path.includes('/records')) {
        selectedKey.value = path.includes('/export') ? 'records-export' : 'records-list'
        openKeys.value = ['records']
      } else if (path.includes('/issues')) {
        selectedKey.value = path.includes('/statistics') ? 'issues-statistics' : 'issues-list'
        openKeys.value = ['issues']
      } else if (path.includes('/users')) {
        selectedKey.value = 'users-list'
        openKeys.value = ['users']
      }
    }, { immediate: true })
    
    // 菜单点击事件
    const handleMenuClick = ({ key }) => {
      switch(key) {
        case 'dashboard':
          router.push('/dashboard')
          break
        case 'records-list':
          router.push('/records/list')
          break
        case 'records-export':
          router.push('/records/export')
          break
        case 'issues-list':
          router.push('/issues/list')
          break
        case 'issues-statistics':
          router.push('/issues/statistics')
          break
        case 'users-list':
          router.push('/users/list')
          break
        default:
          router.push('/')
      }
    }
    
    // 退出登录
    const logout = () => {
      localStorage.removeItem('token')
      message.success('退出成功')
      router.push('/login')
    }
    
    return {
      selectedKey,
      openKeys,
      userInfo,
      handleMenuClick,
      logout
    }
  }
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  z-index: 1;
}

.logo {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
}

.header-right {
  display: flex;
  align-items: center;
}

.dropdown-link {
  display: flex;
  align-items: center;
  color: rgba(0, 0, 0, 0.65);
}

.sider {
  background: #fff;
  overflow: auto;
  height: calc(100vh - 64px);
  position: fixed;
  left: 0;
  box-shadow: 1px 0 4px rgba(0, 0, 0, 0.1);
}

.content-layout {
  margin-left: 200px;
}

.content {
  margin: 24px;
  padding: 24px;
  background: #fff;
  min-height: calc(100vh - 112px);
}
</style> 