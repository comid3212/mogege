package com.example.user.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class News extends AppCompatActivity {
    private String cookie;
    private myHandler handler = new myHandler(this);
    private  TextView textView1 ,textView2,textView3,textView4;

    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Bundle bundle = msg.getData();
                    ArrayList<String> Message = bundle.getStringArrayList("Inner");
                    textView1.setText(Message.get(0));
                    textView2.setText(Message.get(1));
                    textView3.setText(Message.get(2));
                    textView4.setText(Message.get(3));

                    break;
            }
        }
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news);
            Bundle bundle = this.getIntent().getExtras();
            cookie = this.getIntent().getExtras().getString("COOKIE");
            textView1 = (TextView)findViewById(R.id.textView11);
            textView2 = (TextView)findViewById(R.id.textView12);
            textView3 = (TextView)findViewById(R.id.textView13);
            textView4 = (TextView)findViewById(R.id.textView14);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connect = null;
                    try {
                        connect = (HttpURLConnection) (new URL("http://msd.ncut.edu.tw/wbcmss/")).openConnection();
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
                        ArrayList<String> Message = new ArrayList<String>();
                        for(int i=6;i<10;i++)
                        {
                            if(!information.get(i).text().equals("")){
                                Message.add(information.get(i).text());
                            } else {
                                Message.add("");
                            }
                        }

                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("Inner", Message);
                        msg.arg1 = 0;
                        msg.setData(bundle);
                        handler.sendMessage(msg);



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
    }

