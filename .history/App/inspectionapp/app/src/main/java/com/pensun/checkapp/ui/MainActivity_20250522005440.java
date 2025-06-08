package com.pensun.checkapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pensun.checkapp.R;
import com.pensun.checkapp.ui.profile.ProfileActivity;
import com.pensun.checkapp.ui.scan.ScanActivity;
import com.pensun.checkapp.ui.records.RecordsActivity;

public class MainActivity extends AppCompatActivity {
    private static final boolean DEBUG = true; // 临时替代BuildConfig.DEBUG
    private WebView webView;
    private WebAppInterface webAppInterface;
    private static final int CAMERA_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnScan = findViewById(R.id.btn_scan);
        Button btnRecords = findViewById(R.id.btn_records);
        Button btnProfile = findViewById(R.id.btn_profile);

        // 检查相机权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }

        // 初始化WebView
        webView = findViewById(R.id.webview);
        setupWebView();

        btnScan.setOnClickListener(v -> startActivity(new Intent(this, ScanActivity.class)));
        btnRecords.setOnClickListener(v -> startActivity(new Intent(this, RecordsActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        if (DEBUG) {
            // 优先加载本地开发服务器，失败则降级为本地文件
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // 加载失败时自动降级为本地文件
                    view.loadUrl("file:///android_asset/dist/index.html");
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webView.loadUrl("http://10.0.2.2:5173");
        } else {
            // 生产环境：加载打包后的本地文件
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webView.loadUrl("file:///android_asset/dist/index.html");
        }

        // 设置WebChromeClient
        webView.setWebChromeClient(new WebChromeClient());

        // 添加JS接口
        webAppInterface = new WebAppInterface(this);
        webView.addJavascriptInterface(webAppInterface, "android");
    }

    // 开始扫码
    public void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("扫描巡检点二维码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    // 处理扫码结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                webAppInterface.showToast("扫描取消");
            } else {
                webAppInterface.handleScanResult(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予
            } else {
                webAppInterface.showToast("需要相机权限才能使用扫码功能");
            }
        }
    }

    // 处理返回键
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // 提供给WebAppInterface调用的JS注入方法
    public void evaluateJavascript(String script) {
        if (webView != null) {
            webView.evaluateJavascript(script, null);
        }
    }
} 