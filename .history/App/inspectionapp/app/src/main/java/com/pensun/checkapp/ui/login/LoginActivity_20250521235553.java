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

import com.pensun.checkapp.R;
import com.pensun.checkapp.model.LoginRequest;
import com.pensun.checkapp.model.LoginResponse;
import com.pensun.checkapp.network.RetrofitClient;
import com.pensun.checkapp.ui.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox cbRemember;
    private TextView tvTip;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("login", MODE_PRIVATE);
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
        RetrofitClient.getInstance()
                .getApiService()
                .login(request)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.getCode() == 200) {
                                // 登录成功，保存token和用户信息
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
                                // 跳转主页
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
} 