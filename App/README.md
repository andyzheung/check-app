# App目录说明

本目录为巡检App Hybrid应用源码，包含原生Android端（Java）和前端Vue3项目。

## 目录结构

```
App/
├── android/                # 原生Android工程（Java）
│   ├── app/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/com/yourcompany/inspectionapp/
│   │   │   │   │   ├── MainActivity.java         # 启动入口，加载WebView
│   │   │   │   │   ├── JSBridge.java            # JS与Java交互桥
│   │   │   │   │   └── ...                      # 其他与设备交互的Java类
│   │   │   │   └── res/
│   │   │   │       └── layout/
│   │   │   │           └── activity_main.xml
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   └── ...
├── web/                    # 前端Vue3项目
│   ├── public/
│   ├── src/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── views/
│   │   │   ├── Login.vue
│   │   │   ├── Home.vue
│   │   │   ├── Scan.vue
│   │   │   ├── Records.vue
│   │   │   ├── Profile.vue
│   │   │   └── ...
│   │   ├── App.vue
│   │   ├── main.js
│   │   └── router.js
│   ├── vite.config.js
│   └── package.json
└── README.md
```

- Android端负责原生能力（扫码、设备信息等），通过JSBridge与前端通信。
- 前端Vue3负责全部UI和业务逻辑，打包后嵌入WebView。 