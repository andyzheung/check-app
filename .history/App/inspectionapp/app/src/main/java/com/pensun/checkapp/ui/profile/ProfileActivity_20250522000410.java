package com.pensun.checkapp.ui.profile;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.pensun.checkapp.R;
import com.pensun.checkapp.model.User;
import com.pensun.checkapp.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {
    private ProfileViewModel profileViewModel;
    private TextView tvProfileTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvProfileTip = findViewById(R.id.tv_profile_tip);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getUser().observe(this, user -> {
            if (user == null) {
                tvProfileTip.setText("未登录");
            } else {
                tvProfileTip.setText("用户名：" + user.getUsername() + "\n姓名：" + user.getRealName() + "\n角色：" + user.getRole());
            }
        });
        // TODO: 实际项目中应从本地或网络获取用户信息
        User demo = new User();
        demo.setUsername("admin");
        demo.setRealName("管理员");
        demo.setRole("超级管理员");
        profileViewModel.setUser(demo);
    }
} 