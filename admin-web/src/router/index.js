import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../views/layout/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/Dashboard.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'records',
        name: 'Records',
        redirect: '/records/list',
        meta: { title: '巡检记录' }
      },
      {
        path: 'records/list',
        name: 'RecordsList',
        component: () => import('../views/records/Records.vue'),
        meta: { title: '巡检记录列表' }
      },
      {
        path: 'records/export',
        name: 'RecordsExport',
        component: () => import('../views/records/RecordsExport.vue'),
        meta: { title: '导出记录' }
      },
      {
        path: 'issues',
        name: 'Issues',
        redirect: '/issues/list',
        meta: { title: '问题列表' }
      },
      {
        path: 'issues/list',
        name: 'IssuesList',
        component: () => import('../views/issues/Issues.vue'),
        meta: { title: '问题列表' }
      },
      {
        path: 'issues/statistics',
        name: 'IssuesStatistics',
        component: () => import('../views/issues/IssuesStatistics.vue'),
        meta: { title: '问题统计' }
      },
      {
        path: 'users',
        name: 'Users',
        redirect: '/users/list',
        meta: { title: '用户管理' }
      },
      {
        path: 'users/list',
        name: 'UsersList',
        component: () => import('../views/users/Users.vue'),
        meta: { title: '用户列表' }
      },
      {
        path: 'users/permissions',
        name: 'UserPermissions',
        component: () => import('../views/users/UserPermission.vue'),
        meta: { title: '用户权限管理' }
      },
      {
        path: 'areas',
        name: 'Areas',
        redirect: '/areas/config',
        meta: { title: '区域管理' }
      },
      {
        path: 'areas/config',
        name: 'AreaConfig',
        component: () => import('../views/areas/AreaConfig.vue'),
        meta: { title: '区域配置' }
      },
      {
        path: 'schedule',
        name: 'Schedule',
        redirect: '/schedule/tasks',
        meta: { title: '巡检排班' }
      },
      {
        path: 'schedule/tasks',
        name: 'TaskSchedule',
        component: () => import('../views/schedule/TaskSchedule.vue'),
        meta: { title: '任务排班' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/error/NotFound.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router