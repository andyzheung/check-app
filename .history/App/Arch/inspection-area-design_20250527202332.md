# 巡检区域扫码功能设计文档

## 9. 二维码生成方案

### 9.1 二维码内容设计
```json
{
  "areaCode": "AREAA001",     // 区域编码
  "timestamp": 1647123456789, // 生成时间戳
  "signature": "xxxxx"        // 安全签名
}
```

### 9.2 二维码生成流程
1. 数据准备：
   - 组装区域信息JSON
   - 添加时间戳
   - 计算安全签名（使用SHA-256）

2. 二维码生成：
   - 使用 QRCode.js 前端生成
   - 使用 ZXing 后端生成
   - 支持自定义样式（Logo、颜色等）

3. 二维码规格：
   - 尺寸：建议 300x300 像素
   - 容错级别：H级（最高）
   - 边距：留白 4 个单位
   - 输出格式：PNG（推荐）或 SVG

### 9.3 安全机制
1. 签名生成：
```typescript
function generateSignature(areaCode: string, timestamp: number, secretKey: string): string {
  const data = `${areaCode}:${timestamp}:${secretKey}`;
  return crypto.createHash('sha256').update(data).digest('hex');
}
```

2. 签名验证：
```typescript
function verifySignature(qrData: QRData, secretKey: string): boolean {
  const { areaCode, timestamp, signature } = qrData;
  const expectedSignature = generateSignature(areaCode, timestamp, secretKey);
  return signature === expectedSignature;
}
```

### 9.4 二维码样式示例
```javascript
// 前端生成示例（使用 QRCode.js）
const qrcode = new QRCode(document.getElementById("qrcode"), {
  text: JSON.stringify(qrData),
  width: 300,
  height: 300,
  colorDark: "#000000",
  colorLight: "#ffffff",
  correctLevel: QRCode.CorrectLevel.H,
  logo: "/path/to/logo.png",    // 可选：添加Logo
  logoWidth: 60,                // Logo宽度
  logoHeight: 60,               // Logo高度
  logoBackgroundColor: '#ffffff', // Logo背景色
  logoBackgroundTransparent: false // Logo背景透明
});
```

### 9.5 二维码使用建议
1. 打印建议：
   - 使用防水材质
   - 建议尺寸 5x5 cm
   - 避免反光材质
   - 四周留白

2. 张贴位置：
   - 避免强光直射
   - 避免可能被遮挡的位置
   - 建议高度 1.2-1.5m
   - 确保手机易于对准

3. 定期维护：
   - 定期检查二维码完整性
   - 及时更换损坏的二维码
   - 保持二维码清晰可见
   - 记录更换日志

### 9.6 错误处理
1. 生成失败处理：
   - 记录错误日志
   - 重试机制
   - 通知管理员

2. 扫描失败处理：
   - 提供手动输入备选方案
   - 扫描失败原因分析
   - 用户提示优化

3. 验证失败处理：
   - 签名验证失败提示
   - 过期二维码处理
   - 非法二维码拦截 