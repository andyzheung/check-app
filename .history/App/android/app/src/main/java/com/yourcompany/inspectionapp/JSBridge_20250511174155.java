package com.yourcompany.inspectionapp;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class JSBridge {
    private Context context;
    private WebView webView;

    public JSBridge(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    // 示例：原生弹窗
    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // 示例：扫码功能（实际需集成扫码库并回调结果）
    @JavascriptInterface
    public void scanQRCode() {
        // TODO: 调用原生扫码，扫码完成后通过webView.evaluateJavascript回传结果
        // webView.evaluateJavascript("window.onScanResult('二维码内容')", null);
    }

    // 示例：获取设备信息
    @JavascriptInterface
    public String getDeviceInfo() {
        // TODO: 返回设备信息JSON字符串
        return "{\"model\":\"Android\",\"version\":\"10\"}";
    }
} 