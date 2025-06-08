import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Scan from '@/views/Scan.vue'
import Records from '@/views/Records.vue'
import RecordDetail from '@/views/RecordDetail.vue'
import InspectionForm from '@/views/InspectionForm.vue'
import Profile from '@/views/Profile.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Home',
        component: Home
      },
      {
        path: 'scan',
        name: 'Scan',
        component: Scan
      },
      {
        path: 'records',
        name: 'Records',
        component: Records
      },
      {
        path: 'records/:id',
        name: 'RecordDetail',
        component: RecordDetail
      },
      {
        path: 'inspection',
        name: 'InspectionForm',
        component: InspectionForm
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router 