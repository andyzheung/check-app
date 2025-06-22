# 巡检App前后端API一致性设计文档

## 1. 登录认证接口

### 设计规范
- **接口路径**: `/api/v1/auth/login`
- **请求方法**: POST
- **认证方式**: JWT Token
- **响应格式**: JSON

### 前端实现
文件位置: `App/web/src/api/auth.js`
```javascript
export async function login(username, password) {
  try {
    const res = await request.post('/auth/login', { username, password });
    if (res.data && res.data.token) {
      const userStore = useUserStore();
      userStore.setToken(res.data.token);
      userStore.setUserInfo({
        userId: res.data.userId,
        username: res.data.username,
        realName: res.data.realName,
        role: res.data.role,
        avatar: res.data.avatar
      });
      return { success: true, data: res.data };
    } else {
      return { success: false, message: '登录失败' };
    }
  } catch (err) {
    console.error('登录错误:', err);
    return { success: false, message: err.message || '网络异常' };
  }
}
```

### 后端实现
文件位置: `check-app-server/src/main/java/com/pensun/checkapp/controller/AuthController.java`
```java
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@Valid @RequestBody UserLoginDTO loginDTO) {
        System.out.println("[AuthController] 收到登录请求: " + loginDTO.getUsername());
        UserLoginResponseDTO resp = userService.login(loginDTO);
        System.out.println("[AuthController] 登录响应: " + resp);
        return resp;
    }
}
```

### 注意事项
1. 前端路径为`/auth/login`，实际完整路径为`/api/v1/auth/login`
2. 后端需要在`SecurityConfig`和`JwtInterceptor`中配置允许该路径无需认证
3. 接口返回用户信息和JWT令牌，前端存储在store中

---

## 2. 登出接口

### 设计规范
- **接口路径**: `/api/v1/auth/logout`
- **请求方法**: POST
- **认证方式**: 需要JWT Token
- **响应格式**: JSON

### 前端实现
前端暂未实现登出API调用，可以从以下视图清除token:
- 前端实现token清除：
```javascript
// 示例(未实际实现)
function logout() {
  const userStore = useUserStore();
  userStore.clearToken();
  router.push('/login');
}
```

### 后端实现
文件位置: `check-app-server/src/main/java/com/pensun/checkapp/controller/AuthController.java`
```java
@PostMapping("/logout")
public Result<Void> logout(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);
        // 记录token到黑名单，过期时间与token一致
        Long expire = jwtTokenUtil.getExpireFromToken(token);
        if (expire != null && expire > 0) {
            // stringRedisTemplate.opsForValue().set("jwt:blacklist:" + token, "1", expire, java.util.concurrent.TimeUnit.SECONDS);
            log.info("用户退出，token已加入黑名单: {}，过期时间:{}秒", token, expire);
        }
    } else {
        log.warn("退出请求未携带有效token");
    }
    return Result.success();
}
```

### 需改进
1. 前端需要添加登出API调用，调用后端接口使token失效
2. 后端缺少Redis配置，目前仅记录日志但未真正实现token黑名单

---

## 3. 巡检记录接口

### 设计规范
- **接口路径**: `/api/v1/records`
- **请求方法**: GET/POST/PUT/DELETE
- **认证方式**: 需要JWT Token
- **响应格式**: JSON

### 前端实现
文件位置: `App/web/src/api/record.js`和`App/web/src/api/inspection.js`
```javascript
// record.js
export function createRecord(data) {
  return request.post('/records', data)
}

export function listRecords(params) {
  return request.get('/records', { params })
}

// inspection.js
export function getRecordList(params) {
  return request.get('/records', { params })
}

export function getRecordDetail(id) {
  return request.get(`/records/${id}`)
}

export function updateRecord(id, data) {
  return request({
    url: `/records/${id}`,
    method: 'put',
    data
  });
}

export function deleteRecord(id) {
  return request({
    url: `/records/${id}`,
    method: 'delete'
  });
}
```

### 后端实现
文件位置: `check-app-server/src/main/java/com/pensun/checkapp/controller/RecordController.java`
```java
@RestController
@RequestMapping("/api/v1/records")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private InspectionRecordService inspectionRecordService;

    @GetMapping
    public Result list(/* 参数列表 */) {
        // 获取记录列表逻辑
    }

    @GetMapping("/{id}")
    public Result<InspectionRecordDetailDTO> getById(@PathVariable Long id) {
        // 获取记录详情逻辑
    }

    @PostMapping
    public Result create(@Valid @RequestBody RecordDTO recordDTO) {
        // 创建记录逻辑
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody RecordDTO recordDTO) {
        // 更新记录逻辑
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        // 删除记录逻辑
    }
}
```

