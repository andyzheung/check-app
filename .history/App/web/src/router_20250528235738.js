import { createRouter, createWebHistory } from 'vue-router'
import Layout from './views/Layout.vue'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import Scan from './views/Scan.vue'
import Records from './views/Records.vue'
import Profile from './views/Profile.vue'
import RecordEdit from './views/RecordEdit.vue'
import InspectionForm from './views/InspectionForm.vue'
import RecordDetail from './views/RecordDetail.vue'
import { useUserStore } from './stores/user'

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
        redirect: '/home'
      },
      {
        path: 'home',
        name: 'Home',
        component: Home
      },
      {
        path: 'scan',
        name: 'Scan',
        component: Scan
      },
      {
        path: 'inspection-form',
        name: 'InspectionForm',
        component: InspectionForm
      },
      {
        path: 'records',
        name: 'Records',
        component: Records
      },
      {
        path: 'record-detail',
        name: 'RecordDetail',
        component: RecordDetail
      },
      {
        path: 'record-edit',
        name: 'RecordEdit',
        component: RecordEdit
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

const whiteList = ['/login']

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userStore = useUserStore()
  
  if (whiteList.includes(to.path)) {
    if (token && to.path === '/login') {
      next('/home')
    } else {
      next()
    }
  } else {
    if (!token) {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    } else {
      next()
    }
  }
})

export default router 