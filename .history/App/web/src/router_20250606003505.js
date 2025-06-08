import { createRouter, createWebHistory } from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import Scan from './views/Scan.vue'
import Records from './views/Records.vue'
import Profile from './views/Profile.vue'
import RecordEdit from './views/RecordEdit.vue'
import InspectionForm from './views/InspectionForm.vue'
import RecordDetail from './views/RecordDetail.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/scan', component: Scan },
  { path: '/inspection-form', component: InspectionForm },
  { path: '/records', component: Records },
  { path: '/profile', component: Profile },
  { path: '/home', component: Home },
  { path: '/record-edit', component: RecordEdit },
  { path: '/record-detail', component: RecordDetail }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const whiteList = ['/login', '/scan', '/inspection-form']
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const needLogin = !whiteList.includes(to.path)
  if (needLogin && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/home')
  } else {
    next()
  }
})

export default router 