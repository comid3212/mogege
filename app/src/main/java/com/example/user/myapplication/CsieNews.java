package com.example.user.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CsieNews extends AppCompatActivity {
    private String web ="http://csie.ncut.edu.tw/";
    private  String cookie;
    private TextView textView1,textView2,textView3,textView4,textView5;
    private myHandler handler = new myHandler(this);

    class myHandler extends Handler {
        private WeakReference<Activity> reference;
        myHandler(Activity activity){
            reference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 0:
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    ArrayList<String> Message = bundle.getStringArrayList("Inner2");
                    textView1.setText(Message.get(0));
                    break;
                case 2:
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csie_news);
         final Bundle bundle = this.getIntent().getExtras();
        cookie = this.getIntent().getExtras().getString("COOKIE");
        textView1 = (TextView)findViewById(R.id.textView20);
        //textView1.setText(bundle.getString("X"));
       // textView2.setText(bundle.getString("Inn1"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    String a = bundle.getString("Inn1");
                    connect = (HttpURLConnection) (new URL(web+a)).openConnection();
                    MainActivity.setHttpUrlConnection(connect);
                    MainActivity.setHttpUrlConnectionCookie(connect, cookie);
                    MainActivity.getReader(connect);
                    BufferedReader reader = MainActivity.getReader(connect, "utf-8");//轉換顯示格式
                    StringBuilder all = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        all.append(line);
                    }
                    Document document = Jsoup.parse(all.toString());
                    Elements information = document.getElementsByTag("div");

                    ArrayList<String> Message2 = new ArrayList<String>();
                    for(int i =76;i<information.size();i++){
                        if(!information.get(i).equals("")){
                            Message2.add(information.get(i).text());
                        }
                        else
                            Message2.add("");
                    }



                    Message msg = new Message();
                    msg.arg1=1;
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("Inner2", Message2);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
