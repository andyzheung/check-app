import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/scan',
    name: 'Scan',
    component: () => import('@/views/Scan.vue')
  },
  {
    path: '/inspection-form',
    name: 'InspectionForm',
    component: () => import('@/views/InspectionForm.vue')
  },
  {
    path: '/records',
    name: 'Records',
    component: () => import('@/views/Records.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue')
  },
  {
    path: '/record-edit',
    name: 'RecordEdit',
    component: () => import('@/views/RecordEdit.vue')
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

const whiteList = ['/login'];

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (!whiteList.includes(to.path) && !token) {
    next('/login');
  } else if (to.path === '/login' && token) {
    next('/home');
  } else {
    next();
  }
});

export default router; 