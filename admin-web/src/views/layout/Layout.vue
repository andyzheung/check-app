<template>
  <div class="layout">
    <!-- 顶部导航 -->
    <div class="nav">
      <div class="logo">巡检管理系统后台</div>
      <div class="menu">
        <a @click="handleMenuClick('dashboard')" :class="{ active: selectedKey === 'dashboard' }">仪表盘</a>
        <a @click="handleMenuClick('records')" :class="{ active: selectedKey.startsWith('records') }">巡检记录</a>
        <a @click="handleMenuClick('schedule')" :class="{ active: selectedKey === 'schedule' }">巡检排班</a>
        <a @click="handleMenuClick('issues')" :class="{ active: selectedKey.startsWith('issues') }">问题列表</a>
        <a @click="handleMenuClick('users')" :class="{ active: selectedKey.startsWith('users') }">用户管理</a>
        <a @click="handleMenuClick('config')" :class="{ active: selectedKey.startsWith('config') }">系统配置</a>
      </div>
      <div class="user">
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
    </div>
    
    <!-- 内容区域 -->
    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { DownOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

export default defineComponent({
  name: 'Layout',
  components: {
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
      } else if (path.includes('/records')) {
        selectedKey.value = 'records'
      } else if (path.includes('/schedule')) {
        selectedKey.value = 'schedule'
      } else if (path.includes('/issues')) {
        selectedKey.value = 'issues'
      } else if (path.includes('/users')) {
        selectedKey.value = 'users'
      } else if (path.includes('/areas') || path.includes('/config')) {
        selectedKey.value = 'config'
      }
    }, { immediate: true })
    
    // 菜单点击事件
    const handleMenuClick = (key) => {
      switch(key) {
        case 'dashboard':
          router.push('/dashboard')
          break
        case 'records':
          router.push('/records/list')
          break
        case 'schedule':
          router.push('/schedule/tasks')
          break
        case 'issues':
          router.push('/issues/list')
          break
        case 'users':
          router.push('/users/list')
          break
        case 'config':
          router.push('/areas/config')
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
  background: #f0f2f5;
}

.nav {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  padding: 0 32px;
  display: flex;
  align-items: center;
  height: 56px;
  justify-content: space-between;
  z-index: 1;
}

.logo {
  font-weight: bold;
  color: #1890ff;
  font-size: 22px;
}

.menu {
  display: flex;
  gap: 32px;
  font-size: 16px;
}

.menu a {
  color: #666;
  text-decoration: none;
  padding: 8px 0;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
  cursor: pointer;
}

.menu a:hover, .menu a.active {
  color: #1890ff;
  border-bottom-color: #1890ff;
}

.user {
  display: flex;
  align-items: center;
  gap: 8px;
}

.dropdown-link {
  color: #333;
  text-decoration: none;
}

.content {
  max-width: 1300px;
  margin: 32px auto 0 auto;
  padding: 0 16px;
  min-height: calc(100vh - 88px);
}
</style> 