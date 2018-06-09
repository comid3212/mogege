package com.example.user.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AllDepartmentView extends AppCompatActivity {
private  WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_department_view);
        webView = (WebView) findViewById(R.id.Webviewtest);
        Bundle bundle = this.getIntent().getExtras();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        setContentView(webView);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient());
        String a = bundle.getString("GetWeb");
        webView.loadUrl(a);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            if (webView.canGoBack()){
                webView.goBack();
            }
                return true;
        }
        return super.onKeyDown(keyCode, event);//退出整个应用程序
    }
    }

