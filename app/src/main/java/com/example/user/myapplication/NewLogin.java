package com.example.user.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewLogin extends AppCompatActivity {
    WebView webView;
    String cookie;
    @SuppressLint("SetJavaScriptEnabled")
    private myHandler handler = new myHandler(this);

    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);}
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 0:
                    Intent intent = new Intent();
                    intent.setClass(reference.get(), slidermain.class);
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 1:

                    break;
                case 2:
                    break;
            }
        }
        }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);


        webView = findViewById(R.id.Webviewtest);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        WebViewClient webViewClient = webView.getWebViewClient();

        webView.setWebViewClient(new WebViewClient(){

            boolean firstGoing = true;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(request.getUrl().toString().equals("http://nmsd.ncut.edu.tw/wbcmss/")){
                    String cookies = CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
                    Log.d("ASDSADASDSADSAD", cookies);
                    if(cookies.split("WBCMSSAUTH=").length == 2){
                        finish();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HttpURLConnection connect = null;
                                try {
                                    connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Query/Schedule")).openConnection();
                                    Document document = Util.getDocumentFromUrlConnection(connect, cookie);

                                    Elements information = document.getElementsByTag("th");
                                    String id,name,classss ;
                                    id = information.get(0).text();
                                    name = information.get(1).text();
                                    classss = information.get(2).text();
                                    Message msg = new Message();
                                    Bundle bundle = new Bundle();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                                Bundle bundle = new Bundle();
                        Message msg = new Message();
                        msg.arg1 = 0;
                        msg.setData(bundle);
                        handler.sendMessage(msg);//
                    }

                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(firstGoing) {
                    view.loadUrl("javascript:Account.value='3A417104';Password.value='asd860219';btn = document.getElementsByClassName('btn btn-primary g-recaptcha')[0]; btn.click()");
                    firstGoing = false;
                }
                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl("http://nmsd.ncut.edu.tw/wbcmss/Auth/Login");
        setContentView(webView);

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Query/Schedule")).openConnection();
                    MainActivity.setHttpUrlConnection(connect);
                    MainActivity.setHttpUrlConnectionCookie(connect, cookie);
                    MainActivity.getReader(connect);
                    BufferedReader reader = MainActivity.getReader(connect);
                    StringBuilder all = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        all.append(line);
                    }
                    Document document = Jsoup.parse(all.toString());
                    Elements information = document.getElementsByTag("th");
                    String id ;
                    id = information.get(0).text();
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",id);
                    msg.arg1 = 0;
                    msg.setData(bundle);
                    handler.sendMessage(msg);



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
}
