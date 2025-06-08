package com.pensun.checkapp.ui.profile;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.pensun.checkapp.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView tv = findViewById(R.id.tv_profile_tip);
        tv.setText("这里是个人中心页面，后续可展示用户信息");
    }
} 