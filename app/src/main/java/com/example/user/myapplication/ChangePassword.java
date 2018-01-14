package com.example.user.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ChangePassword extends AppCompatActivity {

    private String cookie;
    private String password="oldpass=%s&PASS=%s&CONFIRM=%s&ACTION=%%BDT%%A9w";
    private TextView textView1, textView2, textView3;
    private myHandler handler = new myHandler(this);

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
                    reference.get().finish();
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        cookie = this.getIntent().getExtras().getString("COOKIE");
        textView1 = (TextView)findViewById(R.id.editText3);
        textView2 = (TextView)findViewById(R.id.editText4);
        textView3 = (TextView)findViewById(R.id.editText5);
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
                    HttpURLConnection connection = (HttpURLConnection)(new URL("http://msd.ncut.edu.tw/wbcmss/passwd_change.asp")).openConnection();
                    MainActivity.setHttpUrlConnection(connection);
                    MainActivity.setHttpUrlConnectionCookie(connection, cookie);
                    MainActivity.sendData(connection, String.format(password, textView1.getText().toString(), textView2.getText().toString(), textView2.getText().toString()));
                    BufferedReader reader = MainActivity.getReader(connection);
                    StringBuilder all = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        all.append(line);
                    }
                    Document document = Jsoup.parse(all.toString());
                    Element information = document.getElementsByTag("th").get(2);
                    Message msg = new Message();
                    if(information.text().replaceAll("\\s+","").equals("密碼已變更成功")) {
                        msg.arg1 = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("MESSAGE", information.text());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    } else {
                        msg.arg1 = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("TITLE", "ERROR");
                        bundle.putString("MESSAGE", information.text());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
