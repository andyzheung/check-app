# 巡检App登录认证流程设计

## 1. 登录流程概述

巡检App使用基于JWT (JSON Web Token) 的认证机制，实现前后端分离的登录流程。登录成功后，服务器生成一个Token返回给客户端，客户端在后续请求中携带该Token进行身份验证。

## 2. 前端登录流程

### 2.1 登录页面实现

前端登录页面位于 `App/web/src/views/Login.vue`，主要功能包括：
- 用户名/密码输入
- 记住密码功能
- 登录请求发送
- 错误信息展示

关键代码：
```javascript
// Login.vue
async function doLogin() {
  errorMsg.value = ''
  const res = await login(username.value, password.value)
  if (res.success) {
    if (remember.value) {
      localStorage.setItem(
        'loginInfo',
        JSON.stringify({ username: username.value, password: password.value })
      )
    } else {
      localStorage.removeItem('loginInfo')
    }
    router.push('/scan')
  } else {
    errorMsg.value = res.message
  }
}
```

### 2.2 API调用

登录API调用封装在 `App/web/src/api/auth.js` 中：
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

### 2.3 状态管理

用户Token和用户信息通过Pinia状态库管理，实现状态持久化。

```javascript
// stores/user.js (推测实现)
export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}')
  }),
  actions: {
    setToken(token) {
      this.token = token;
      localStorage.setItem('token', token);
    },
    setUserInfo(userInfo) {
      this.userInfo = userInfo;
      localStorage.setItem('userInfo', JSON.stringify(userInfo));
    },
    clearToken() {
      this.token = '';
      this.userInfo = {};
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
    }
  }
});
```

### 2.4 请求拦截器

Axios请求拦截器在每次API请求中自动添加Token，实现在 `App/web/src/utils/request.js` 中：

```javascript
// 请求拦截器
request.interceptors.request.use(
  config => {
    const userStore = useUserStore();
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`;
    }
    return config;
  },
  error => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);
```

## 3. 后端登录认证实现

### 3.1 登录控制器

后端登录接口实现在 `check-app-server/src/main/java/com/pensun/checkapp/controller/AuthController.java` 中：

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

### 3.2 JWT令牌生成

JWT令牌生成与解析在 `JwtTokenUtil` 类中实现，主要功能包括：
- 生成Token
- 验证Token有效性
- 从Token中获取用户信息
- 计算Token过期时间

### 3.3 安全配置

安全配置在 `check-app-server/src/main/java/com/pensun/checkapp/config/SecurityConfig.java` 中实现：

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/v1/login", "/api/v1/auth/login").permitAll()
                .anyRequest().authenticated()
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
            .and()
            .sessionManagement().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable()
            .addFilterBefore(jwtInterceptor, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

### 3.4 JWT拦截器

JWT拦截器在 `check-app-server/src/main/java/com/pensun/checkapp/interceptor/JwtInterceptor.java` 中实现，主要功能包括：
- 拦截HTTP请求
- 排除登录等公开接口
- 验证Token有效性
- 解析用户信息
- 设置安全上下文

```java
@Component
public class JwtInterceptor extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.endsWith("/api/v1/login") ||
            path.endsWith("/api/v1/register") ||
            path.endsWith("/api/v1/captcha") ||
            path.endsWith("/api/v1/users/login") ||
            path.endsWith("/api/v1/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Token验证逻辑
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            boolean valid = jwtTokenUtil.validateToken(token);
            if (valid) {
                // 设置安全上下文
                // ...
                filterChain.doFilter(request, response);
                return;
            }
        }
        
        // 验证失败
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"Unauthorized or invalid token\"}");
    }
}
```

## 4. 登出流程

### 4.1 前端登出

前端登出主要是清除本地存储的Token和用户信息，然后跳转到登录页面。

### 4.2 后端登出

后端登出接口实现在 `AuthController` 中：

```java
@PostMapping("/logout")
public Result<Void> logout(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);
        // 记录token到黑名单，过期时间与token一致
        Long expire = jwtTokenUtil.getExpireFromToken(token);
        if (expire != null && expire > 0) {
            // Redis实现暂未启用
            // stringRedisTemplate.opsForValue().set("jwt:blacklist:" + token, "1", expire, TimeUnit.SECONDS);
            log.info("用户退出，token已加入黑名单: {}，过期时间:{}秒", token, expire);
        }
    }
    return Result.success();
}
```

## 5. 认证流程改进建议

### 5.1 问题分析

当前登录认证实现存在以下问题：

1. **多个登录入口**：存在 `/api/v1/auth/login` 和 `/api/v1/users/login` 两个登录接口
2. **Token黑名单未实现**：登出时需要将Token加入黑名单，但Redis实现被注释
3. **错误处理不统一**：前端错误处理不完善，无法区分不同类型的认证错误
4. **安全加固不足**：缺少防暴力破解、限流等安全措施

### 5.2 改进方案

1. **统一登录入口**：
   - 保留 `/api/v1/auth/login` 作为唯一登录入口
   - 在 `UserController` 中移除重复的登录接口

2. **实现Token黑名单**：
   - 启用Redis配置
   - 实现登出时将Token加入黑名单
   - 请求拦截时检查Token是否在黑名单中

3. **完善错误处理**：
   - 统一错误代码和错误消息
   - 前端根据错误类型展示不同提示

4. **增强安全措施**：
   - 登录失败次数限制
   - IP限流
   - 敏感操作二次验证 