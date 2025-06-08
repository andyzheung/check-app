import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/inspection',
    name: 'Inspection',
    component: () => import('@/views/layout/index.vue'),
    children: [
      {
        path: 'records',
        name: 'InspectionRecords',
        component: () => import('@/views/inspection/RecordList.vue'),
        meta: {
          title: '巡检记录',
          requiresAuth: true
        }
      },
      {
        path: 'record/:id',
        name: 'InspectionRecordDetail',
        component: () => import('@/views/inspection/RecordDetail.vue'),
        meta: {
          title: '记录详情',
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home.vue')
      },
      {
        path: 'records',
        name: 'Records',
        component: () => import('@/views/Records.vue')
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Settings.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 初始化 token
  userStore.initToken()

  if (to.meta.requiresAuth && !userStore.token) {
    next('/login')
  } else if (to.path === '/login' && userStore.token) {
    next('/')
  } else {
    next()
  }
})

export default router 