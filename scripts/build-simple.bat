@echo off
chcp 65001
echo ========================================
echo        巡检App 简单构建脚本
echo ========================================
echo.

REM 1. 构建前端
echo [1/3] 构建Vue.js前端...
cd /d "%~dp0..\App\web"
call npm run build
if %errorlevel% neq 0 (
    echo ❌ 前端构建失败
    pause
    exit /b 1
)
echo ✅ 前端构建完成
echo.

REM 2. 构建Android APK
echo [2/3] 构建Android APK...
cd /d "%~dp0..\App\inspectionapp"
call gradlew assembleDebug
if %errorlevel% neq 0 (
    echo ❌ APK构建失败
    pause
    exit /b 1
)
echo ✅ APK构建完成
echo.

REM 3. 显示结果
echo [3/3] 构建完成
set "APK_PATH=%~dp0..\App\inspectionapp\app\build\outputs\apk\debug\app-debug.apk"
if exist "!APK_PATH!" (
    echo ✅ APK文件位置: !APK_PATH!
    
    REM 计算文件大小
    for %%A in ("!APK_PATH!") do set "APK_SIZE=%%~zA"
    set /a "APK_SIZE_MB=!APK_SIZE! / 1024 / 1024"
    echo ✅ APK文件大小: !APK_SIZE_MB! MB
    
    echo.
    echo 是否安装到连接的设备？
    set /p "INSTALL_CHOICE=(y/n): "
    if /i "!INSTALL_CHOICE!"=="y" (
        adb install -r "!APK_PATH!"
        if !errorlevel! equ 0 (
            echo ✅ APK安装成功
        ) else (
            echo ⚠️ APK安装失败（检查设备连接）
        )
    )
) else (
    echo ❌ APK文件未找到
    pause
    exit /b 1
)

echo.
echo ========================================
echo           构建流程完成！
echo ========================================
pause