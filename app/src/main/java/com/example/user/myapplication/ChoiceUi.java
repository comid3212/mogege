package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChoiceUi extends AppCompatActivity {

    private String cookie;
    private myHandler handler = new myHandler(this);

    public void changePassword(View view) {
        Message msg = new Message();
        msg.arg1 = 2;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    class myHandler extends Handler {
        WeakReference<Activity> reference;
        myHandler(Activity activity){
            reference  = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            switch (msg.arg1){
                case 0:
                    break;
                case 1:

                case 2:
                    intent = new Intent();
                    intent.setClass(reference.get(), ChangePassword.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_ui);
        cookie = this.getIntent().getExtras().getString("COOKIE");
    }

    public void getgetCurriculum(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection)(new URL("http://msd.ncut.edu.tw/wbcmss/show_timetable.asp?detail=yes")).openConnection();
                    MainActivity.setHttpUrlConnection(connect);
                    MainActivity.setHttpUrlConnectionCookie(connect, cookie);
                    MainActivity.getReader(connect);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
