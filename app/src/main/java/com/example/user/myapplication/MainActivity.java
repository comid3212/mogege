package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//所有chrome的title

    static class myHandler extends Handler {
        //幫忙把東西塞在ui thread裡面
        private WeakReference<MainActivity> reference;
        public myHandler(MainActivity activity){
            reference = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            reference.get().loginButton.setVisibility(View.VISIBLE);
            switch (msg.arg1){
                case 0:
                    Intent intent = new Intent();
                    intent.setClass(reference.get(), slidermain.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 1:
                    break;
                case 2:
                    Bundle bundle = msg.getData();
                    AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
                    builder.setMessage(bundle.getString("MESSAGE"));
                    builder.setTitle(bundle.getString("TITLE)"));
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    break;
            }
        }
    }

    private myHandler handler = new myHandler(this);


    private TextView id, pwd;
    private Button loginButton;
    LoginWebView webView;


    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (TextView) findViewById(R.id.editText);
        pwd = (TextView) findViewById(R.id.editText2);
        loginButton = (Button) findViewById(R.id.button);

        View loginWebView = LayoutInflater.from(this).inflate(R.layout.activity_new_login, null);
        webView = loginWebView.findViewById(R.id.Webviewtest);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setMessageCallBack(new LoginWebView.MessageCallBack() {
            @Override
            public void callback(Message msg) {
                handler.sendMessage(msg);
            }
        });
        webView.setLoginPage("http://nmsd.ncut.edu.tw/wbcmss/Auth/Login");

        String cookies = android.webkit.CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
        if(cookies != null && cookies.split("WBCMSSAUTH=").length == 2){
            loginButton.setVisibility(View.INVISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection  connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Query/Schedule")).openConnection();//檢查是否登入
                        Document document = Util.getDocumentFromUrlConnection(connect, android.webkit.CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/"));


                        Elements information = document.getElementsByTag("th");

                        if(document == null || information.size() < 3) {
                            android.webkit.CookieManager.getInstance().removeAllCookies(null);
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            msg.arg1 = 2;
                            bundle.putString("TITLE", "ERROR");
                            bundle.putString("MESSAGE", "連線逾時，請重新登入");
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                            return;
                        }

                        String id,name,_class ;
                        id = information.get(0).text();
                        name = information.get(1).text();
                        _class = information.get(2).text();
                        Message msg = new Message();
                        msg.arg1 = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("NAME", name);
                        bundle.putString("ID", id);
                        bundle.putString("CLASS", _class);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void login(View view) {//登入流程
        loginButton.setVisibility(View.INVISIBLE);
        webView.login(id.getText().toString(), pwd.getText().toString());
    }
}
