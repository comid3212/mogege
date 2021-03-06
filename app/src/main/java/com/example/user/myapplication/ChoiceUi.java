package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

public class ChoiceUi extends AppCompatActivity {
    private String cookie;
    private myHandler handler = new myHandler(this);


    public void classtable(View view) {
        Message msg = new Message();
        msg.arg1 = 1;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void changePassword(View view) {
        Message msg = new Message();
        msg.arg1 = 2;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void Doorline(View view) {
        Message msg = new Message();
        msg.arg1 = 3;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void GradesChoicelist(View view) {
        Message msg = new Message();
        msg.arg1 = 4;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void back(View view) {
        Message msg = new Message();
        msg.arg1 = 5;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void test(View view) {
        Message msg = new Message();
        msg.arg1 = 5;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void sick(View view) {
        Message msg = new Message();
        msg.arg1 = 6;
        Bundle bundle = new Bundle();
        bundle.putString("COOKIE", cookie);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class myHandler extends Handler {
        WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Intent[] intent = new Intent[1];
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    intent[0] = new Intent();
                    intent[0].setClass(reference.get(), classtable.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent[0].putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent[0]);
                    break;
                case 2:
                    intent[0] = new Intent();
                    intent[0].setClass(reference.get(), ChangePassword.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent[0].putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent[0]);
                    break;
                case 3:
                    intent[0] = new Intent();
                    intent[0].setClass(reference.get(), Doorline.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent[0].putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent[0]);
                    break;
                case 4:
                    intent[0] = new Intent();
                    intent[0].setClass(reference.get(), GradeListN.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent[0].putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent[0]);
                    break;
                case 5:
                    new AlertDialog.Builder(ChoiceUi.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要登出嗎?")
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   android.webkit.CookieManager.getInstance().removeAllCookies(null);
                                   intent[0] = new Intent().setClass(reference.get(), MainActivity.class);
                                   intent[0].setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(intent[0]);
                                }
                     })
                     .setNegativeButton("取消",
                             new DialogInterface.OnClickListener() {

                                 public void onClick(DialogInterface dialog, int which) {

                                 }
                     }).show();
                    break;



                case 6:
                    intent[0] = new Intent();
                    intent[0].setClass(reference.get(), Malingering.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入

                    //將Bundle物件assign給intent
                    intent[0].putExtras(msg.getData());

                    //切換Activity
                    reference.get().startActivity(intent[0]);
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_ui);
        cookie = this.getIntent().getExtras().getString("COOKIE");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }


    }
}