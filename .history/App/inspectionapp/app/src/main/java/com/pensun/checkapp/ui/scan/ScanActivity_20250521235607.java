package com.pensun.checkapp.ui.scan;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.pensun.checkapp.R;

public class ScanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        TextView tv = findViewById(R.id.tv_scan_tip);
        tv.setText("这里是扫码页面，后续可集成扫码SDK");
    }
} 