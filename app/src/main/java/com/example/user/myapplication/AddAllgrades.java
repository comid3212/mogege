package com.example.user.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
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
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    ArrayList<String> Message = bundle.getStringArrayList("Inner");
                    credit_view.setText(Message.get(0));
                    grades_view.setText(Message.get(1));
                    rank_view.setText(Message.get(2));


                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allgrades);
        cookie = CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
        credit_view = (TextView)findViewById(R.id.credit_view);
        grades_view = (TextView)findViewById(R.id.grades_view );
        rank_view = (TextView)findViewById(R.id.rank_view);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Query/History")).openConnection();
                    MainActivity.setHttpUrlConnection(connect);
                    MainActivity.setHttpUrlConnectionCookie(connect, cookie);
                    MainActivity.getReader(connect);
                    BufferedReader reader = MainActivity.getReader(connect, "utf-8");
                    StringBuilder all = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        all.append(line);
                    }
                    Document document = Jsoup.parse(all.toString());
                    Elements information = document.getElementsByTag("td");


                    ArrayList<String> Message = new ArrayList<String>();
                    for(int i=4;i<=6;i++)
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
                    msg.arg1 = 1;
                    msg.setData(bundle);
                    handler.sendMessage(msg);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
