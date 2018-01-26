package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.BackingStoreException;

public class slidermain extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    private String cookie;
    private String website="%d-%d-%d";
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
                    break;
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
                    builder.setTitle(msg.getData().getString("TITLE"));
                    builder.setMessage(msg.getData().getString("MESSAGE"));
                    //builder.setIcon(android.R.drawable.ic_dialog_info);

                   // builder.setItems(new String[]{String.valueOf( msg.getData().getString("MESSAGE"))},null).setPositiveButton("OK", null);


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
        Bundle bundle = this.getIntent().getExtras();
        cookie = this.getIntent().getExtras().getString("COOKIE");
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

        if (id == R.id.nav_camera) {

            Intent intent = new Intent();
            intent = new Intent();
            intent.setClass(this, classtable.class);

            //new一個Bundle物件，並將要傳遞的資料傳入

            //將Bundle物件assign給intent
            Bundle bundle = new Bundle();
            bundle.putString("COOKIE", cookie);
            intent.putExtras(bundle);

            //切換Activity
            this.startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent();
            intent = new Intent();
            intent.setClass(this, ChangePassword.class);

            //new一個Bundle物件，並將要傳遞的資料傳入

            //將Bundle物件assign給intent
            Bundle bundle = new Bundle();
            bundle.putString("COOKIE", cookie);
            intent.putExtras(bundle);

            //切換Activity
            this.startActivity(intent);

        } else if (id == R.id.nav_doorline) {
            Intent intent = new Intent();
            intent = new Intent();
            intent.setClass(this, Doorline.class);

            //new一個Bundle物件，並將要傳遞的資料傳入

            //將Bundle物件assign給intent
            Bundle bundle = new Bundle();
            bundle.putString("COOKIE", cookie);
            intent.putExtras(bundle);

            //切換Activity
            this.startActivity(intent);
        } else if (id == R.id.nav_shop) {


        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent();
            intent = new Intent();
            intent.setClass(this, GradesChoicelist.class);

            //new一個Bundle物件，並將要傳遞的資料傳入

            //將Bundle物件assign給intent
            Bundle bundle = new Bundle();
            bundle.putString("COOKIE", cookie);
            intent.putExtras(bundle);

            //切換Activity
            this.startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout){

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
                    MainActivity.setHttpUrlConnection(connect);//連結到抓資訊的網址
                    BufferedReader reader = MainActivity.getReader(connect, "utf-8");//轉換顯示格式
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

}
