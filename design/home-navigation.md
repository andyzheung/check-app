# 巡检App首页导航与扫码返回设计

## 1. 首页设计规范

### 1.1 首页功能需求

首页应包含以下核心功能：
1. **任务统计**：显示今日任务、已完成、待完成的数量统计
2. **待完成任务**：默认显示最近3条待完成任务，提供"查看全部"功能
3. **巡检区域**：默认显示最近3条巡检区域，提供"查看全部"功能
4. **底部导航**：提供首页、巡检、记录、我的四个主要功能入口

### 1.2 导航流程设计

- 用户登录后应默认进入首页
- 点击底部导航"首页"按钮应跳转到首页
- 扫码页面的返回按钮应跳转回首页
- 巡检完成后应返回首页

## 2. 当前实现情况

### 2.1 路由配置

文件位置：`App/web/src/router.js`

```javascript
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
```

**问题**：
- 根路径`/`重定向到`/login`而不是`/home`
- 登录后直接跳转到`/scan`而不是`/home`：
```javascript
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const needLogin = !whiteList.includes(to.path)
  if (needLogin && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/scan')  // 这里应该跳转到 /home
  } else {
    next()
  }
})
```

### 2.2 首页实现

文件位置：`App/web/src/views/Home.vue`

首页组件已实现以下功能：
- 顶部用户信息展示
- 任务统计（今日任务、已完成、待完成）
- 待完成任务列表（限制3条）
- 巡检区域列表（限制3条）
- 底部导航栏

关键代码：
```javascript
// 获取待完成任务
async function fetchPendingTasks() {
  try {
    const res = await request.get('/tasks', {
      params: {
        status: 'PENDING',
        page: 1,
        size: 3  // 限制3条
      }
    })
    // ...处理数据
  } catch (err) {
    console.error('获取待完成任务失败:', err)
  }
}

// 获取巡检区域
async function fetchAreas() {
  try {
    const res = await request.get('/areas', {
      params: {
        status: 'active',
        page: 1,
        size: 3  // 限制3条
      }
    })
    // ...处理数据
  } catch (err) {
    console.error('获取巡检区域失败:', err)
  }
}
```

### 2.3 扫码页面返回按钮

文件位置：`App/web/src/views/Scan.vue`

```javascript
function goHome() {
  router.push('/home')
}
```

```html
<button class="back-btn" @click="goHome">
  <span class="material-icons">arrow_back</span>
</button>
```

### 2.4 底部导航实现

文件位置：`App/web/src/views/Home.vue`

```html
<div class="bottom-nav">
  <router-link to="/" class="nav-item" exact-active-class="active" replace @click.native.prevent="goHome">
    <span style="font-size: 22px;">🏠</span>
    <span>首页</span>
  </router-link>
  <router-link to="/scan" class="nav-item" active-class="active">
    <span style="font-size: 22px;">📷</span>
    <span>巡检</span>
  </router-link>
  <router-link to="/records" class="nav-item" active-class="active">
    <span style="font-size: 22px;">📜</span>
    <span>记录</span>
  </router-link>
  <router-link to="/profile" class="nav-item" active-class="active">
    <span style="font-size: 22px;">👤</span>
    <span>我的</span>
  </router-link>
</div>
```

```javascript
// 添加 goHome 方法
function goHome() {
  if (router.currentRoute.value.path === '/') {
    // 如果已经在首页，刷新数据
    fetchUserInfo()
    fetchUnreadCount()
    fetchTaskStats()
    fetchPendingTasks()
    fetchAreas()
  } else {
    // 如果不在首页，跳转到首页
    router.replace('/')
  }
}
```

## 3. 存在问题与改进方案

### 3.1 问题列表

1. **路由配置问题**：
   - 根路径`/`重定向到`/login`而非`/home`
   - 登录成功后跳转到`/scan`而非`/home`

2. **扫码返回问题**：
   - 扫码页面返回按钮跳转到`/home`，而不是根路径`/`
   - 导致底部导航栏高亮状态不正确

3. **首页刷新问题**：
   - 点击首页导航项时，如果已在首页，会刷新数据，但路径是`/`而非`/home`
   - 导致路径与组件不一致

4. **任务和区域数据展示**：
   - 已正确限制显示3条数据
   - 但"查看全部"功能跳转到的页面可能不存在

### 3.2 改进方案

1. **路由配置调整**：
   ```javascript
   const routes = [
     { path: '/', component: Home },  // 根路径直接使用Home组件
     { path: '/login', component: Login },
     // ...其他路由
   ]
   
   router.beforeEach((to, from, next) => {
     const token = localStorage.getItem('token')
     const needLogin = !whiteList.includes(to.path)
     if (needLogin && !token) {
       next('/login')
     } else if (to.path === '/login' && token) {
       next('/')  // 登录成功后跳转到首页
     } else {
       next()
     }
   })
   ```

2. **扫码返回调整**：
   ```javascript
   function goHome() {
     router.push('/')  // 统一使用根路径
   }
   ```

3. **首页导航调整**：
   ```javascript
   function goHome() {
     if (router.currentRoute.value.path === '/') {
       // 如果已经在首页，刷新数据
       fetchUserInfo()
       fetchUnreadCount()
       fetchTaskStats()
       fetchPendingTasks()
       fetchAreas()
     } else {
       // 如果不在首页，跳转到首页
       router.push('/')
     }
   }
   ```

4. **完善"查看全部"功能**：
   - 实现`/tasks`和`/areas`页面
   - 或者在现有页面添加过滤功能

## 4. 实施建议

### 4.1 路由调整

1. 修改`router.js`中的根路径配置
2. 修改登录成功后的跳转目标
3. 移除冗余的`/home`路由，统一使用根路径`/`

### 4.2 组件调整

1. 修改`Scan.vue`中的返回按钮跳转目标
2. 统一所有组件中对首页的引用为根路径`/`
3. 确保底部导航栏在所有页面保持一致

### 4.3 功能完善

1. 实现"查看全部"功能对应的页面
2. 确保任务和区域数据正确加载和分页
3. 添加加载状态和错误处理 