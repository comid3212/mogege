package com.example.user.myapplication;

import android.app.Activity;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
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

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    ArrayList<String> Message = bundle.getStringArrayList("Mesg");

                    english_view.setText(Message.get(0));
                    copysience_view.setText(Message.get(1));
                    service_view.setText(Message.get(2));
                    work_view.setText(Message.get(3));


                    break;
            }
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorline);
        setTitle("畢業門檻");
        //cookie = this.getIntent().getExtras().getString("COOKIE");
        cookie = CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
        english_view = (TextView)findViewById(R.id. english_view);
        copysience_view = (TextView)findViewById(R.id.copysience_view);
        service_view = (TextView)findViewById(R.id.service_view );
        work_view = (TextView)findViewById(R.id.work_view);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Graduate/GradPass")).openConnection();
                    Document document = Util.getDocumentFromUrlConnection(connect, cookie);

                    Elements information = document.getElementsByTag("li");
                    ArrayList<String> Message = new ArrayList<String>();
                    for(int i=60;i<64;i++)
                    {
                        Message.add(information.get(i).text());
                    }

                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("Mesg", Message);
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

