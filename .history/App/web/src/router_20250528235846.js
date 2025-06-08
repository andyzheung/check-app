import { createRouter, createWebHistory } from 'vue-router'
import Layout from './views/Layout.vue'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import Scan from './views/Scan.vue'
import Records from './views/Records.vue'
import Profile from './views/Profile.vue'
import InspectionForm from './views/InspectionForm.vue'
import RecordDetail from './views/RecordDetail.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: Home,
        meta: { requiresAuth: true }
      },
      {
        path: 'scan',
        name: 'Scan',
        component: Scan,
        meta: { requiresAuth: true }
      },
      {
        path: 'records',
        name: 'Records',
        component: Records,
        meta: { requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile,
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/inspection-form',
    name: 'InspectionForm',
    component: InspectionForm,
    meta: { requiresAuth: true }
  },
  {
    path: '/record-detail',
    name: 'RecordDetail',
    component: RecordDetail,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router 