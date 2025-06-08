package com.pensun.inspection_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private CheckBox cbRemember;
    private TextView tvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        cbRemember = findViewById(R.id.cb_remember);
        tvTip = findViewById(R.id.tv_tip);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "开始请求后端...", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8080/api/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                JSONObject json = new JSONObject();
                json.put("username", username);
                json.put("password", password);
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.close();

                int code = conn.getResponseCode();
                runOnUiThread(() -> Toast.makeText(this, "HTTP响应码: " + code, Toast.LENGTH_SHORT).show());
                InputStream is = (code == 200) ? conn.getInputStream() : conn.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                JSONObject resp = new JSONObject(sb.toString());
                if (resp.has("token")) {
                    SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                    sp.edit().putString("token", resp.getString("token")).apply();
                    runOnUiThread(() -> {
                        Toast.makeText(this, "登录成功，token=" + resp.optString("token"), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "登录失败：" + resp.optString("error", "未知错误"), Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "网络异常: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
} 