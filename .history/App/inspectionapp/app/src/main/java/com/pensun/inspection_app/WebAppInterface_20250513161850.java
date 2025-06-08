package com.pensun.inspection_app;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    private Activity mActivity;
    private MainActivity mainActivity;

    public WebAppInterface(Activity activity) {
        mActivity = activity;
        if (activity instanceof MainActivity) {
            mainActivity = (MainActivity) activity;
        }
    }

    @JavascriptInterface
    public void startScan() {
        if (mainActivity != null) {
            mainActivity.startScan();
        }
    }

    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    // 处理扫码结果
    public void handleScanResult(String result) {
        if (mainActivity != null) {
            mainActivity.evaluateJavascript(
                String.format("window.handleScanResult('%s')", result),
                null
            );
        }
    }
} 