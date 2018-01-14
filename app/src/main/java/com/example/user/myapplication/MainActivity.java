package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
import java.util.List;
import java.util.Map;

<<<<<<< HEAD
public class MainActivity extends AppCompatActivity {//所有chrome的標頭檔


=======
public class MainActivity extends AppCompatActivity {
//所有chrome的title
>>>>>>> 507f80e3ef1dae40101dbd3f01d6d5a3b7326682
    static final String COOKIES_HEADER = "Set-Cookie";
    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
    static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    static final String ACCEPT_ENCODING = "gzip, deflate";
    static final String Accept_Language  = "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7";
    static final String Referer ="http://msd.ncut.edu.tw/wbcmss/home.asp";
    static final String HOST = "msd.ncut.edu.tw";

<<<<<<< HEAD
    class myHandler extends Handler {//
        private WeakReference<Activity> referance;
=======
    static class myHandler extends Handler {
        //幫忙把東西塞在ui thread裡面
        private WeakReference<Activity> reference;
>>>>>>> 507f80e3ef1dae40101dbd3f01d6d5a3b7326682
        public myHandler(Activity activity){
            reference = new WeakReference<Activity>(activity);
        }

        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 0:
                    AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
                    builder.setTitle(msg.getData().getString("TITLE"));
                    builder.setMessage(msg.getData().getString("MESSAGE"));
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    break;
                case 1:
                    Intent intent = new Intent();
                    intent.setClass(reference.get(), ChoiceUi.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
            }
        }
    }

    private String cookie;

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
            int    postDataLength = postData.length;
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

    static void getCookie(final URL url, final getCookieCallBack callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connect = (HttpURLConnection)url.openConnection();
                    CookieManager cookieManager = new CookieManager();
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
        final String urlParameters  = "stagex=pre&sid=%s&pass=%s&code=mnm4&code1=MNM4&action1=%%BDT%%A9w";

        try {
            final URL url = new URL("http://msd.ncut.edu.tw/wbcmss/home.asp");
            getCookie(url, new getCookieCallBack() {
                @Override
                public void callback(final String cookie) {
                    try {
                        HttpURLConnection connect = (HttpURLConnection)url.openConnection();//
                        setHttpUrlConnection(connect);//set header
                        setHttpUrlConnectionCookie(connect, cookie);//將cookie值丟入某個連線

                        sendData(connect, String.format(urlParameters, id.getText().toString(), pwd.getText().toString()));//丟入你的帳密
                        if(connect.getResponseCode() != 200)//如果錯誤
                            return;
                        BufferedReader r = getReader(connect);

                        StringBuilder all = new StringBuilder();
                        String line;
                        while((line = r.readLine()) != null) {
                            all.append(line).append("\n");
                        }

                        Document document = Jsoup.parse(all.toString());
                        if(!document.title().equals("")) {//當title是空的，代表登入成功
                            Bundle bundle = new Bundle();
                            bundle.putString("TITLE", "ID or Password ERROR");
                            bundle.putString("MESSAGE", "please check your id or password");
                            Message msg = new Message();
                            msg.arg1 = 0;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                            return;
                        }

                        connect.disconnect();//連線成功後中斷連線(因為不需要了)

                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("COOKIE", cookie);
                        msg.arg1 = 1;
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        MainActivity.this.cookie = cookie;//存入cookie
                    } catch (IOException e) {
                        e.printStackTrace(); //錯誤處理
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
