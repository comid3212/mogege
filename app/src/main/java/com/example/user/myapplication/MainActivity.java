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
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    static final String COOKIES_HEADER = "Set-Cookie";
    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
    static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    static final String ACCEPT_ENCODING = "gzip, deflate";
    static final String Accept_Language  = "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7";
    static final String Referer ="http://msd.ncut.edu.tw/wbcmss/home.asp";
    static final String HOST = "msd.ncut.edu.tw";


    static class myHandler extends Handler {
        //幫忙把東西塞在ui thread裡面
        private WeakReference<Activity> reference;
        public myHandler(Activity activity){
            reference = new WeakReference<Activity>(activity);
        }

        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 0://輸入錯誤，顯示msg訊息
                    Intent intent = new Intent();
                    intent.setClass(reference.get(), slidermain.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 1://輸入成功，切換畫面

                    break;
                    //( (MainActivity)reference.get()). textView8.setText(Message.get(6));
            }
        }
    }
    private myHandler handler = new myHandler(this);

    public interface getCookieCallBack{
        void callback(String cookie);
    }
    static void setHttpUrlConnection(HttpURLConnection urlConnection){
        urlConnection.setInstanceFollowRedirects( true );
        urlConnection.addRequestProperty("User-Agent", USER_AGENT);
        urlConnection.addRequestProperty("Accept", ACCEPT);
        urlConnection.addRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
        urlConnection.addRequestProperty("Accept-Language", Accept_Language);
        urlConnection.addRequestProperty("Referer", Referer);
        urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty( "charset", "big5");
        urlConnection.setRequestProperty("Host", HOST);
    }

    static void setHttpUrlConnectionCookie(HttpURLConnection urlConnection, String cookie) {
        urlConnection.setRequestProperty("Cookie", cookie);
    }

    static void sendData(HttpURLConnection urlConnection, String data){
        byte[] postData;
        try {
            urlConnection.setDoOutput( true );
            urlConnection.setInstanceFollowRedirects( true );
            urlConnection.setRequestMethod( "POST" );
            postData = data.getBytes("big5");
            int  postDataLength = postData.length;
            urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            urlConnection.getOutputStream().write(postData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedReader getReader(HttpURLConnection urlConnection){
        try {
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "big5"));
        } catch (IOException e) {
            return null;
        }
    }
    public static BufferedReader getReader(HttpURLConnection urlConnection, String type){
        try {
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), type));
        } catch (IOException e) {
            return null;
        }
    }


    static void getCookie(final URL url, final getCookieCallBack callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {//將cooke加載
                    HttpURLConnection connect = (HttpURLConnection)url.openConnection();
                    CookieManager cookieManager = new CookieManager();//接收cookie和發送cookie
                    setHttpUrlConnection(connect);

                    Map<String, List<String>> headerFields = connect.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                    for(String cookie : cookiesHeader) {
                        cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }

                    StringBuilder tmp = new StringBuilder();
                    if( cookieManager.getCookieStore().getCookies() != null) {
                        for (HttpCookie cookies : cookieManager.getCookieStore().getCookies()) {
                            tmp.append(cookies.toString()).append(";");
                        }
                    }
                    connect.disconnect();
                    callback.callback(tmp.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private TextView id, pwd;
    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (TextView)findViewById(R.id.editText);
        pwd = (TextView)findViewById(R.id.editText2);

    }

    public void login(View view) {//登入流程
        LayoutInflater inflater = LayoutInflater.from(this);
        View loginWebView = inflater.inflate(R.layout.activity_new_login,null);
        WebView webView = loginWebView.findViewById(R.id.Webviewtest);
        webView.getSettings().setJavaScriptEnabled(true);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(loginWebView);

        final AlertDialog webViewDialog = alertBuilder.create();

        webView.setWebViewClient(new WebViewClient(){

            boolean firstGoing = true;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(request.getUrl().toString().equals("http://nmsd.ncut.edu.tw/wbcmss/")){
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
                                    Message msg = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("NAME", name);
                                    bundle.putString("ID", id);
                                    bundle.putString("CLASS", _class);
                                    webViewDialog.cancel();
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
                webViewDialog.show();
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

    }
}
