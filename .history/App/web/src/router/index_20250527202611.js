import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue')
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
        path: 'scan',
        name: 'ScanArea',
        component: () => import('@/views/inspection/ScanArea.vue'),
        meta: {
          title: '扫码巡检',
          requiresAuth: true
        }
      },
      {
        path: 'task/:areaId',
        name: 'InspectionTask',
        component: () => import('@/views/inspection/Task.vue'),
        meta: {
          title: '巡检任务',
          requiresAuth: true
        }
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router; 