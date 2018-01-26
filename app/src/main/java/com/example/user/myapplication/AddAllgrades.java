package com.example.user.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddAllgrades extends AppCompatActivity {
    private String cookie;
    private String years;
    private myHandler handler = new myHandler(this);
    private TextView credit_view,grades_view,rank_view;
    class myHandler extends Handler {
        WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allgrades);
        cookie = this.getIntent().getExtras().getString("COOKIE");
        credit_view = (TextView)findViewById(R.id.credit_view);
        grades_view = (TextView)findViewById(R.id.grades_view );
        rank_view = (TextView)findViewById(R.id.rank_view);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://msd.ncut.edu.tw/wbcmss/score_browse.asp?table=V05")).openConnection();
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
                    Elements information = document.getElementsByTag("td");


                    credit_view.setText(information.get(4).text());
                    grades_view.setText(information.get(5).text());
                    rank_view.setText(information.get(6).text());


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
