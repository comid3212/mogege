package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.ImageView;

import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Malingering extends AppCompatActivity {
    static final String COOKIES_HEADER = "Set-Cookie";
    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
    static final String ACCEPT = "application/json, text/plain, */*";
    static final String ACCEPT_ENCODING = "gzip, deflate";
    static final String CONTEXT_TYPE = "application/json;charset=UTF-8";
    static final String Accept_Language  = "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7";
    static final String Referer ="http://140.128.78.77/stdl/web/main.html";
    static final String HOST = "140.128.78.77";
    static final String Origin = "http://140.128.78.77";

    private String data ="{\"userId\":\"%s\",\"password\":\"%s\",\"captcha\":\"%s\",\"schyy\":0,\"smt\":0,\"isLogin\":true}";
    private EditText id,pwd,vcode;
    private String cookie;
    private ImageView Vcodeshow;
    private myHandler handler = new myHandler(this);
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Bundle bundle = msg.getData();
                    byte gg1[]=bundle.getByteArray("ASD");
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(gg1, 0, gg1.length);
                    Vcodeshow.setImageBitmap(decodedByte);
                    break;
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
                    builder.setTitle(msg.getData().getString("TITLE"));
                    builder.setMessage(msg.getData().getString("MESSAGE"));
                    builder.setPositiveButton("OK", null);
                    builder.show();

                    break;
                case 2:
                    Intent intent = new Intent();
                    intent.setClass(reference.get(), Malingering_Detail.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);


                    break;
            }
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malingering);
        id = (EditText) findViewById(R.id.editText7);
        pwd = (EditText) findViewById(R.id.editText8);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
//                try {
//                    byte b[] = new byte[3000];
//                    connect = (HttpURLConnection) (new URL("http://140.128.78.77/stdl/WebApi/NcitnfData/GetCaptcha")).openConnection();
//                    int length = connect.getInputStream().read(b);
//
//                    String a = new String(b, 23, length - 24);
//                    byte[] decodedString = Base64.decode(a, Base64.DEFAULT);
//                    Bundle bundle = new Bundle();
//                    Message msg = new Message();
//                    bundle.putByteArray("ASD", decodedString);
//                    msg.arg1 = 0;
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

        }).start();
    }

    public void login(View view) {//登入流程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connect = null;

                    connect = (HttpURLConnection) (new URL("http://140.128.78.77/stdl/WebApi/NcitnfData/Authorized")).openConnection();
                    String ss = String.format(data, id.getText().toString(), pwd.getText().toString(), "326093");
                    Util.setHttpUrlConnection(connect);
                    connect.setRequestProperty("Host", HOST);
                    connect.setRequestProperty("Referer", Referer);
                    connect.setRequestProperty("Origin", Origin);
                    connect.setRequestProperty("User-Agent", USER_AGENT);
                    connect.setRequestProperty("Cookie", "CheckCode=326093");
                    connect.setRequestProperty("Accept", ACCEPT);
                    connect.setRequestProperty("Content-Type", CONTEXT_TYPE);
                    connect.setRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
                    connect.setDoOutput(true);
                    connect.setRequestMethod("PUT");
                    connect.getOutputStream().write(ss.getBytes());
                    connect.getOutputStream().close();
//                    byte a[] = new byte[1000];
//                    int len = connect.getInputStream().read(a);
//                    String aaaa = new String(a, 0, len);

                    Map<String, List<String>> headerFields = connect.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
                    if(connect.getResponseCode() == 500)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("TITLE", "ID or Password ERROR");
                        bundle.putString("MESSAGE", "please check your id or password");
                        Message msg = new Message();
                        msg.arg1 = 1;
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        return;
                    }
                    else
                    {
                        android.webkit.CookieManager.getInstance().setCookie("http://140.128.78.77/", cookiesHeader.get(0));
                        Bundle bundle = new Bundle();
                        Message msg = new Message();


                        BufferedReader reader = Util.getReader(connect,"utf-8");
                        String rgloijrhoijregoirejg = reader.readLine();
                        String IDNAME = rgloijrhoijregoirejg.split("id")[1].split("type")[0];
                        bundle.putString("apple",IDNAME);
                        msg.arg1 = 2;
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    }





                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}

