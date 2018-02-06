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

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    ArrayList<String> Tagg = bundle.getStringArrayList("Tag");
                    ArrayList<String> Message = bundle.getStringArrayList("Inner");
                    english_view.setText(Message.get(0));
                    copysience_view.setText(Message.get(1));
                    service_view.setText(Message.get(2));
                    work_view.setText(Message.get(3));
                    textView2.setText(Tagg.get(0));
                    textView3.setText(Tagg.get(1));
                    textView4.setText(Tagg.get(2));
                    textView5.setText(Tagg.get(3));

                    break;
            }
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
                    ArrayList<String> Message = new ArrayList<String>();
                    ArrayList<String> Tagg = new ArrayList<String>();
                    for(int i=0;i<4;i++)
                    {
                        if(!information.get(i).text().equals("")){
                            Message.add(information.get(i).text());
                        } else {
                            Message.add("");
                        }
                    }
                    for(int i=0;i<=12;i+=4)
                    {
                        if(!information2.get(i).text().equals("")){
                            Tagg.add(information2.get(i).text());
                        } else {
                            Tagg.add("");
                        }
                    }
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("Tag", Tagg);
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
