package com.inspection.app;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class WebAppInterface {
    private Activity activity;
    private IntentIntegrator integrator;

    public WebAppInterface(Activity activity) {
        this.activity = activity;
        this.integrator = new IntentIntegrator(activity);
    }

    @JavascriptInterface
    public void startScan() {
        activity.runOnUiThread(() -> {
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("请将二维码/条形码放入框内");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // 将扫描结果发送给 WebView
                String js = String.format("javascript:onScanResult('%s')", result.getContents());
                ((MainActivity) activity).getWebView().evaluateJavascript(js, null);
            }
        }
    }
} 