package com.pensun.checkapp.ui;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    private Context context;

    public WebAppInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void handleScanResult(String result) {
        // 这里可以处理扫码结果，比如回调给Web页面
        showToast("扫码结果: " + result);
    }
} 