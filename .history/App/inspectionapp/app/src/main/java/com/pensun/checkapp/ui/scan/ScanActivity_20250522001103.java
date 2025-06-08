package com.pensun.checkapp.ui.scan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pensun.checkapp.R;
import com.pensun.checkapp.viewmodel.RecordsViewModel;

public class ScanActivity extends AppCompatActivity {
    private RecordsViewModel recordsViewModel;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        TextView tv = findViewById(R.id.tv_scan_tip);
        Button btn = new Button(this);
        btn.setText("开始扫码");
        ((android.widget.LinearLayout) findViewById(android.R.id.content)).addView(btn);
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        token = sp.getString("token", "");
        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        btn.setOnClickListener(v -> startScan());
    }
    private void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("请扫描二维码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String code = result.getContents();
            recordsViewModel.uploadScanResult(token, code).observe(this, success -> {
                if (Boolean.TRUE.equals(success)) {
                    Toast.makeText(this, "扫码上传成功: " + code, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "扫码上传失败", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "扫码取消", Toast.LENGTH_SHORT).show();
        }
    }
} 