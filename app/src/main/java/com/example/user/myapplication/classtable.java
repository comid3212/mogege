package com.example.user.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class classtable extends AppCompatActivity {
    private String cookie;
    private int gridHeight, gridWidth;
    private RelativeLayout layout;
    private RelativeLayout tmpLayout;
    private static boolean isFirst = true;
    private TextView test1 ;
    private myHandler handler = new myHandler(this);

    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
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

                case 3:
                    Bundle bundle = msg.getData();
                    ArrayList<String> className = bundle.getStringArrayList("ClassName");
                    ArrayList<Integer> classLength = bundle.getIntegerArrayList("ClassLength");
                    test1.setText(className.get(10));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_classtable);
        cookie = this.getIntent().getExtras().getString("COOKIE");
        test1 = (TextView)findViewById(R.id.textView6);
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://msd.ncut.edu.tw/wbcmss/show_timetable.asp?detail=yes")).openConnection();
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
                    ArrayList<String> className = new ArrayList<String>();
                    ArrayList<Integer> classLength = new ArrayList<>();
                    for(int i=7;i<information.size() - 1;i++)
                    {
                        if(!information.get(i).text().equals("") &&
                                !information.get(i).attr("bgcolor").equals("yellow")){
                            className.add(information.get(i).text());
                            classLength.add(Integer.valueOf(information.get(i).attr("rowSpan")));
                        } else {
                            className.add("");
                            classLength.add(1);
                        }
                    }
                    Message msg = new Message();
                    msg.arg1 = 3;
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("ClassName", className);
                    bundle.putIntegerArrayList("ClassLength", classLength);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}




