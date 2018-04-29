package com.example.user.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ChangePassword extends AppCompatActivity {

    private String cookie;
    private String password="__RequestVerificationToken=%s&Password=%s&NewPassword=%s&ConfirmPassword=%s";
    private TextView textView1, textView2, textView3;
    private myHandler handler = new myHandler(this);

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class myHandler extends Handler{
        private WeakReference<Activity> reference;
        myHandler(Activity activity){
            reference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 0:
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(reference.get());
                    builder.setTitle(msg.getData().getString("TITLE"));
                    builder.setMessage(msg.getData().getString("MESSAGE"));
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    break;
                case 1:
                    break;
                case 2:
                    Toast.makeText(reference.get(), msg.getData().getString("MESSAGE"), Toast.LENGTH_LONG).show();

                    android.webkit.CookieManager.getInstance().removeAllCookies(null);
                    Intent intent = new Intent(reference.get(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        cookie = CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
        textView1 = (TextView)findViewById(R.id.editText3);
        textView2 = (TextView)findViewById(R.id.editText4);
        textView3 = (TextView)findViewById(R.id.editText5);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    public void changePassword(View view) {
        if(!textView2.getText().toString().equals(textView3.getText().toString())){
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
            builder.setTitle("ERROR");
            builder.setMessage("密碼不符, 請重新輸入");
            builder.show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = (HttpURLConnection)(new URL("http://nmsd.ncut.edu.tw/wbcmss/Profile/ChangePassword")).openConnection();
                    Document document1 = Util.getDocumentFromUrlConnection(connection, cookie);
                    String a = document1.getElementsByTag("input").get(0).val();
                    connection = (HttpURLConnection)(new URL("http://nmsd.ncut.edu.tw/wbcmss/Profile/ChangePassword")).openConnection();
                    Util.setHttpUrlConnection(connection);
                    Util.setHttpUrlConnectionCookie(connection, cookie);
                    Util.sendData(connection, String.format(password, a, textView1.getText().toString(), textView2.getText().toString(), textView2.getText().toString()));
                    BufferedReader reader = Util.getReader(connection);
                    reader.read();
                    String url = connection.getURL().toString();

                    Message msg = new Message();
                    if(url.equals("http://nmsd.ncut.edu.tw/wbcmss/")){
                        msg.arg1 = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("MESSAGE", "密碼已變更成功，請重新登入");
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    } else {
                        msg.arg1 = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("TITLE", "ERROR");
                        bundle.putString("MESSAGE", "密碼更新失敗！");
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
