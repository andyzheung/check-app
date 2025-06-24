package com.pensun.inspection_app.scan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import androidx.activity.result.ActivityResultLauncher;

/**
 * 二维码扫描Activity - 最小化实现
 * 复用ZXing的CaptureActivity，减少代码复杂度
 */
public class ScanActivity extends AppCompatActivity {
    private static final String TAG = "ScanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "启动扫码功能");
        
        // 使用ZXing内置的扫码Activity，最小化实现
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE); // 只扫描二维码
        options.setPrompt("请将二维码放入框内"); // 提示文字
        options.setCameraId(0); // 使用后置摄像头
        options.setBeepEnabled(true); // 扫码成功后蜂鸣提示
        options.setBarcodeImageEnabled(false); // 不保存图片
        options.setOrientationLocked(true); // 锁定竖屏
        options.setCaptureActivity(CaptureActivity.class); // 使用默认扫码界面
        
        // 启动扫码
        ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    handleScanResult(result.getContents());
                } else {
                    // 用户取消扫码
                    Toast.makeText(this, "扫码已取消", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        );
        
        barcodeLauncher.launch(options);
    }

    /**
     * 处理扫码结果
     */
    private void handleScanResult(String scanResult) {
        Log.d(TAG, "扫码成功: " + scanResult);
        
        // 验证扫码结果格式
        if (isValidQRCode(scanResult)) {
            Toast.makeText(this, "扫码成功", Toast.LENGTH_SHORT).show();
            
            // 返回扫码结果给MainActivity
            Intent intent = new Intent();
            intent.putExtra("scan_result", scanResult);
            setResult(RESULT_OK, intent);
        } else {
            // 对于测试，也接受简单的文本格式
            Log.w(TAG, "二维码格式不标准，但仍返回结果: " + scanResult);
            Toast.makeText(this, "扫码完成", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent();
            intent.putExtra("scan_result", scanResult);
            setResult(RESULT_OK, intent);
        }
        
        finish();
    }

    /**
     * 验证二维码格式
     */
    private boolean isValidQRCode(String qrData) {
        try {
            // 验证是否为JSON格式的区域二维码
            // 期望格式：{"areaCode":"AREAB001","timestamp":1703123456789,"signature":"abc123..."}
            return qrData != null && 
                   qrData.trim().startsWith("{") && 
                   qrData.trim().endsWith("}") &&
                   qrData.contains("areaCode") && 
                   qrData.contains("timestamp") && 
                   qrData.contains("signature");
        } catch (Exception e) {
            Log.e(TAG, "二维码格式验证失败", e);
            return false;
        }
    }
} 