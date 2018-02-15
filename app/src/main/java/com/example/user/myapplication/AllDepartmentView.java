package com.example.user.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AllDepartmentView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_department_view);
        WebView webview = (WebView) findViewById(R.id.Webviewtest);
        Bundle bundle = this.getIntent().getExtras();
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        setContentView(webview);
        webview.setWebViewClient(new WebViewClient());
        String a = bundle.getString("GetWeb");
        webview.loadUrl(a);
    }
}
