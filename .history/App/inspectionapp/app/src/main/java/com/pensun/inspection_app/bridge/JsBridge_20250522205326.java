package com.pensun.inspection_app.bridge;

import android.webkit.JavascriptInterface;
import android.app.Activity;
import com.pensun.inspection_app.MainActivity;
import android.content.SharedPreferences;
import android.content.Context;

public class JsBridge {
    private Activity activity;
    public JsBridge(Activity activity) {
        this.activity = activity;
    }

    // H5调用：window.android.startScan()
    @JavascriptInterface
    public void startScan() {
        activity.runOnUiThread(() -> {
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).startScanActivity();
            }
        });
    }

    // H5调用：window.android.getToken()
    @JavascriptInterface
    public String getToken() {
        SharedPreferences sp = activity.getSharedPreferences("app", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    // 可扩展更多原生能力
} 