package com.pensun.inspection_app.scan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ScanActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 集成ZXing/MLKit扫码，这里用模拟扫码结果
        Toast.makeText(this, "模拟扫码成功", Toast.LENGTH_SHORT).show();
        String result = "TEST-QR-123456";
        Intent intent = new Intent();
        intent.putExtra("scan_result", result);
        setResult(RESULT_OK, intent);
        finish();
    }
} 