### 一致性说明
1. 前端存在功能重复：`record.js`和`inspection.js`中均有记录相关API
2. 命名不一致：前端使用`getRecordList`和`listRecords`两个命名，后端使用`list`
3. 参数格式一致：两者都使用`params`对象传递查询参数

---

## 4. 用户管理接口

### 设计规范
- **接口路径**: `/api/v1/users`
- **请求方法**: GET/POST/PUT/DELETE
- **认证方式**: 需要JWT Token
- **响应格式**: JSON

### 前端实现
前端未直接实现用户相关API调用，仅在登录流程中使用

### 后端实现
文件位置: `check-app-server/src/main/java/com/pensun/checkapp/controller/UserController.java`
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginDTO loginDTO) {
        // 登录逻辑
    }
    
    @GetMapping("/current")
    public Result<UserDTO> getCurrentUser() {
        // 获取当前用户信息
    }
    
    @GetMapping("/page")
    public Result<IPage<UserDTO>> getUserPage(UserQueryDTO queryDTO) {
        // 分页查询用户
    }
    
    @PostMapping
    public Result<Long> createUser(@RequestBody UserCreateDTO createDTO) {
        // 创建用户
    }
    
    @PutMapping
    public Result<Void> updateUser(@RequestBody UserCreateDTO createDTO) {
        // 更新用户
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        // 删除用户
    }
}
```

### 需改进
1. 前端缺少用户管理相关API调用实现
2. 存在重复登录接口：`/api/v1/auth/login`和`/api/v1/users/login`
3. 需要统一身份验证入口

---

## 5. 区域和巡检点接口

### 设计规范
- **接口路径**: `/api/v1/areas`和`/api/v1/points`
- **请求方法**: GET/POST/PUT/DELETE
- **认证方式**: 需要JWT Token
- **响应格式**: JSON

### 前端实现
文件位置: `App/web/src/api/inspection.js`
```javascript
export function getAllAreas() {
  return request.get('/areas')
}

export function getAreaByCode(code) {
  return request.get(`/areas/code/${code}`)
}

export function getTemplateByAreaId(areaId) {
  return request.get(`/areas/${areaId}/templates`)
}

export function getInspectionPoint(code) {
  return request.get(`/points/${code}`)
}

export function generateQRCode(areaId) {
  return request.get(`/areas/${areaId}/qrcode`)
}

export function verifyQRCode(qrData) {
  return request.post('/areas/verify', qrData)
}
```

### 后端实现
后端有对应的控制器实现，但未详细查看其内容

### 一致性检查
需要检查后端是否完全实现了前端所调用的API路径，特别是：
1. `/api/v1/areas/code/{code}`
2. `/api/v1/areas/{areaId}/templates`
3. `/api/v1/areas/{areaId}/qrcode`
4. `/api/v1/areas/verify`
5. `/api/v1/points/{code}`

---

## 6. 配置信息

### 前端配置
文件位置: `App/web/src/utils/request.js`
```javascript
const request = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
    'Accept': 'application/json;charset=utf-8'
  }
});
```

### 后端配置
文件位置: `check-app-server/src/main/resources/application.yml`
```yaml
server:
  port: 8080
  servlet:
    encoding:
      charset: UTF-8
      force: true
      enabled: true
```

### 代理配置
文件位置: `App/web/vite.config.js`
```javascript
server: {
  port: 5173,
  open: true,
  host: true,
  cors: true,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      secure: false,
      ws: true
    }
  }
}
```

---

## 7. 存在的问题及改进建议

1. **登录接口重复**：
   - 存在`/api/v1/auth/login`和`/api/v1/users/login`两个登录接口
   - 建议统一为一个入口

2. **前端API组织混乱**：
   - 记录相关API分散在`record.js`和`inspection.js`中
   - 建议按功能模块整合API调用

3. **命名不一致**：
   - 前端使用`getRecordList`和`listRecords`等不同命名
   - 建议统一命名规范

4. **缺少完整前端接口**：
   - 用户管理等功能前端未实现完整API调用
   - 建议补充完整前端API实现

5. **Redis配置未激活**：
   - 登出功能的token黑名单依赖Redis但未启用
   - 建议配置Redis或使用其他方式实现token失效

6. **接口文档与实际不符**：
   - 设计文档中接口路径与实际实现不一致
   - 建议更新设计文档或调整实现 