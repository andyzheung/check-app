import { createRouter, createWebHistory } from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import Scan from './views/Scan.vue'
import Records from './views/Records.vue'
import Profile from './views/Profile.vue'
import RecordEdit from './views/RecordEdit.vue'
import InspectionForm from './views/InspectionForm.vue'

const routes = [
  { path: '/', redirect: '/scan' },
  { path: '/login', component: Login },
  { path: '/home', component: Home },
  { path: '/scan', component: Scan },
  { path: '/records', component: Records },
  { path: '/profile', component: Profile },
  { path: '/record-edit', component: RecordEdit },
  { path: '/inspection-form', component: InspectionForm }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/home')
  } else {
    next()
  }
})

export default router 