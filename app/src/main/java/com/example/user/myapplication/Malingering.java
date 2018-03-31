package com.example.user.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

public class Malingering extends AppCompatActivity {
    static final String COOKIES_HEADER = "Set-Cookie";
    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
    static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    static final String ACCEPT_ENCODING = "gzip, deflate";
    static final String Accept_Language  = "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7";
    static final String Referer ="http://140.128.78.77/stdl/web/main.html";
    static final String HOST = "140.128.78.77";
    private String data ="{\"userId\":\"%s\",\"password\":\"%s\",\"captcha\":\"%s\",\"schyy\":0,\"smt\":0,\"isLogin\":true}";
    private EditText id,pwd,vcode;
    private String cookie;
    private ImageView Vcodeshow;
    private myHandler handler = new myHandler(this);
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

                    break;
            }
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malingering);
        id = (EditText) findViewById(R.id.editText7);
        pwd = (EditText) findViewById(R.id.editText8);
        vcode = (EditText) findViewById(R.id.editText6);
        Vcodeshow = (ImageView) findViewById(R.id.imageView5);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    byte b[] = new byte[3000];
                    connect = (HttpURLConnection) (new URL("http://140.128.78.77/stdl/WebApi/NcitnfData/GetCaptcha")).openConnection();
                    int length = connect.getInputStream().read(b);

                    String a = new String(b, 23, length - 24);
                    byte[] decodedString = Base64.decode(a, Base64.DEFAULT);
                    Bundle bundle = new Bundle();
                    Message msg = new Message();
                    bundle.putByteArray("ASD", decodedString);
                    msg.arg1 = 0;
                    msg.setData(bundle);
                    handler.sendMessage(msg);


                    connect = (HttpURLConnection) (new URL("http://140.128.78.77/stdl/web/main.html")).openConnection();
                    Document document = Util.getDocumentFromUrlConnection(connect);
                    connect = (HttpURLConnection) (new URL("http://140.128.78.77/stdl/web/main.html")).openConnection();
                    Util.sendData(connect, String.format(data, id.getText().toString(), pwd.getText().toString(), vcode.getText().toString()));//丟入你的帳密
                    if (connect.getResponseCode() != 200)//如果錯誤
                        return;
                    BufferedReader r = Util.getReader(connect);

                    StringBuilder all = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        all.append(line).append("\n");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void login(View view) {//登入流程

        try {
           final URL url = new URL("http://140.128.78.77/stdl/web/main.html");
            Util.getCookie(url, android.webkit.CookieManager.getInstance(), new Util.GetCookieCallBack() {

                HttpURLConnection connect = null;
                public void callback(CookieManager manager) {
                        try {

                        connect = (HttpURLConnection) (new URL("http://140.128.78.77/stdl/web/main.html")).openConnection();
                        Document document = Util.getDocumentFromUrlConnection(connect);
                        Util.sendData(connect, String.format(data, id.getText().toString(), pwd.getText().toString(), vcode.getText().toString()));//丟入你的帳密
                        if (connect.getResponseCode() != 200)//如果錯誤
                            return;
                        BufferedReader r = Util.getReader(connect);

                        StringBuilder all = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            all.append(line).append("\n");
                        }
                            Malingering.this.cookie = cookie;//存入cookie

                    } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

