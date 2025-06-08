<template>
  <a-layout class="layout">
    <a-layout-header class="header">
      <div class="logo">巡检管理系统后台</div>
      <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="light"
        mode="horizontal"
        :style="{ lineHeight: '56px' }"
      >
        <a-menu-item v-for="item in menuItems" :key="item.key" @click="() => navigateTo(item.path)">
          {{ item.label }}
        </a-menu-item>
      </a-menu>
      <div class="user-info">
        <a-dropdown>
          <a class="ant-dropdown-link" @click.prevent>
            <user-outlined /> {{ userInfo.name }} <down-outlined />
          </a>
          <template #overlay>
            <a-menu>
              <a-menu-item key="0">
                <a @click="handleLogout">退出登录</a>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </a-layout-header>
    <a-layout-content>
      <div class="container">
        <router-view />
      </div>
    </a-layout-content>
    <a-layout-footer class="footer">
      © 2025 巡检管理系统后台
    </a-layout-footer>
  </a-layout>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { UserOutlined, DownOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getUserInfo, logout } from '@/api/user'

export default {
  name: 'Layout',
  components: {
    UserOutlined,
    DownOutlined
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const userInfo = ref({ name: '管理员' })
    
    const menuItems = [
      { key: 'dashboard', label: '仪表盘', path: '/' },
      { key: 'records', label: '巡检记录', path: '/records' },
      { key: 'issues', label: '问题列表', path: '/issues' },
      { key: 'users', label: '用户管理', path: '/users' },
      { key: 'areas', label: '巡检区域', path: '/areas' },
      { key: 'tasks', label: '任务管理', path: '/tasks' },
      { key: 'forms', label: '表单管理', path: '/forms' },
      { key: 'statistics', label: '统计分析', path: '/statistics' },
      { key: 'settings', label: '系统设置', path: '/settings' }
    ]
    
    const selectedKeys = computed(() => {
      const path = route.path
      if (path === '/') return ['dashboard']
      return [path.split('/')[1]]
    })
    
    const navigateTo = (path) => {
      router.push(path)
    }
    
    const handleLogout = async () => {
      try {
        await logout()
        localStorage.removeItem('token')
        message.success('退出登录成功')
        router.push('/login')
      } catch (error) {
        console.error('Logout failed:', error)
      }
    }
    
    onMounted(async () => {
      try {
        const data = await getUserInfo()
        userInfo.value = data
      } catch (error) {
        console.error('Failed to get user info:', error)
      }
    })
    
    return {
      menuItems,
      selectedKeys,
      userInfo,
      navigateTo,
      handleLogout
    }
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
}
.header {
  display: flex;
  align-items: center;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 0 32px;
}
.logo {
  font-weight: bold;
  color: #1890ff;
  font-size: 22px;
  margin-right: 32px;
}
.user-info {
  margin-left: auto;
  cursor: pointer;
}
.ant-dropdown-link {
  color: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  gap: 8px;
}
</style> 