package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.BackingStoreException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXTransformerFactory;

public class slidermain extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    private String cookie;
    private String website="%d-%d-%d";
    private  TextView Tianchiview,chineseview;
    private ImageView TianView;
    private CalendarView testcalendar;
    static final String COOKIES_HEADER = "Set-Cookie";
    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
    static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    static final String ACCEPT_ENCODING = "gzip, deflate";
    static final String Accept_Language  = "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7";
    static final String Referer ="http://web2.ncut.edu.tw/bin/home.php";
    static final String HOST = "www.ncut.edu.tw";


    private slidermain.myHandler handler = new slidermain.myHandler(this);
    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Bundle bundle = msg.getData();
                    String moge2 = bundle.getString("Chinesetmp");
                    String moge = bundle.getString("Tianchi");
                    int moge3 = bundle.getInt("TODAYVIEW");
                    Tianchiview.setText(moge+"℃");
                    chineseview.setText(moge2);
                    Drawable drawable = getResources().getDrawable(moge3, slidermain.this.getTheme());
                    TianView.setImageDrawable(drawable);
                    break;
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
                    builder.setTitle(msg.getData().getString("TITLE"));
                    builder.setMessage(msg.getData().getString("MESSAGE"));

                    builder.setPositiveButton("OK", null);

                    builder.show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidermain);
        testcalendar=(CalendarView) findViewById(R.id.testcalendar);
        Tianchiview=(TextView) findViewById(R.id.textView21);
        chineseview=(TextView) findViewById(R.id.textView22);
        TianView = (ImageView) findViewById( R.id.imageView3);
        final Bundle bundle = this.getIntent().getExtras();
         cookie = CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        testcalendar.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView testcalendar ,int year, int month, int dayOfMonth) {
                String date = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                Toast.makeText(slidermain.this, date, Toast.LENGTH_LONG).show();
                getDateInfo(String.format(website, year, month+1,dayOfMonth));
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        ((TextView)view.findViewById(R.id.user_name)).setText(bundle.getString("NAME"));
        ((TextView)view.findViewById(R.id.id_vew)).setText(bundle.getString("ID"));
        ((TextView)view.findViewById(R.id.id_vew2)).setText(bundle.getString("CLASS"));
        final ImageView imageView = view.findViewById(R.id.imageView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    String str = bundle.getString("ID").split(":")[1];
                    str = str.substring(1);
                    String [] ddd = str.split("A");
                    String a = "http://nmsd.ncut.edu.tw/wbcmss/Uploads/Stpic/A" + ddd[1].substring(0, 1) + "/" + ddd[0] + "/" + str + "n.jpg";
                    is = (InputStream) new URL(a).getContent();
                    final Drawable d = Drawable.createFromStream(is, "src name");
                    slidermain.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageDrawable(d);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect= null;
                try {
                    connect = (HttpURLConnection)(new URL("http://opendata.cwb.gov.tw/opendataapi?dataid=F-C0032-021&authorizationkey=CWB-BC8B7AC3-2075-4464-9099-2B3F500F8910")).openConnection();

                    //BufferedReader reader = Util.getReader(connect, "utf-8");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    org.w3c.dom.Document doc = dBuilder.parse(connect.getInputStream());
                    NodeList nodeList = doc.getElementsByTagName("parameterValue");
                    String west = nodeList.item(3).getTextContent();
                    String temperature = west.split("太平區")[1].split("氣溫")[1].split("℃")[0];
                    String chinesetmp = west.split("太平區")[1].split("為")[1].split("的天氣")[0];
                    int imagetian=0;
                    if(chinesetmp.equals("晴時多雨")){
                         imagetian =R.drawable.c02;
                    }
                    else if (chinesetmp.equals("多雨時晴")){
                        imagetian=R.drawable.c05;
                    }
                    else if (chinesetmp.equals("多雲時陰")){
                        imagetian=R.drawable.c05;
                    }
                    else if (chinesetmp.equals("陰時多雲")){
                        imagetian=R.drawable.c05;
                    }
                    else if (chinesetmp.equals("多雲時晴")){
                        imagetian=R.drawable.c03;
                    }
                    else if (chinesetmp.equals("晴時多雲")){
                        imagetian=R.drawable.c03;
                    }
                    else if (chinesetmp.equals("多雲短暫雨")){
                        imagetian=R.drawable.c14;
                    }
                    else if (chinesetmp.equals("多雲短暫陣雨或雷雨")){
                        imagetian=R.drawable.c10;
                    }
                    else if (chinesetmp.equals("陰短暫陣雨或雷雨")){
                        imagetian=R.drawable.c10;
                    }
                    else if (chinesetmp.equals("多雲")){
                        imagetian=R.drawable.c15;
                    }
                    else {
                        imagetian=R.drawable.doge;
                    }

                    Bundle bundle = new Bundle();
                    Message msg = new Message();
                    bundle.putString("Tianchi",temperature);
                    bundle.putString("Chinesetmp",chinesetmp);
                    bundle.putInt("TODAYVIEW",imagetian);

                    msg.arg1=0;
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slidermain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_student) {

            Intent intent = new Intent();
            intent = new Intent();
            intent.setClass(this, ChoiceUi.class);

            //new一個Bundle物件，並將要傳遞的資料傳入

            //將Bundle物件assign給intent
            Bundle bundle = getIntent().getExtras();
            bundle.putString("COOKIE", cookie);
            intent.putExtras(bundle);

            //切換Activity
            this.startActivity(intent);
        } else if (id == R.id.nav_twohard) {
            Intent intent = new Intent();
            intent = new Intent();
            intent.setClass(this, NewsWeb.class);

            //new一個Bundle物件，並將要傳遞的資料傳入

            //將Bundle物件assign給intent
            Bundle bundle = new Bundle();
            bundle.putString("COOKIE", cookie);
            intent.putExtras(bundle);

            //切換Activity
            this.startActivity(intent);



        } else if (id == R.id.nav_class) {
            Intent intent = new Intent();
            intent = new Intent();
            intent.setClass(this, AllDepartmentNews.class);

            //new一個Bundle物件，並將要傳遞的資料傳入

            //將Bundle物件assign給intent
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);

            //切換Activity
            this.startActivity(intent);

        } else if (id == R.id.nav_shop) {

        }else if (id == R.id.nav_logout){
            new AlertDialog.Builder(slidermain.this)
            .setTitle("確認視窗")
            .setMessage("確定要登出嗎?")
            .setPositiveButton("確定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            android.webkit.CookieManager.getInstance().removeAllCookies(null);
                            Intent intent = new Intent(slidermain.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
            .setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                    }
             }).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void getDateInfo(final String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://www.ncut.edu.tw/news2/event_list_day.php?nid=" + date)).openConnection();
                    Util.setHttpUrlConnection(connect);//連結到抓資訊的網址
                    BufferedReader reader = Util.getReader(connect, "utf-8");//轉換顯示格式
                    StringBuilder all = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        all.append(line);//一行一行讀取網頁資訊
                    }

                    Document document = Jsoup.parse(all.toString());//抓取
                    Element element = document.getElementsByTag("td").get(5);//抓取網頁中td資訊中的5
                    Elements rawinfomation = element.getElementsByTag("div");//td抓div資訊
                    List<Element> infomation = new Elements();//清空資訊
                    for(int i = 1; i < rawinfomation.size() - 1; i += 7) {
                        infomation.add(rawinfomation.get(i));//重新抓取資訊
                    }


                    List<String> activitys = new ArrayList<>();
                    for(Element ele : infomation) {
                        Elements info = ele.getElementsByTag("a");
                        if(!info.attr("class").equals("null"))
                                activitys.add("\n"+info.get(0).attr("title")+"\n" );
                   }

                    Message msg = new Message();
                    msg.arg1 = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("TITLE", date);
                    bundle.putString("MESSAGE", activitys +"" );


                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            android.webkit.CookieManager.getInstance().removeAllCookies(null);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
