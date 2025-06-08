package com.pensun.checkapp.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.pensun.checkapp.R;
import com.pensun.checkapp.model.LoginRequest;
import com.pensun.checkapp.model.LoginResponse;
import com.pensun.checkapp.ui.MainActivity;
import com.pensun.checkapp.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox cbRemember;
    private TextView tvTip;
    private SharedPreferences sp;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        loginViewModel = new LoginViewModel();
        initViews();
        setupListeners();
        autoFill();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        cbRemember = findViewById(R.id.cb_remember);
        tvTip = findViewById(R.id.tv_tip);
    }

    private void autoFill() {
        etUsername.setText(sp.getString("username", ""));
        etPassword.setText(sp.getString("password", ""));
        cbRemember.setChecked(sp.getBoolean("remember", false));
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean remember = cbRemember.isChecked();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        loginViewModel.login(request).observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse != null && loginResponse.getCode() == 200) {
                    String token = loginResponse.getData().getToken();
                    SharedPreferences.Editor editor = sp.edit();
                    if (remember) {
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putBoolean("remember", true);
                    } else {
                        editor.clear();
                    }
                    editor.putString("token", token);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败，请检查账号密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
} 