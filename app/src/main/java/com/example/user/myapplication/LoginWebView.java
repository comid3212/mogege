package com.example.user.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by USER on 2018/3/4.
 */


public class LoginWebView extends WebView {
    AlertDialog alertDialog;

    class MyWebViewClient extends WebViewClient {
        boolean firstGoing = true;
        void setfirstGoing(boolean b) { firstGoing = b; }
    }

    interface MessageCallBack {
        void callback(Message msg);
    }

    private MessageCallBack msgCallBack;
    private String loginPage, id, pass;

    public void setMessageCallBack(MessageCallBack msgCallBack) {
        this.msgCallBack = msgCallBack;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public void login(String id, String pass) {
        synchronized (this) {
            this.id = id;
            this.pass = pass;
            if (this.getUrl() == null || !this.getUrl().equals(loginPage)) {
                this.loadUrl(loginPage);
            } else {
                this.loadUrl("javascript:Account.value='" + id + "';Password.value='" + pass + "';btn = document.getElementsByClassName('btn btn-primary g-recaptcha')[0]; btn.click()");
                webViewClient.setfirstGoing(false);
            }
        }
    }
    MyWebViewClient webViewClient;
    public LoginWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(this);
        alertDialog = builder.create();

        webViewClient = new MyWebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                checkLogin(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            //asd860219
            @Override
            public void onPageFinished(WebView view, String url) {
                synchronized (this) {
                    if (firstGoing) {//alert(dd.style.visibility);
                        view.loadUrl("javascript:Account.value='" + id + "';Password.value='" + pass + "';btn = document.getElementsByClassName('btn btn-primary g-recaptcha')[0];dd = document.getElementsByTagName('div')[document.getElementsByTagName('div').length - 3];btn.addEventListener('click', function() { setTimeout(function() { if(dd.style.visibility == 'visible') { alert('請輸入驗證圖形') } }, 1000) } ); btn.click()");
                        firstGoing = false;
                    } else {
                        checkLogin(url);
                        view.loadUrl("javascript: a=document.getElementsByClassName('validation-summary-errors')[0];x=a.getElementsByTagName('li')[0];alert(x.textContent)");
                        firstGoing = true;
                    }
                    super.onPageFinished(view, url);
                }
            }

        };

        setWebViewClient(webViewClient);


        setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if(message.equals("請輸入驗證圖形")) {
                    alertDialog.show();
                } else if(msgCallBack != null){
                    if(alertDialog.isShowing())
                        alertDialog.cancel();
                    if(msgCallBack != null) {
                        Message msg = new Message();
                        msg.arg1 = 1;
                        msgCallBack.callback(msg);
                    }
                }
                return super.onJsConfirm(view, url, message, result);
            }
        });
    }
    private void checkLogin(String url) {
        if(url.equals("http://nmsd.ncut.edu.tw/wbcmss/")){

            if(alertDialog.isShowing())
                alertDialog.cancel();

            String cookies = android.webkit.CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
            if(cookies.split("WBCMSSAUTH=").length == 2){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connect = null;
                        try {
                            connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Query/Schedule")).openConnection();
                            Document document = Util.getDocumentFromUrlConnection(connect, android.webkit.CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/"));

                            Elements information = document.getElementsByTag("th");
                            String id,name,_class ;
                            id = information.get(0).text();
                            name = information.get(1).text();
                            _class = information.get(2).text();
                            if(msgCallBack != null) {
                                Message msg = new Message();
                                msg.arg1 = 0;
                                Bundle bundle = new Bundle();
                                bundle.putString("NAME", name);
                                bundle.putString("ID", id);
                                bundle.putString("CLASS", _class);
                                msg.setData(bundle);
                                msgCallBack.callback(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}