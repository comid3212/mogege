package com.example.user.myapplication;

import android.app.Activity;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.Handler;
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

public class Doorline extends AppCompatActivity {
    private String cookie;
    private TextView english_view,copysience_view,service_view,work_view;
    private TextView textView2,textView3,textView4,textView5;
    private myHandler handler = new myHandler(this);
    class myHandler extends Handler{
        private WeakReference<Activity> reference;
        myHandler(Activity activity){
            reference = new WeakReference<Activity>(activity);
        }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorline);

        cookie = this.getIntent().getExtras().getString("COOKIE");
        english_view = (TextView)findViewById(R.id. english_view);
        copysience_view = (TextView)findViewById(R.id.copysience_view);
        service_view = (TextView)findViewById(R.id.service_view );
        work_view = (TextView)findViewById(R.id.work_view);
         textView2 = (TextView) findViewById(R.id.textView2);
         textView3 = (TextView)findViewById(R.id.textView3);
         textView4 = (TextView)findViewById(R.id.textView4 );
         textView5 = (TextView)findViewById(R.id.textView5);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://msd.ncut.edu.tw/wbcmss/graduate_query.asp")).openConnection();
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
                    Elements information = document.getElementsByTag("span");
                    Document document2 = Jsoup.parse(all.toString());
                    Elements information2 = document2.getElementsByTag("td");

                    english_view.setText(information.get(0).text());
                    copysience_view.setText(information.get(1).text());
                    service_view.setText(information.get(2).text());
                    work_view.setText(information.get(3).text());
                    textView2.setText(information2.get(0).text());
                    textView3.setText(information2.get(4).text());
                    textView4.setText(information2.get(8).text());
                    textView5.setText(information2.get(12).text());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
