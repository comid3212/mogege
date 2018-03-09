package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import java.util.List;

public class CopyScience extends AppCompatActivity {
    private String cookie;
    private String web ="http://csie.ncut.edu.tw/";
    private TextView[] textView;
    private myHandler handler = new myHandler(this);


    public  void News(View view) {
        Bundle bundle = new Bundle();
        String In1 = textView[Integer.valueOf(view.getTag().toString())].getTag().toString();
        String x = web;
        bundle.putString("Inn1",In1);
        bundle.putString("COOKIE", cookie);
        bundle.putString("X",x);
        Message msg = new Message();
        msg.arg1 = 2;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    ArrayList<String> Ac      = bundle.getStringArrayList("Ac");
                    ArrayList<String> Message = bundle.getStringArrayList("Inner");
                    for(int i = 0; i < 5; ++i) {
                        textView[i].setText(Message.get(i));
                        textView[i].setTag(Ac.get(i));
                    }
                    //ArrayList<String> website = bundle.getStringArrayList("Inner2");

                    break;
                case 2:
                    Intent intent = new Intent();
                    intent.setClass(reference.get(), CsieNews.class);
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
        setContentView(R.layout.activity_copy_science);
        cookie = this.getIntent().getExtras().getString("COOKIE");
        textView = new TextView[7];
        textView[0] = (TextView) findViewById(R.id.textView15);
        textView[1] = (TextView) findViewById(R.id.textView16);
        textView[2] = (TextView)findViewById(R.id.textView17);
        textView[3] = (TextView)findViewById(R.id.textView18 );
        textView[4] = (TextView)findViewById(R.id.textView19);
       // textView[6] = (TextView) findViewById(R.id.textView23);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://csie.ncut.edu.tw/news.php?content=1")).openConnection();
                    Util.setHttpUrlConnection(connect);
                    Util.setHttpUrlConnectionCookie(connect, cookie);
                    Util.getReader(connect);
                    BufferedReader reader = Util.getReader(connect, "utf-8");//轉換顯示格式
                    StringBuilder all = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        all.append(line);
                    }
                    Document document = Jsoup.parse(all.toString());
                    Elements information = document.getElementsByTag("td");
                    Element element = document.getElementsByTag("td").get(168);
                    Elements rawinfomation = element.getElementsByTag("div");

                    Element element2 = document.getElementsByTag("td").get(175);
                    Elements rawinfomation2 = element2.getElementsByTag("div");

                    Element element3 = document.getElementsByTag("td").get(182);
                    Elements rawinfomation3 = element3.getElementsByTag("div");

                    Element element4 = document.getElementsByTag("td").get(189);
                    Elements rawinfomation4 = element4.getElementsByTag("div");

                    Element element5 = document.getElementsByTag("td").get(196);
                    Elements rawinfomation5 = element5.getElementsByTag("div");

                    Element element6 = document.getElementsByTag("td").get(196);
                    Elements rawinfomation6 = element6.getElementsByTag("div");

                    Element element7 = document.getElementsByTag("td").get(196);
                    Elements rawinfomation7 = element7.getElementsByTag("div");

                    Element element8 = document.getElementsByTag("td").get(196);
                    Elements rawinfomation8 = element8.getElementsByTag("div");

                    Element element9 = document.getElementsByTag("td").get(196);
                    Elements rawinfomation9 = element9.getElementsByTag("div");

                    Element element10 = document.getElementsByTag("td").get(196);
                    Elements rawinfomation10 = element10.getElementsByTag("div");


                    ArrayList<String> activitys  = new ArrayList<>();
                    for(Element ele : rawinfomation) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }
                    for(Element ele : rawinfomation2) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }
                    for(Element ele : rawinfomation3) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }
                    for(Element ele : rawinfomation4) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }
                    for(Element ele : rawinfomation5) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }


                    for(Element ele : rawinfomation6) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }
                    for(Element ele : rawinfomation7) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }
                    for(Element ele : rawinfomation8) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }
                    for(Element ele : rawinfomation9) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }
                    for(Element ele : rawinfomation10) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("href").equals("null"))

                            activitys.add(info.get(0).attr("href") );
                    }


                    ArrayList<String> Message = new ArrayList<String>();
                    ArrayList<String> Website = new ArrayList<String>();

                    for(int i=168;i<203;i+=7)
                    {
                        if(!information.get(i).text().equals("")){
                            Message.add(information.get(i).text());

                        } else {
                            Message.add("");
                        }
                    }

                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("Ac",activitys);
                    bundle.putStringArrayList("Inner", Message);
                    bundle.putStringArrayList("WEB", Website);
                    msg.arg1 = 1;
                    msg.setData(bundle);
                    handler.sendMessage(msg);


                    connect.disconnect();


                   /*connect = (HttpURLConnection) (new URL("http://csie.ncut.edu.tw/news_content.php?content=1&news_id=1161#.Wnc73a6Wapo")).openConnection();
                    MainActivity.setHttpUrlConnection(connect);
                    MainActivity.setHttpUrlConnectionCookie(connect, cookie);
                    MainActivity.getReader(connect);
                    BufferedReader reader2 = MainActivity.getReader(connect, "utf-8");//轉換顯示格式
                    document = Jsoup.parse(all.toString());
                    Elements information2 = document.getElementsByTag("p");
                    ArrayList<String> Message2 = new ArrayList<String>();
                    for(int i =1;i<8;i++){
                        if(!information2.get(i).equals("")){
                            Message2.add(information2.get(i).text());
                        }
                        else
                            Message2.add("");
                    }
                    bundle.putStringArrayList("Inner2", Message2);
                    msg.setData(bundle);
                    handler.sendMessage(msg);*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
