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
import Tasks from './views/Tasks.vue'
import Areas from './views/Areas.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      { path: 'home', component: Home, meta: { title: '首页' } },
      { path: 'scan', component: Scan, meta: { title: '扫码巡检' } },
      { path: 'records', component: Records, meta: { title: '巡检记录', subtitle: '(管理员可查看所有记录)' } },
      { path: 'profile', component: Profile, meta: { title: '个人中心' } },
      { path: 'record-edit', component: RecordEdit, meta: { title: '编辑记录' } },
      { path: 'record-detail', component: RecordDetail, meta: { title: '记录详情' } },
      { path: 'tasks', component: Tasks, meta: { title: '任务列表' } },
      { path: 'areas', component: Areas, meta: { title: '巡检区域' } },
      { path: 'inspection-form', component: InspectionForm, meta: { title: '巡检表单' } },
    ]
  },
  { path: '/login', component: Login, meta: { showBottomNav: false, showHeader: false } },
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
    // 避免无限重定向，只在不是从登录页跳转时才添加redirect参数
    if (to.path !== '/login') {
      next({ path: '/login', query: { redirect: to.fullPath } })
    } else {
      next('/login')
    }
  } else if (to.path === '/login' && token) {
    // 如果已登录且访问登录页，重定向到首页
    next('/home')
  } else {
    next()
  }
})

export default router 