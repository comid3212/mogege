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
                    ArrayList<String> className = new ArrayList<String>();
                    for(int i=0;i<4;i++)
                    {
                        if(!information.get(i).text().equals("") )
                              {
                                  information.get(i).text().equals(information.get(i).text());
                        } else {
                            information.get(i).text().equals("");
                        }
                    }
                    english_view.setText(information.get(0).text());
                    copysience_view.setText(information.get(1).text());
                    service_view.setText(information.get(2).text());
                    work_view.setText(information.get(3).text());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
