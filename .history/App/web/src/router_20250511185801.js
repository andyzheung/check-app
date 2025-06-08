import { createRouter, createWebHashHistory } from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import Scan from './views/Scan.vue'
import Records from './views/Records.vue'
import Profile from './views/Profile.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/home', component: Home },
  { path: '/scan', component: Scan },
  { path: '/records', component: Records },
  { path: '/profile', component: Profile }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router 