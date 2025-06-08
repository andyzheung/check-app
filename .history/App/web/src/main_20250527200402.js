import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import 'vant/lib/index.css'
import '@/assets/css/common.css'
import {
  Button,
  Form,
  Field,
  CellGroup,
  Cell,
  List,
  PullRefresh,
  Tag,
  Tabbar,
  TabbarItem,
  Pagination,
  Picker,
  Popup,
  DatePicker,
  Toast
} from 'vant'

const app = createApp(App)
const pinia = createPinia()

// 注册 Vant 组件
const vantComponents = [
  Button,
  Form,
  Field,
  CellGroup,
  Cell,
  List,
  PullRefresh,
  Tag,
  Tabbar,
  TabbarItem,
  Pagination,
  Picker,
  Popup,
  DatePicker
]

vantComponents.forEach(component => {
  app.use(component)
})

app.use(pinia)
app.use(router)
app.mount('#app') 