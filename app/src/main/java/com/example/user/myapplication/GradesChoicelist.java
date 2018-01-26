package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class GradesChoicelist extends AppCompatActivity {
    private String cookie;
    private String years;
    private myHandler handler = new myHandler(this);


    public void button104up(View view) {
        Message msg = new Message();
        msg.arg1 = 0;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        String a = "104";
        String b = "上學期";
        bundle.putString("學年", a);
        bundle.putString("學期", b);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void button104down(View view) {
        Message msg = new Message();
        msg.arg1 = 1;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        String a = "104";
        String b = "下學期";
        bundle.putString("學年", a);
        bundle.putString("學期", b);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void button105up(View view) {
        Message msg = new Message();
        msg.arg1 = 2;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        String a = "105";
        String b = "上學期";
        bundle.putString("學年", a);
        bundle.putString("學期", b);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void button105down(View view) {
        Message msg = new Message();
        msg.arg1 = 3;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        String a = "105";
        String b = "下學期";
        bundle.putString("學年", a);
        bundle.putString("學期", b);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void button106up(View view) {
        Message msg = new Message();
        msg.arg1 = 4;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        String a = "106";
        String b = "上學期";
        bundle.putString("學年", a);
        bundle.putString("學期", b);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void button106down(View view) {
        Message msg = new Message();
        msg.arg1 = 5;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        String a = "106";
        String b = "下學期";
        bundle.putString("學年", a);
        bundle.putString("學期", b);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void allgrades(View view) {
        Message msg = new Message();
        msg.arg1 = 6;
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
                    intent = new Intent();
                    intent.setClass(reference.get(), GradeList.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 1:
                    intent = new Intent();
                    intent.setClass(reference.get(), GradeList.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 2:
                    intent = new Intent();
                    intent.setClass(reference.get(), GradeList.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 3:
                    intent = new Intent();
                    intent.setClass(reference.get(), GradeList.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 4:
                    intent = new Intent();
                    intent.setClass(reference.get(), GradeList.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 5:
                    intent = new Intent();
                    intent.setClass(reference.get(), GradeList.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent.putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent);
                    break;
                case 6:
                    intent = new Intent();
                    intent.setClass(reference.get(), AddAllgrades.class);

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
        setContentView(R.layout.activity_grades_choicelist);
        cookie = this.getIntent().getExtras().getString("COOKIE");
    }


}