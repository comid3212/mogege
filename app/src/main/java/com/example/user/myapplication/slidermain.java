package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class slidermain extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    private String cookie;


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
                    Bundle bundle = msg.getData();
                    ArrayList<Integer> classDay = bundle.getIntegerArrayList("ClassDay");
                    ArrayList<String> classMouth = bundle.getStringArrayList("ClassMouth");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidermain);
        Bundle bundle = this.getIntent().getExtras();
        cookie = this.getIntent().getExtras().getString("COOKIE");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

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
                    MainActivity.setHttpUrlConnection(connect);
                    MainActivity.getReader(connect);
                    StringBuilder all = new StringBuilder();

                    Document document = Jsoup.parse(all.toString());
                    Element element = document.getElementsByTag("td").get(5);
                    Elements rawinfomation = element.getElementsByTag("div");
                    List<Element> infomation = new Elements();
                    for(int i = 1; i < rawinfomation.size() - 1; i += 6) {
                        infomation.add(rawinfomation.get(i));
                    }


                    List<String> activitys = new ArrayList<>();
                    for(Element ele : infomation) {
                        Elements info = ele.getElementsByTag("a");
                        activitys.add(info.get(0).text());
                   }

                    Message msg = new Message();
                    msg.arg1 = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("TITLE", date);
                    for(String activity : activitys) {
                        bundle.putString("MESSAGE", activity + "\n");
                    }
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
