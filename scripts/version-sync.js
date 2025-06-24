#!/usr/bin/env node

/**
 * 版本同步脚本
 * 同步 package.json 版本到 Android build.gradle
 */

const fs = require('fs');
const path = require('path');

function syncVersion() {
    try {
        // 读取package.json版本
        const webPackagePath = path.join(__dirname, '../App/web/package.json');
        const webPackage = JSON.parse(fs.readFileSync(webPackagePath, 'utf8'));
        const version = webPackage.version;
        
        console.log(`📦 检测到前端版本: ${version}`);

        // 更新Android版本
        const gradlePath = path.join(__dirname, '../App/inspectionapp/app/build.gradle');
        let gradleContent = fs.readFileSync(gradlePath, 'utf8');

        // 更新versionName
        const versionNameRegex = /versionName\s+"[^"]*"/;
        if (versionNameRegex.test(gradleContent)) {
            gradleContent = gradleContent.replace(versionNameRegex, `versionName "${version}"`);
            console.log(`✅ 更新versionName: ${version}`);
        } else {
            console.log('⚠️ 未找到versionName配置');
        }

        // 更新versionCode (使用时间戳)
        const versionCode = Math.floor(Date.now() / 1000);
        const versionCodeRegex = /versionCode\s+\d+/;
        if (versionCodeRegex.test(gradleContent)) {
            gradleContent = gradleContent.replace(versionCodeRegex, `versionCode ${versionCode}`);
            console.log(`✅ 更新versionCode: ${versionCode}`);
        } else {
            console.log('⚠️ 未找到versionCode配置');
        }

        // 写入更新后的内容
        fs.writeFileSync(gradlePath, gradleContent);
        console.log(`🎉 版本同步完成: ${version} (${versionCode})`);
        
        return { version, versionCode };
    } catch (error) {
        console.error('❌ 版本同步失败:', error.message);
        process.exit(1);
    }
}

// 如果直接运行此脚本
if (require.main === module) {
    syncVersion();
}

module.exports = { syncVersion };