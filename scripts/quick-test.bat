@echo off
chcp 65001
echo ========================================
echo        巡检App 快速测试脚本
echo ========================================
echo.

REM 检查前端资源
echo [1/3] 检查前端资源...
if not exist "%~dp0..\App\inspectionapp\app\src\main\assets\index.html" (
    echo ⚠️ 前端资源缺失，开始构建...
    cd /d "%~dp0..\App\web"
    call npm run build
    if %errorlevel% neq 0 (
        echo ❌ 前端构建失败
        pause
        exit /b 1
    )
    echo ✅ 前端资源构建完成
) else (
    echo ✅ 前端资源已存在
)
echo.

REM 构建APK
echo [2/3] 构建APK...
cd /d "%~dp0..\App\inspectionapp"
call gradlew assembleDebug
if %errorlevel% neq 0 (
    echo ❌ APK构建失败
    pause
    exit /b 1
)
echo ✅ APK构建完成
echo.

REM 自动安装到设备
echo [3/3] 安装APK到设备...
set "APK_PATH=%~dp0..\App\inspectionapp\app\build\outputs\apk\debug\app-debug.apk"
adb install -r "%APK_PATH%"
if %errorlevel% equ 0 (
    echo ✅ APK安装成功
    echo.
    echo 🎉 测试完成！您可以在设备上打开巡检App进行测试
) else (
    echo ⚠️ APK安装失败
    echo 请检查：
    echo - USB调试是否开启
    echo - 设备是否已连接
    echo - ADB是否已安装
    echo.
    echo APK文件位置: %APK_PATH%
    echo 您可以手动安装此文件
)

echo.
echo ========================================
echo           快速测试完成！
echo ========================================
pause