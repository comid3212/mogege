package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private  TextView testtest;



    public void allgrades(View view) {
        Message msg = new Message();
        msg.arg1 = 1;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void search(View view) {
        Message msg = new Message();
        msg.arg1 = 0;
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
        cookie = CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
        Spinner spinner = (Spinner)findViewById(R.id.spinner);

        final String[] lunch = {"102", "103", "104", "105", "106"};
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(GradesChoicelist.this,
                android.R.layout.simple_spinner_dropdown_item,
                lunch);
        spinner.setAdapter(lunchList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GradesChoicelist.this, "你選的是" + lunch[position], Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                Message msg = new Message();
                bundle.putString("Choice",lunch[position]);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
