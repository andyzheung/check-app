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
        name: 'Dashboard',
        component: () => import('../views/dashboard/Dashboard.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'records',
        name: 'Records',
        component: () => import('../views/records/Records.vue'),
        meta: { title: '巡检记录' }
      },
      {
        path: 'issues',
        name: 'Issues',
        component: () => import('../views/issues/Issues.vue'),
        meta: { title: '问题列表' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('../views/users/Users.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'areas',
        name: 'Areas',
        component: () => import('../views/areas/Areas.vue'),
        meta: { title: '巡检区域' }
      },
      {
        path: 'tasks',
        name: 'Tasks',
        component: () => import('../views/tasks/Tasks.vue'),
        meta: { title: '任务管理' }
      },
      {
        path: 'forms',
        name: 'Forms',
        component: () => import('../views/forms/Forms.vue'),
        meta: { title: '表单管理' }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('../views/statistics/Statistics.vue'),
        meta: { title: '统计分析' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('../views/settings/Settings.vue'),
        meta: { title: '系统设置' }
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