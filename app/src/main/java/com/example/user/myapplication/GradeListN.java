package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class GradeListN extends AppCompatActivity {
    private  String cookie;
    private TextView test100,test101;
    private Button Allgrades_btn;
    ListView listview1000;
    private myHandler handler = new myHandler(this);
    private ListAdapter listAdapter;
    public void Allgrades(View view) {
        Message msg = new Message();
        msg.arg1 = 2;
        Bundle bundle = new Bundle();
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
    static class MyAdapter extends BaseAdapter {

        Context context;
        List<RequestInfo> infoList;

        static class RequestInfo {
            String Classes, Must, Grades,Years,Point;
        }

        public MyAdapter(Context context, List<RequestInfo> infoList){
            this.context = context;
            this.infoList = infoList;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int i) {
            return infoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.activity_grades_table, null);
            }

            if(view.getTag() != null && Integer.parseInt(view.getTag().toString()) == i) {
                return view;
            }

            TextView
                    Class_view = view.findViewById(R.id.Class_view),
                    Must_view = view.findViewById(R.id.Must_view),
                    Grades_view = view.findViewById(R.id.Grades_view),
                    Years_view = view.findViewById(R.id.Years_view);

            RequestInfo info = infoList.get(i);
            if(info.Classes.equals("學期實得學分(及格分數加上抵免學分)")){
                    view.setBackgroundColor(-1319524);
            } else {
                view.setBackgroundColor(Color.WHITE);
            }

            Class_view.setText(info.Classes);
            Must_view.setText(info.Must);
            Grades_view.setText(info.Grades);
            Years_view.setText(info.Years);

            return view;

        }
    }

    class myHandler extends Handler {
        private WeakReference<Activity> reference;

        myHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    //test100.setText("");
                   ArrayList<String> ClassName = bundle.getStringArrayList("ClassName");
                    ArrayList<String> ClassMust = bundle.getStringArrayList("ClassMust");
                    ArrayList<String> ClassGrades = bundle.getStringArrayList("ClassGrades");
                    ArrayList<String> ClassYears = bundle.getStringArrayList("ClassYears");
                    ArrayList<String> ClassPoint = bundle.getStringArrayList("ClassPoint");
                    List<MyAdapter.RequestInfo> infoList = new ArrayList<>();
                    for(int i = 0; i < ClassName.size()-1; ++i) {
                            MyAdapter.RequestInfo info = new MyAdapter.RequestInfo();
                            info.Classes = ClassName.get(i);
                            info.Must = ClassMust.get(i);
                            info.Grades = ClassGrades.get(i);
                            info.Years = ClassYears.get(i);
                            info.Point = ClassPoint.get(i);
                            infoList.add(info);

                   }


                    MyAdapter adapter;
                    adapter = new MyAdapter(reference.get() , infoList);
                    listview1000.setAdapter(adapter);
                    listview1000.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            MyAdapter.RequestInfo info = ((MyAdapter.RequestInfo)listview1000.getAdapter().getItem(i));
                            AlertDialog.Builder builder = new AlertDialog.Builder(GradeListN.this);
                            builder.setTitle(info.Classes);
                            builder.setMessage("該科為:"+info.Must+"   學分"+info.Point+"   成績"+info.Grades);
                            builder.show();
                        }
                    });


                    break;
                case 1:
                    AlertDialog.Builder builder = new AlertDialog.Builder(reference.get());
                    builder.setTitle(msg.getData().getString("TITLE"));
                    builder.setMessage(msg.getData().getString("MESSAGE"));
                    builder.setPositiveButton("OK", null);
                    builder.show();

                    break;
                case 2:
                    Intent intent;
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
        setContentView(R.layout.activity_grade_list_n);
        Allgrades_btn = (Button)findViewById(R.id.Allgrades_btn) ;
        cookie = CookieManager.getInstance().getCookie("http://nmsd.ncut.edu.tw/");

        final Bundle bundle = this.getIntent().getExtras();
        listview1000 =(ListView) findViewById(R.id.Lisrview_1000) ;

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connect = null;
                try {
                    connect = (HttpURLConnection) (new URL("http://nmsd.ncut.edu.tw/wbcmss/Query/History")).openConnection();
                    Util.setHttpUrlConnection(connect);
                    Util.setHttpUrlConnectionCookie(connect, cookie);
                    Util.getReader(connect);
                    BufferedReader reader = Util.getReader(connect, "utf-8");
                    StringBuilder all = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        all.append(line);
                    }
                    Document document = Jsoup.parse(all.toString());
                    Elements information = document.getElementsByTag("td");
                    ArrayList<String> className = new ArrayList<String>();
                    ArrayList<String> classMust = new ArrayList<String>();
                    ArrayList<String> classGrades = new ArrayList<String>();
                    ArrayList<String> classYears = new ArrayList<String>();
                    ArrayList<String> classPoint = new ArrayList<String>();
                    for (int i = 10; i < information.size() - 1;i+=8 ) {
                       className.add(information.get(i).text());
                        }
                    for (int i = 11; i < information.size() - 1;i+=8 ) {
                        classMust.add(information.get(i).text());
                    }
                    for (int i = 13; i < information.size() - 1;i+=8 ) {
                        classGrades.add(information.get(i).text());
                    }
                    for (int i = 8; i < information.size() - 1;i+=8 ) {
                        classYears.add(information.get(i).text());
                    }
                    for (int i = 12; i < information.size() - 1;i+=8 ) {
                        classPoint.add(information.get(i).text());
                    }

                    Message msg = new Message();
                    msg.arg1 = 0;
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("ClassName", className);
                    bundle.putStringArrayList("ClassMust", classMust);
                    bundle.putStringArrayList("ClassGrades", classGrades);
                    bundle.putStringArrayList("ClassYears", classYears);
                    bundle.putStringArrayList("ClassPoint", classPoint);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        }
}

