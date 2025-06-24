# 巡检App构建脚本使用说明

## 🚀 快速开始

### Windows环境

#### 1. 简单构建（推荐）
```batch
scripts\build-simple.bat
```
- 自动构建前端 + Android APK
- 可选择安装到设备
- 适合日常开发使用

#### 2. 快速测试
```batch
scripts\quick-test.bat  
```
- 智能检测前端资源
- 自动构建并安装APK
- 适合快速验证功能

#### 3. 版本同步
```bash
node scripts\version-sync.js
```
- 同步package.json版本到Android
- 自动生成versionCode
- 确保版本一致性

## 📋 前置要求

### 必需工具
- **Node.js 18+**: 前端构建
- **JDK 8/11**: Android编译
- **Android SDK**: Android平台工具

### 验证环境
```bash
node --version    # 应显示v18.x.x或更高
java -version     # 应显示1.8.x或11.x.x
```

### 可选工具
- **ADB**: 自动安装APK到设备
- **Android Studio**: 开发调试

## 🔧 构建流程

### 完整构建流程
```
1. Vue.js前端构建 (npm run build)
   ↓
2. 资源复制到Android assets
   ↓  
3. Android APK构建 (gradle assembleDebug)
   ↓
4. APK输出 (app-debug.apk)
```

### 输出文件位置
```
App/inspectionapp/app/build/outputs/apk/
├── debug/
│   └── app-debug.apk      # Debug版本
└── release/
    └── app-release.apk    # Release版本
```

## 📱 APK安装

### 自动安装（推荐）
脚本会自动检测连接的设备并安装APK

### 手动安装
```bash
# 通过ADB安装
adb install -r app-debug.apk

# 或直接拷贝APK文件到设备安装
```

### 设备要求
- Android 8.0+ (API 26+)
- 开启USB调试模式
- 允许未知来源安装

## 🐛 常见问题

### 1. 前端构建失败
```bash
# 清理依赖重新安装
cd App/web
rm -rf node_modules package-lock.json
npm install
```

### 2. Android构建失败
```bash
# 清理Android项目
cd App/inspectionapp
gradlew clean
gradlew assembleDebug
```

### 3. APK安装失败
- 检查设备连接: `adb devices`
- 检查USB调试是否开启
- 检查是否允许未知来源安装

### 4. 版本不一致
```bash
# 运行版本同步脚本
node scripts/version-sync.js
```

## 🔍 高级用法

### 构建Release版本
```batch
# 修改build-simple.bat中的gradlew命令
gradlew assembleRelease
```

### 环境变量配置
```bash
# 设置Android SDK路径（如果需要）
set ANDROID_SDK_ROOT=C:\Users\YourName\AppData\Local\Android\Sdk
```

### 自定义构建配置
编辑相应的配置文件：
- 前端配置: `App/web/vite.config.js`
- Android配置: `App/inspectionapp/app/build.gradle`

## 📈 性能优化

### 构建速度优化
- 使用 `npm ci` 代替 `npm install`
- 启用Gradle构建缓存
- 使用增量构建

### APK大小优化
- 启用代码混淆（Release版本）
- 压缩资源文件
- 移除未使用的依赖

## 🎯 最佳实践

1. **定期同步版本**: 每次发布前运行version-sync.js
2. **增量开发**: 使用quick-test.bat进行快速迭代
3. **版本管理**: 保持前端和Android版本号一致
4. **测试验证**: 构建后及时在真机上测试功能

## 🆘 获取帮助

如果遇到问题，请检查：
1. 环境要求是否满足
2. 工具版本是否兼容  
3. 网络连接是否正常
4. 设备权限是否正确配置

---

**最后更新**: 2025-06-24  
**脚本版本**: v1.0  
**兼容性**: Windows 10/11, Android 8.0+