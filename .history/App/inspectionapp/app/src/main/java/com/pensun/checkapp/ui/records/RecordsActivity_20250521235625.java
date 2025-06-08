package com.pensun.checkapp.ui.records;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.pensun.checkapp.R;

public class RecordsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        TextView tv = findViewById(R.id.tv_records_tip);
        tv.setText("这里是巡检记录页面，后续可展示记录列表");
    }
} 